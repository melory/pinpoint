package com.navercorp.pinpoint.profiler.monitor.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.util.*;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-27 17:17
 * @see
 * @since
 */
public class JmxGet {

    private static final Logger logger = LoggerFactory.getLogger(JmxGet.class);

    public static Map<String, String> get(String query) throws Exception {
        logger.info("JmxGet get()...");
        MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
        ObjectName queryObjectName = new ObjectName(query); // "Hadoop:service=" + service + ",*"
        mbsc.queryNames(queryObjectName, null);
        Set<ObjectName> names = new TreeSet<ObjectName>(mbsc.queryNames(queryObjectName, null));

        Object val;
        Map<String, String> map = new HashMap<>();
        for (ObjectName oname : names) {
            MBeanInfo mbinfo = mbsc.getMBeanInfo(oname);
            MBeanAttributeInfo[] mbinfos = mbinfo.getAttributes();
            for (MBeanAttributeInfo mb : mbinfos) {
                val = mbsc.getAttribute(oname, mb.getName());
                map.put(mb.getName(), (val == null) ? "" : val.toString());
            }
        }
        logger.info("JmxGet get {}...", map);
        return map;
    }

    public static Map<String, String> getJmxMetrics() {
        logger.info("getJmxMetrics...");
        Map<String, String> map = new HashMap<>();
        List<JmxInfo> jmxInfos = JmxFinder.find();
        for (JmxInfo jmxInfo : jmxInfos) {
            for (String query : jmxInfo.getQueries()) {
                try {
                    map.putAll(get(query));
                } catch (Exception e) {
                    logger.error("get jxm " + jmxInfo + " failed.", e);
                }
            }
        }
        return map;
    }
}
