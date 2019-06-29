package com.navercorp.pinpoint.profiler.monitor.jmx;

import com.navercorp.pinpoint.profiler.sender.localfilesender.LocalFileData;

import java.util.Map;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-27 17:11
 * @see
 * @since
 */
public class JmxMetricSet extends LocalFileData {
    private String agentId;
    private long startTimestamp;
    private long timestamp;
    private long collectInterval;
    Map<String, String> metrics;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCollectInterval() {
        return collectInterval;
    }

    public void setCollectInterval(long collectInterval) {
        this.collectInterval = collectInterval;
    }

    public Map<String, String> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<String, String> metrics) {
        this.metrics = metrics;
    }

    @Override
    public String toString() {
        return "JmxMetricSet{" +
                "agentId='" + agentId + '\'' +
                ", startTimestamp=" + startTimestamp +
                ", timestamp=" + timestamp +
                ", collectInterval=" + collectInterval +
                ", metrics=" + metrics +
                '}';
    }

    @Override
    public String getLocalFilePath() {
        return "JmxMetrics";
    }
}
