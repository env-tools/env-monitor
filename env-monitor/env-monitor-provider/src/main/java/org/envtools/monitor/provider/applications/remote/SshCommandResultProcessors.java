package org.envtools.monitor.provider.applications.remote;

import org.apache.log4j.Logger;
import org.envtools.monitor.model.applications.ApplicationStatus;
import org.envtools.monitor.provider.applications.configurable.model.LinkBasedVersionLookupXml;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created: 12/11/16 1:31 AM
 *
 * @author Yury Yakovlev
 */
public class SshCommandResultProcessors {

    private static final Logger LOGGER = Logger.getLogger(SshCommandResultProcessors.class);

    private static final int DEFAULT_GROUP_INDEX = 1;

    public static Consumer<String> returnTagBasedProcessLookupResultProcessor(Consumer<Optional<ApplicationStatus>> resultHandler) {
        return s ->
                {
                    if (StringUtils.isEmpty(s)) {
                        resultHandler.accept(Optional.<ApplicationStatus>empty());
                        return;
                    }

                    Optional<Integer> nOccurrences = extractInt(s);

                    if (nOccurrences.isPresent() && nOccurrences.get() > 0) {
                        resultHandler.accept(Optional.of(ApplicationStatus.RUNNING));
                        return;
                    }
                    resultHandler.accept(Optional.of(ApplicationStatus.STOPPED));
                    return;
                };
    }

    public static Consumer<String> returnLinkBasedVersionLookupResultProcessor(LinkBasedVersionLookupXml linkBasedVersionLookup,
                                                                               Consumer<Optional<String>> resultHandler) {
        return s ->
                {
                    s = StringUtils.trimWhitespace(s);

                    if (StringUtils.isEmpty(s)) {
                        resultHandler.accept(Optional.<String>empty());
                        return;
                    } else {
                        String regex = linkBasedVersionLookup.getLinkTargetPattern();
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(s);

                        if (!matcher.matches()) {
                            LOGGER.warn(String.format("RemoteMetricsServiceImpl.returnLinkBasedVersionLookupResultProcessor - actual link target '%s' does not match pattern '%s', " +
                                    " please check configuration!", s, regex));
                            resultHandler.accept(Optional.<String>empty());
                            return;
                        }

                        String version = matcher.group(DEFAULT_GROUP_INDEX);
                        if (!StringUtils.isEmpty(version)) {
                            resultHandler.accept(Optional.of(version));
                            return;
                        } else {
                            resultHandler.accept(Optional.<String>empty());
                            return;
                        }
                    }
                };
    }

    public static Consumer<String> returnApplicationMemoryResultProcessor(Consumer<Optional<Double>> resultHandler) {
        return s ->
                {
                    if (StringUtils.isEmpty(s)) {
                        resultHandler.accept(Optional.<Double>empty());
                        return;
                    }

                    Optional<Integer> intMemoryInKb = extractInt(s);
                    if (intMemoryInKb.isPresent()) {
                        resultHandler.accept(Optional.of(intMemoryInKb.get() / 1000.0));
                        return;
                    } else {
                        resultHandler.accept(Optional.<Double>empty());
                        return;
                    }
                };
    }

    private static Optional<Integer> extractInt(String str) {
        Matcher matcher = Pattern.compile("\\d+").matcher(str);

        if (!matcher.find()) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(matcher.group()));
    }
}
