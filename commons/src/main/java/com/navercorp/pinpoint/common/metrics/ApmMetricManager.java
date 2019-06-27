package com.navercorp.pinpoint.common.metrics;

import com.alibaba.metrics.*;

import java.util.Map;

/**
 * APM Metric Manager
 *
 * @author meilong.hml(梅熙)
 * @date 2019-06-21 16:11
 * @see
 * @since
 */
public class ApmMetricManager {

    /**
     * Get Counter for given group, key and tags, if not exist, an instance will be created. the group name, key and
     * tags uniquely identifies the metric.
     *
     * @param group group name
     * @param key   key
     * @param tags  tags
     * @param level Severity of metric
     * @return
     */
    public static Counter getCounter(String group, String key, Map<String, String> tags, MetricLevel level) {
        MetricName metricName = new MetricName(key, tags).level(level);
        return MetricManager.getCounter(group, metricName);
    }


    /**
     * Get Meter for given group, key and tags, if not exist, an instance will be created. the group name, key and
     * tags uniquely identifies the metric.
     *
     * @param group
     * @param key
     * @param tags
     * @param level
     * @return
     */
    public static Meter getMeter(String group, String key, Map<String, String> tags, MetricLevel level) {
        MetricName metricName = new MetricName(key, tags).level(level);
        return MetricManager.getMeter(group, metricName);
    }

    /**
     * Get Histogram for given group, key and tags, if not exist, an instance will be created. the group name, key and
     * tags uniquely identifies the metric.
     *
     * @param group
     * @param key
     * @param tags
     * @param level
     * @return
     */
    public static Histogram getHistogram(String group, String key, Map<String, String> tags, MetricLevel level) {
        MetricName metricName = new MetricName(key, tags).level(level);
        return MetricManager.getHistogram(group, metricName);
    }

    /**
     * Register a Gauge metric to metric manager. the group name, key and
     * tags uniquely identifies the metric.
     *
     * @param group
     * @param key
     * @param tags
     * @param gauge
     * @param level
     */
    public static void registerGauge(String group, String key, Map<String, String> tags, Gauge gauge,
                                     MetricLevel level) {
        MetricName metricName = new MetricName(key, tags).level(level);
        MetricManager.register(group, metricName, gauge);
    }

    /**
     * Get Timer for given group, key and tags, if not exist, an instance will be created. the group name, key and
     * tags uniquely identifies the metric.
     * <pre>
     *     void doGetSth() {
     *     // 获取timer的上下文，并开始计时
     *     Timer.Context context = timer.time();
     *
     *     try {
     *         // 执行业务逻辑
     *         doCreateOrder();
     *     } catch (Exception e1) {
     *         // 处理异常
     *     } finally {
     *         // 停止上下文
     *         // timer会自动记录当前的运行时间，调用次数，从而算出qps，rt的分布情况等信息
     *         context.stop();
     *     }
     * </pre>
     *
     * @param group
     * @param key
     * @param tags
     * @param level
     * @return
     */
    public static Timer getTimer(String group, String key, Map<String, String> tags, MetricLevel level) {
        MetricName metricName = new MetricName(key, tags).level(level);
        return MetricManager.getTimer(group, metricName);
    }


    /**
     * Get Timer for given group, key and tags, if not exist, an instance will be created. the group name, key and
     * tags uniquely identifies the metric.
     *
     * @param group
     * @param key
     * @param tags
     * @param level
     * @return
     */
    public static Compass getCompass(String group, String key, Map<String, String> tags, MetricLevel level) {
        MetricName metricName = new MetricName(key, tags).level(level);
        return MetricManager.getCompass(group, metricName);
    }

    public static FastCompass getFastCompass(String group, String key, Map<String, String> tags, MetricLevel level) {
        MetricName metricName = new MetricName(key, tags).level(level);
        return MetricManager.getFastCompass(group, metricName);
    }


    public static ClusterHistogram getClusterHistogram(String group, String key, Map<String, String> tags,
                                                       MetricLevel level) {
        MetricName metricName = new MetricName(key, tags).level(level);
        return MetricManager.getClusterHistogram(group, metricName);
    }

}
