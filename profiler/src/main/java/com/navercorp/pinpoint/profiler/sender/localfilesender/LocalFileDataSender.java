package com.navercorp.pinpoint.profiler.sender.localfilesender;

import com.navercorp.pinpoint.common.util.StringUtils;
import com.navercorp.pinpoint.profiler.sender.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-22 10:14
 * @see
 * @since
 */
public class LocalFileDataSender implements DataSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final AsyncQueueingExecutor<Object> executor;

    protected final String baseDataDirectory;


    public LocalFileDataSender(String name, String baseDataDirectory) {
        this.baseDataDirectory = baseDataDirectory;
        final String executorName = getExecutorName(name);
        this.executor = createAsyncQueueingExecutor(1024 * 5, executorName);
    }


    private String getExecutorName(String name) {
        name = StringUtils.defaultString(name, "DEFAULT");
        return String.format("Pinpoint-LocalFileDataSender(%s)-Executor", name);
    }

    private AsyncQueueingExecutor<Object> createAsyncQueueingExecutor(int queueSize, String executorName) {
        AsyncQueueingExecutorListener<Object> listener = new DefaultAsyncQueueingExecutorListener() {
            @Override
            public void execute(Object message) {
                LocalFileDataSender.this.logData(message);
            }
        };
        final AsyncQueueingExecutor<Object> executor = new AsyncQueueingExecutor<Object>(queueSize, executorName, listener);
        return executor;
    }

    private void logData(Object message) {
        logger.info(message.toString());
        Logger localDataLogger = LoggerCache.getLogger("DefaultLog");
        if (message instanceof LocalFileData) {
            String logPath = ((LocalFileData) message).getLocalFilePath();
            if (!StringUtils.isEmpty(logPath)) {
                localDataLogger = LoggerCache.getLogger(logPath);
            }
        }
        localDataLogger.info(message.toString());

    }

    @Override
    public boolean send(Object data) {
        return executor.execute(data);
    }

    @Override
    public void stop() {
        executor.stop();
    }
}
