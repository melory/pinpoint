package com.navercorp.pinpoint.profiler.monitor.jmx;

import com.navercorp.pinpoint.profiler.util.RuntimeMXBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-27 16:26
 * @see
 * @since
 */
public class JmxFinder {

    private static final Logger logger = LoggerFactory.getLogger(JmxFinder.class);
    private static Map<JmxInfo, String> jxmInfoProcessKeyWordMap = new HashMap<>();


    /**
     * Add JMX pattern
     *
     * @param name           JMX metric name
     * @param processKeyWord process keyword
     * @return
     */
    private static Map<JmxInfo, String> addPattern(String name, List<String> queries, String processKeyWord) {
        JmxInfo jmxInfo = new JmxInfo(name, queries);
        jxmInfoProcessKeyWordMap.put(jmxInfo, processKeyWord);
        return jxmInfoProcessKeyWordMap;
    }

    // TODO init from configuration file.
    static {
        addPattern("HdfsNameNodeJmxMetrics", new ArrayList<>(Arrays.asList("Hadoop:service=NameNode,*")),
                "org.apache.hadoop.hdfs.server.namenode.NameNode");
        addPattern("YarnResourceManagerJmxMetrics",
                new ArrayList<>(Arrays.asList("Hadoop:service=ResourceManager,*")),
                "grep org.apache.hadoop.yarn.server.resourcemanager.ResourceManager");

        addPattern("HbaseHmasterStatMetrics",
                new ArrayList<>(Arrays.asList("hadoop:service=Master,name=MasterStatistics,*")),
                "org.apache.hadoop.hbase.master.HMaster");

        addPattern("HbaseHmasterRPCStatMetrics",
                new ArrayList<>(Arrays.asList("hadoop:service=HBase,name=RPCStatistics*")),
                "org.apache.hadoop.hbase.master.HMaster");
        addPattern("HbaseRegionServerStatMetrics",
                new ArrayList<>(Arrays.asList("hadoop:service=RegionServer,name=RegionServerStatistics,*")),
                "org.apache.hadoop.hbase.regionserver.HRegionServer");
        addPattern("HbaseRegionServerRPCStatMetrics",
                new ArrayList<>(Arrays.asList("hadoop:service=HBase,name=RPCStatistics*")),
                "org.apache.hadoop.hbase.regionserver.HRegionServer");
    }


    public static List<JmxInfo> find() {
        List<JmxInfo> result = new ArrayList<>();
//        List<String> vmArgs = RuntimeMXBeanUtils.getVmArgs();
        String jvmCommandLine = getJVMCommandLine();
        logger.info("JmxFinder jvmCommandLine = {}", jvmCommandLine);
        for (JmxInfo jmxInfo : jxmInfoProcessKeyWordMap.keySet()) {
            if (null != jvmCommandLine && jvmCommandLine.contains(jxmInfoProcessKeyWordMap.get(jmxInfo))) {
                result.add(jmxInfo);
                logger.info("JmxFinder found {}...", jmxInfo);
            }
        }
        return result;
    }


    public final static long getSelfPid() {
        // Java 9 only
        // return ProcessHandle.current().getPid();
        try {
            return Long.parseLong(new File("/proc/self").getCanonicalFile().getName());
        } catch (Exception e) {
            logger.error("getSelfPid failed.", e);
            return -1;
        }
    }


    public final static String getJVMCommandLine() {
        try {
            // Java 9 only
            // long pid = ProcessHandle.current().getPid();
            long pid = getSelfPid();
            logger.info("pid from /proc/self is " + pid);
            logger.info("pid from RuntimeMXBeanUtils is " + RuntimeMXBeanUtils.getPid());
            byte[] encoded = Files.readAllBytes(Paths.get("/proc/" + pid + "/cmdline"));
            // assume ISO_8859_1, but could look in /proc/<pid>/environ for LANG or something I suppose
            String commandLine = new String(encoded, StandardCharsets.ISO_8859_1);
            String modifiedCommandLine = commandLine.replace((char) 0, ' ').trim();
            return modifiedCommandLine;
        } catch (Exception e) {
            logger.error("getJVMCommandLine failed.", e);
            return null;
        }
    }
}
