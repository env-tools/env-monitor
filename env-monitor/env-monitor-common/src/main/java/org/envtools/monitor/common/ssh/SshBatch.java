package org.envtools.monitor.common.ssh;

import com.google.common.collect.*;
import com.jcraft.jsch.JSchException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created: 12/6/16 1:04 AM
 *
 * @author Yury Yakovlev
 */
public class SshBatch {

    private static final Logger LOGGER = Logger.getLogger(SshBatch.class);

    private Multimap<String, BatchTask> tasksPerHost = ArrayListMultimap.create();

    private SshHelperService sshHelperService;

    public SshBatch(SshHelperService sshHelperService) {
        this.sshHelperService = sshHelperService;
    }

    public void addToBatch(String host, String command, Consumer<String> sshResultHandler) {
        tasksPerHost.put(host, new BatchTask(host, command, sshResultHandler));
    }

    public void execute() {
        for (String host : tasksPerHost.keySet()) {
            Collection<BatchTask> tasks = null;
            try {
                tasks = tasksPerHost.get(host);
                executeHostBatch(host, tasks);
            } catch (JSchException e) {
                LOGGER.error(String.format(
                        "SshBatch.execute - error executing batch for host %s : %s", host, tasks));
            }
        }
    }

    private void executeHostBatch(String host, Collection<BatchTask> tasks) throws JSchException {
        SshHelper sshHelper = sshHelperService.getHelper(host);
        StringBuilder batchCommandBuilder = new StringBuilder();
        //We will use task ids to distinguish marker lines and command result lines
        //Also, we will use mapping to lookup task by id
        Map<String, BatchTask> taskById = Maps.newHashMap();

        for (BatchTask task : tasks) {
            batchCommandBuilder.append(String.format("echo %s;", task.getTaskId()));
            batchCommandBuilder.append(task.getCommand() + ";");
            taskById.put(task.getTaskId().toString(), task);
        }

        String batchResult = sshHelper.cmd(batchCommandBuilder.toString());
        Context context = new Context();

        for (String line : Lists.newArrayList(batchResult.split("\n"))) {
            String trimmedLine = StringUtils.trim(line);
            processBatchResultLine(trimmedLine, taskById, context);
        }
        processBatchResultEnd(taskById, context);
    }

    private void processBatchResultEnd(Map<String, BatchTask> taskById, Context context) {
        if (!StringUtils.isBlank(context.getCurrentTaskUid())) {
            //Process the result of the last command
            BatchTask currentTask = taskById
                    .get(context.getCurrentTaskUid());
            String commandResult = toSingleStringResult(context.getCurrentCommandResults());
            if (currentTask.getSshResultHandler() != null) {
                currentTask.getSshResultHandler().accept(commandResult);
            }
            LOGGER.info(String.format("SshBatch.processBatchResultEnd - extracted result of '%s' as '%s' for id %s",
                    currentTask.getCommand(), commandResult, currentTask.getTaskId()));

        }
    }

    private void processBatchResultLine(String trimmedLine, Map<String, BatchTask> taskById, Context context) {

        if (taskById.keySet().contains(trimmedLine)) {
            //This is command id
            if (!StringUtils.isBlank(context.getCurrentTaskUid())) {
                //Process the result of the command, because there was ongoing previous command
                BatchTask currentTask = taskById
                        .get(context.getCurrentTaskUid());
                String commandResult = toSingleStringResult(context.getCurrentCommandResults());
                if (currentTask.getSshResultHandler() != null) {
                    currentTask.getSshResultHandler().accept(commandResult);
                }
                LOGGER.info(String.format("SshBatch.processBatchResultLine - extracted result of '%s' as '%s' for id %s",
                        currentTask.getCommand(), commandResult, currentTask.getTaskId()));

                context.getCurrentCommandResults().clear();

            }

            context.setCurrentTaskUid(trimmedLine);

        } else {
            if (StringUtils.isBlank(context.getCurrentTaskUid())) {
                LOGGER.warn(String.format("SshBatch.executeHostBatch - some result is out of task boundaries at line #%d: %s",
                        context.getLineNumber(), trimmedLine));
            } else {
                context.getCurrentCommandResults().add(trimmedLine);
            }
        }

        context.incrementLineNumber();

    }

    private String toSingleStringResult(List<String> resultLines) {
        return StringUtils.join(resultLines, "\n");
    }

    private static class Context {
        private String currentTaskUid = "";
        private int lineNumber = 0;
        private List<String> currentCommandResults = Lists.newArrayList();

        private Context() {
        }

        public String getCurrentTaskUid() {
            return currentTaskUid;
        }

        public void setCurrentTaskUid(String currentTaskUid) {
            this.currentTaskUid = currentTaskUid;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void incrementLineNumber() {
            lineNumber++;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public List<String> getCurrentCommandResults() {
            return currentCommandResults;
        }

        public void setCurrentCommandResults(List<String> currentCommandResults) {
            this.currentCommandResults = currentCommandResults;
        }
    }

    public static class BatchTask {
        private Consumer<String> sshResultHandler;
        private String host;
        private String command;

        private UUID taskId;

        public BatchTask(String host, String command, Consumer<String> sshResultHandler) {
            this.sshResultHandler = sshResultHandler;
            this.host = host;
            this.command = command;

            taskId = UUID.randomUUID();
        }

        public Consumer<String> getSshResultHandler() {
            return sshResultHandler;
        }

        public String getHost() {
            return host;
        }

        public String getCommand() {
            return command;
        }

        public UUID getTaskId() {
            return taskId;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                    append("command", command).
                    append("taskId", taskId).
                    append("host", host).
                    toString();
        }
    }
}
