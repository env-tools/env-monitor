package org.envtools.monitor.provider.applications.remote;

import org.envtools.monitor.provider.applications.configurable.model.LinkBasedVersionLookupXml;
import org.envtools.monitor.provider.applications.configurable.model.TagBasedProcessLookupXml;

/**
 * Created: 12/11/16 1:00 AM
 *
 * @author Yury Yakovlev
 */
public class SshCommandGenerator {

    private static final String LINK_PRINTOUT_DELIMITER = " -> ";
    private static final String AWK_INSTRUCTION = " {print $2;} ";

    public static String generateCommandForTagBasedProcessLookup(TagBasedProcessLookupXml tagBasedProcessLookup) {
        StringBuilder cmd = new StringBuilder();

        appendProcessLookup(cmd, tagBasedProcessLookup);

        cmd.append("| wc -l");
        return cmd.toString();
    }

    public static String generateCommandForLinkBasedVersionLookup(LinkBasedVersionLookupXml linkBasedVersionLookup) {
        return String.format("ls -la %s | awk -F \"%s\" '%s'",
                linkBasedVersionLookup.getLink(),
                LINK_PRINTOUT_DELIMITER,
                AWK_INSTRUCTION);
    }

    public static String generateCommandForApplicationMemoryLookup(TagBasedProcessLookupXml tagBasedProcessLookup) {
        StringBuilder cmd = new StringBuilder();

        cmd.append("PID=`");

        appendProcessLookup(cmd, tagBasedProcessLookup);

        //Get pid
        cmd.append("| awk '{ print $2; }' ");

        //If pid is available, then grab memory value
        cmd.append("`; if [ -n \"$PID\" ]; then cat /proc/$PID/status | grep VmSize | awk -F' ' '{print $2; }'; fi;");

        return cmd.toString();
    }

    private static void appendProcessLookup(StringBuilder cmd, TagBasedProcessLookupXml tagBasedProcessLookup) {
        cmd.append("ps -ef ");

        for (String tag : tagBasedProcessLookup.getIncludeTags())
            cmd.append("| grep ").append(String.format("'%s'", tag));

        for (String tag : tagBasedProcessLookup.getExcludeTags())
            cmd.append("| grep -v ").append(String.format("'%s'", tag));

        //TODO: the statement below is a thin ice, how to make it better?
        //It will break as soon as command line option for monitored app starts containing 'grep'
        cmd.append("| grep -v grep");
    }
}
