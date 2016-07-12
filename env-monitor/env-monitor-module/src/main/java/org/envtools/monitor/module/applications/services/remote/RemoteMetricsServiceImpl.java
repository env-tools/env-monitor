package org.envtools.monitor.module.applications.services.remote;

import com.jcraft.jsch.JSchException;
import org.apache.log4j.Logger;
import org.envtools.monitor.common.ssh.SshHelperService;
import org.envtools.monitor.model.applications.ApplicationStatus;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationXml;
import org.envtools.monitor.provider.applications.configurable.model.LinkBasedVersionLookupXml;
import org.envtools.monitor.provider.applications.configurable.model.TagBasedProcessLookupXml;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Michal Skuza on 27/06/16.
 */
public class RemoteMetricsServiceImpl implements RemoteMetricsService {

    private static final Logger LOGGER = Logger.getLogger(RemoteMetricsServiceImpl.class);

    private static final String LINK_PRINTOUT_DELIMITER = " -> ";
    private static final String AWK_INSTRUCTION = " {print $2;} ";
    private static final int DEFAULT_GROUP_INDEX = 1;

    private SshHelperService sshHelperService;

    public RemoteMetricsServiceImpl(SshHelperService sshHelperService) {
        this.sshHelperService = sshHelperService;
    }

    @Override
    public Optional<ApplicationStatus> getProcessStatus(VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup) {
        StringBuilder cmd = new StringBuilder();

        appendProcessLookup(cmd, tagBasedProcessLookup);
        cmd.append("| wc -l");

        String result = executeCommand(application, cmd.toString());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        Optional<Integer> nOccurrences = extractInt(result);

        //TODO must be equal to 1, actually
        if (nOccurrences.isPresent() && nOccurrences.get() > 0) {
            return Optional.of(ApplicationStatus.RUNNING);
        }
        return Optional.of(ApplicationStatus.STOPPED);
    }

    @Override
    public Optional<String> getApplicationVersion(VersionedApplicationXml application, LinkBasedVersionLookupXml versionLookup) {

        String cmd = String.format("ls -la %s | awk -F \"%s\" '%s'",
                versionLookup.getLink(),
                LINK_PRINTOUT_DELIMITER,
                AWK_INSTRUCTION);

        String result = executeCommand(application, cmd);

        if (StringUtils.isEmpty(result)) {
            return Optional.empty();
        } else {
            String regex = versionLookup.getLinkTargetPattern();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(result);

            if (!matcher.matches()) {
               LOGGER.warn(String.format("RemoteMetricsServiceImpl.getApplicationVersion - actual link target '%s' does not match pattern '%s', " +
               " please check configuration!", result, regex));
                return Optional.empty();
            }

            String version = matcher.group(DEFAULT_GROUP_INDEX);
            if (!StringUtils.isEmpty(version)) {
               return Optional.of(result);
            } else {
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<Double> getProcessMemoryInMb(VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup) {
        StringBuilder cmd = new StringBuilder();

        appendProcessLookup(cmd, tagBasedProcessLookup);

        //Get pid
        cmd.append("| awk '{ print $2; }' ");

        // pmap requires sudo
        //| xargs pmap | grep total | awk '{print $2; }'");

        String pidCmdResult = executeCommand(application, cmd.toString());

        if (pidCmdResult.isEmpty()) {
            return Optional.empty();
        }

        Optional<Integer> pid = extractInt(pidCmdResult);
        if (!pid.isPresent()) {
            return Optional.empty();
        }

        String memCmdInKb = String.format(String.format("cat /proc/%d/status | grep VmSize | awk -F' ' '{print $2; }'", pid.get()));
        String memCmdResult = executeCommand(application, memCmdInKb.toString());

        if (memCmdResult.isEmpty()) {
            return Optional.empty();
        }

        Optional<Integer> intMemoryInKb = extractInt(memCmdResult);
        if (intMemoryInKb.isPresent()) {
            return Optional.of(intMemoryInKb.get() / 1000.0);
        }

        return Optional.empty();
    }

    private String executeCommand(VersionedApplicationXml application, String cmd) {
        try {
            return sshHelperService.getHelper(application.getHost()).cmd(cmd);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    private void appendProcessLookup(StringBuilder cmd, TagBasedProcessLookupXml tagBasedProcessLookup) {
        cmd.append("ps -ef ");

        for (String tag : tagBasedProcessLookup.getIncludeTags())
            cmd.append("| grep ").append(String.format("'%s'", tag));

        for (String tag : tagBasedProcessLookup.getExcludeTags())
            cmd.append("| grep -v ").append(String.format("'%s'", tag));

        //TODO: the statement below is a thin ice, how to make it better?
        cmd.append("| grep -v grep");
    }

    private Optional<Integer> extractInt(String str) {
        Matcher matcher = Pattern.compile("\\d+").matcher(str);

        if (!matcher.find()) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(matcher.group()));
    }
}
