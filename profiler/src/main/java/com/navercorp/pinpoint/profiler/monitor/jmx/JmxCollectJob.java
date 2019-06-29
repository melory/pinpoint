package com.navercorp.pinpoint.profiler.monitor.jmx;

import com.navercorp.pinpoint.profiler.monitor.collector.AgentStatMetricCollector;
import com.navercorp.pinpoint.profiler.monitor.metric.AgentStatMetricSnapshot;
import com.navercorp.pinpoint.profiler.monitor.metric.AgentStatMetricSnapshotBatch;
import com.navercorp.pinpoint.profiler.sender.DataSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-27 17:01
 * @see
 * @since
 */
public class JmxCollectJob implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataSender dataSender;
    private final String agentId;
    private long prevCollectionTimestamp = System.currentTimeMillis();
    private JmxMetricSet jmxMetricSet;

    public JmxCollectJob(DataSender dataSender, String agentId) {
        if (dataSender == null) {
            throw new NullPointerException("dataSender must not be null");
        }
        this.dataSender = dataSender;
        this.agentId = agentId;
        this.jmxMetricSet = new JmxMetricSet();
        logger.info("JmxCollectJob constructor, dataSender is: " + dataSender.getClass().getName());
    }

    @Override
    public void run() {
        logger.info("start to collect jmx metrics...");
        final long currentCollectionTimestamp = System.currentTimeMillis();
        final long collectInterval = currentCollectionTimestamp - this.prevCollectionTimestamp;
        try {
            jmxMetricSet.setMetrics(JmxGet.getJmxMetrics());
            jmxMetricSet.setAgentId(agentId);
            jmxMetricSet.setCollectInterval(collectInterval);
            jmxMetricSet.setTimestamp(currentCollectionTimestamp);
            sendJmxMetrics();
        } catch (Exception ex) {
            logger.warn("JMX metric collect failed. Caused:{}", ex.getMessage(), ex);
        } finally {
            this.prevCollectionTimestamp = currentCollectionTimestamp;
        }
    }

    private void sendJmxMetrics() {
        dataSender.send(jmxMetricSet);
    }
}
