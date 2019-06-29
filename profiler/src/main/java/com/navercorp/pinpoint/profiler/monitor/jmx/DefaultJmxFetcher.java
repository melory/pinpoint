package com.navercorp.pinpoint.profiler.monitor.jmx;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.navercorp.pinpoint.bootstrap.config.DefaultProfilerConfig;
import com.navercorp.pinpoint.bootstrap.config.ProfilerConfig;
import com.navercorp.pinpoint.common.util.PinpointThreadFactory;
import com.navercorp.pinpoint.profiler.context.module.AgentId;
import com.navercorp.pinpoint.profiler.context.module.AgentStartTime;
import com.navercorp.pinpoint.profiler.context.module.StatDataSender;
import com.navercorp.pinpoint.profiler.monitor.collector.AgentStatMetricCollector;
import com.navercorp.pinpoint.profiler.monitor.metric.AgentStatMetricSnapshot;
import com.navercorp.pinpoint.profiler.sender.DataSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-27 16:58
 * @see
 * @since
 */
public class DefaultJmxFetcher implements JmxFetcher {
    private static final long MIN_COLLECTION_INTERVAL_MS = 1000 * 10;
    private static final long MAX_COLLECTION_INTERVAL_MS = 1000 * 60;
    private static final long DEFAULT_COLLECTION_INTERVAL_MS = DefaultProfilerConfig.DEFAULT_JMX_METRIC_COLLECTION_INTERVAL_MS;
    private static final int DEFAULT_NUM_COLLECTIONS_PER_SEND = DefaultProfilerConfig.DEFAULT_NUM_JMX_METRIC_BATCH_SEND;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final long collectionIntervalMs;

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1, new PinpointThreadFactory("Pinpoint-jmx-fetcher", true));

    private final JmxCollectJob jmxCollectJob;

    @Inject
    public DefaultJmxFetcher(@StatDataSender DataSender dataSender,
                                   @AgentId String agentId, @AgentStartTime long agentStartTimestamp,
                                   @Named("AgentStatCollector") AgentStatMetricCollector<AgentStatMetricSnapshot> agentStatCollector,
                                   ProfilerConfig profilerConfig) {
        this(dataSender, agentId, agentStartTimestamp, agentStatCollector, profilerConfig.getProfileJvmStatCollectIntervalMs(), profilerConfig.getProfileJvmStatBatchSendCount());
        logger.info("DefaultJmxFetcher constructor, dataSender is: " + dataSender.getClass().getName());
    }

    public DefaultJmxFetcher(DataSender dataSender,
                                   String agentId, long agentStartTimestamp,
                                   AgentStatMetricCollector<AgentStatMetricSnapshot> agentStatCollector,
                                   long collectionIntervalMs, int numCollectionsPerBatch) {
        if (dataSender == null) {
            throw new NullPointerException("dataSender must not be null");
        }
        if (agentId == null) {
            throw new NullPointerException("agentId must not be null");
        }
        if (agentStatCollector == null) {
            throw new NullPointerException("agentStatCollector must not be null");
        }
        if (collectionIntervalMs < MIN_COLLECTION_INTERVAL_MS) {
            collectionIntervalMs = DEFAULT_COLLECTION_INTERVAL_MS;
        }
        if (collectionIntervalMs > MAX_COLLECTION_INTERVAL_MS) {
            collectionIntervalMs = DEFAULT_COLLECTION_INTERVAL_MS;
        }
        if (numCollectionsPerBatch < 1) {
            numCollectionsPerBatch = DEFAULT_NUM_COLLECTIONS_PER_SEND;
        }
        this.collectionIntervalMs = collectionIntervalMs;

        logger.info("before initializing JmxCollectJob, dataSender is: " + dataSender.getClass().getName());
        this.jmxCollectJob = new JmxCollectJob(dataSender, agentId);
        logger.info("after initializing CollectJob, dataSender is: " + dataSender.getClass().getName());

    }


    @Override
    public void start() {
        executor.scheduleAtFixedRate(jmxCollectJob, this.collectionIntervalMs, this.collectionIntervalMs, TimeUnit.MILLISECONDS);
        logger.info("DefaultJmxFetcher started, initialDelay=" + this.collectionIntervalMs + ", interval=" + this.collectionIntervalMs);
    }

    @Override
    public void stop() {
        executor.shutdown();
        try {
            executor.awaitTermination(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info("DefaultJmxFetcher stopped");
    }

}
