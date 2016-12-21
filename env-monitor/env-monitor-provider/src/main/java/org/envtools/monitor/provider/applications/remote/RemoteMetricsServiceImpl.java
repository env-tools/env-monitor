package org.envtools.monitor.provider.applications.remote;

import com.jcraft.jsch.JSchException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.envtools.monitor.common.ssh.SshBatch;
import org.envtools.monitor.common.ssh.SshHelperService;
import org.envtools.monitor.model.applications.ApplicationStatus;
import org.envtools.monitor.provider.applications.configurable.model.*;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.envtools.monitor.provider.applications.remote.SshCommandGenerator.generateCommandForApplicationMemoryLookup;
import static org.envtools.monitor.provider.applications.remote.SshCommandGenerator.generateCommandForLinkBasedVersionLookup;
import static org.envtools.monitor.provider.applications.remote.SshCommandGenerator.generateCommandForTagBasedProcessLookup;
import static org.envtools.monitor.provider.applications.remote.SshCommandResultProcessors.returnApplicationMemoryResultProcessor;
import static org.envtools.monitor.provider.applications.remote.SshCommandResultProcessors.returnLinkBasedVersionLookupResultProcessor;
import static org.envtools.monitor.provider.applications.remote.SshCommandResultProcessors.returnTagBasedProcessLookupResultProcessor;

/**
 * Created by Michal Skuza on 27/06/16.
 * Refactored later by Yury Yakovlev
 */
public class RemoteMetricsServiceImpl implements RemoteMetricsService {

    private static final Logger LOGGER = Logger.getLogger(RemoteMetricsServiceImpl.class);

    private SshHelperService sshHelperService;

    private BasicUrlService urlService;

    private static final int DEFAULT_GROUP_INDEX = 1;

    public RemoteMetricsServiceImpl(SshHelperService sshHelperService, BasicUrlService urlService) {
        this.sshHelperService = sshHelperService;
        this.urlService = urlService;
    }

    @Override
    public Optional<ApplicationStatus> getProcessStatus(VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup) {

        String result = executeCommand(application,
                generateCommandForTagBasedProcessLookup(tagBasedProcessLookup));

        Holder<Optional<ApplicationStatus>> holder = new Holder<>();
        returnTagBasedProcessLookupResultProcessor(holder::setValue).accept(result);

        return holder.getValue();
    }

    @Override
    public void getProcessStatusUsingSshBatch(SshBatch sshBatch, Consumer<Optional<ApplicationStatus>> resultHandler,
                                              VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup) {

        addCommand(application,
                generateCommandForTagBasedProcessLookup(tagBasedProcessLookup),
                sshBatch,
                returnTagBasedProcessLookupResultProcessor(resultHandler));

    }

    @Override
    public Optional<String> getApplicationVersion(VersionedApplicationXml application, LinkBasedVersionLookupXml versionLookup) {

        String result = executeCommand(application,
                generateCommandForLinkBasedVersionLookup(versionLookup));

        Holder<Optional<String>> holder = new Holder<>();
        returnLinkBasedVersionLookupResultProcessor(versionLookup, holder::setValue).accept(result);

        return holder.getValue();
    }

    @Override
    public void getApplicationVersionUsingSshBatch(SshBatch sshBatch, Consumer<Optional<String>> resultHandler,
                                                   VersionedApplicationXml application,
                                                   LinkBasedVersionLookupXml versionLookup) {
        addCommand(application,
                generateCommandForLinkBasedVersionLookup(versionLookup),
                sshBatch,
                returnLinkBasedVersionLookupResultProcessor(versionLookup, resultHandler));
    }

    //This method is not used
    @Override
    public Optional<String> getApplicationVersion(VersionedApplicationXml application, ScriptBasedVersionLookupXml versionLookup) {
//        String cmd = String.format("/bin/bash -c '%s %s'",
//                versionLookup.getScriptPath(),
//                versionLookup.getScriptParameters()
//        );
//        String result = executeCommand(application, cmd);
//
//        if (StringUtils.isEmpty(result)) {
//            return Optional.empty();
//        } else {
//            return Optional.of(result);
//        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getApplicationVersion(VersionedApplicationXml application, WebResourceBasedVersionLookupXml versionLookup) {

        Optional<String> resource = urlService.readResource(versionLookup.getUrl(), versionLookup.getHeaders());
        if (resource.isPresent()) {
            Pattern pattern = Pattern.compile(versionLookup.getRegexp());
            for (String line : resource.get().split("\\n")) {
                line = StringUtils.trim(line);
                Matcher matcher = pattern.matcher(line);

                if (matcher.matches()) {
                    try {
                        return Optional.ofNullable(matcher.group(DEFAULT_GROUP_INDEX));
                    } catch (IndexOutOfBoundsException | IllegalStateException e) {
                        LOGGER.warn(String.format(
                                "RemoteMetricsServiceImpl.getApplicationVersion - " +
                                        "could not get group with index %d / regexp %s for line %s",
                                DEFAULT_GROUP_INDEX, versionLookup.getRegexp(), line));
                    }
                }
            }
            LOGGER.warn("None of lines of resource " + versionLookup.getUrl() + " matched " + versionLookup.getRegexp());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Double> getProcessMemoryInMb(VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup) {

        String result = executeCommand(application, generateCommandForApplicationMemoryLookup(tagBasedProcessLookup));

        Holder<Optional<Double>> holder = new Holder<>();
        returnApplicationMemoryResultProcessor(holder::setValue).accept(result);

        return holder.getValue();
    }

    @Override
    public void getProcessMemoryInMbUsingSshBatch(SshBatch sshBatch, Consumer<Optional<Double>> resultHandler, VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup) {
        addCommand(application,
                generateCommandForApplicationMemoryLookup(tagBasedProcessLookup),
                sshBatch,
                returnApplicationMemoryResultProcessor(resultHandler));
    }

    private void addCommand(VersionedApplicationXml application, String cmd, SshBatch sshBatch, Consumer<String> handler) {
        sshBatch.addToBatch(application.getHost(), cmd, handler);
    }

    private String executeCommand(VersionedApplicationXml application, String cmd) {
        try {
            return sshHelperService.getHelper(application.getHost()).cmd(cmd);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Holder<T> {
        private T value;

        private Holder() {
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
