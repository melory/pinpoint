package com.navercorp.pinpoint.common.metrics;

import com.alibaba.metrics.*;

import java.util.HashMap;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-21 15:45
 * @see
 * @since
 */
public class ApmMetricExample {

    public static void main(String[] args) {

        // counter
        Counter counter = ApmMetricManager.getCounter("emr", "cpu.sysem.idle",
                new HashMap<String, String>() {{
                    put("hostName", "emr-header-1");
                    put("serviceName", "HOST");
                }},
                MetricLevel.NORMAL);

        // do sth
        counter.dec();
        // do sth
        counter.inc();
        // do sth
        counter.dec(3l);
        // do sth
        counter.inc(3l);


        // meter
        Meter meter = ApmMetricManager.getMeter("emr", "hdfs.block.report",
                new HashMap<String, String>() {{
                    put("hostName", "emr-header-1");
                    put("serviceName", "HDFS");
                }},
                MetricLevel.NORMAL);

        // interceptor on hdfs block report
        meter.mark();

        // histogram
        Histogram histogram = ApmMetricManager.getHistogram("emr", "zookeeper.write.latency",
                new HashMap<String, String>() {{
                    put("hostName", "emr-header-1");
                    put("serviceName", "ZOOKEEPER");
                }},
                MetricLevel.NORMAL);

        histogram.update(339);


        // gauge
        Gauge<Integer> jvmBufferPoolDirectGauge = new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                Integer value = 0;
                // get jvm metrics by platform mbean server
                return value;
            }

            @Override
            public long lastUpdateTime() {
                return System.currentTimeMillis();
            }
        };
        MetricManager.register("emr",
                MetricName.build("jvm.buffer_pool.direct.count"), jvmBufferPoolDirectGauge);


        // Timer
        Timer timer = ApmMetricManager.getTimer("emr",
                "org.apache.hadoop.hdfs.server.diskbalancer.command.handleNodeReport",
                new HashMap<String, String>() {{
                    put("hostName", "emr-header-1");
                    put("serviceName", "HDFS");
                }},
                MetricLevel.NORMAL);

        // interceptor in  org.apache.hadoop.hdfs.server.diskbalancer.command.handleNodeReport
        Timer.Context context = timer.time();

        try {
            // 执行业务逻辑
            // do handleNodeReport
        } catch (Exception e1) {
            // 处理异常
        } finally {
            // 停止上下文
            // timer会自动记录当前的运行时间，调用次数，从而算出qps，rt的分布情况等信息
            context.stop();
        }

        // Compass
        Compass compass = ApmMetricManager.getCompass("emr",
                "org.apache.hadoop.hdfs.server.diskbalancer.command.handleNodeReport",
                new HashMap<String, String>() {{
                    put("hostName", "emr-header-1");
                    put("serviceName", "HDFS");
                }},
                MetricLevel.NORMAL);

        // FastCompass

    }
}
