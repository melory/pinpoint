package com.navercorp.pinpoint.plugin.hdfs;

import com.navercorp.pinpoint.common.trace.AnnotationKey;
import com.navercorp.pinpoint.common.trace.AnnotationKeyFactory;
import com.navercorp.pinpoint.common.trace.ServiceType;
import com.navercorp.pinpoint.common.trace.ServiceTypeFactory;

import static com.navercorp.pinpoint.common.trace.AnnotationKeyProperty.VIEW_IN_RECORD_SET;
import static com.navercorp.pinpoint.common.trace.ServiceTypeProperty.RECORD_STATISTICS;
import static com.navercorp.pinpoint.common.trace.ServiceTypeProperty.TERMINAL;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-27 14:57
 * @see
 * @since
 */
public class HdfsPluginConstants {

    public HdfsPluginConstants() {
    }

    /**
     * The constant HDFS_CLIENT.
     */
    public static final ServiceType HDFS_CLIENT = ServiceTypeFactory.of(9300, "HDFS_CLIENT", TERMINAL, RECORD_STATISTICS);

    /**
     * The constant HDFS_CLIENT_PARAMS.
     */
    public static final AnnotationKey HDFS_CLIENT_PARAMS = AnnotationKeyFactory.of(330, "hdfs.client.params", VIEW_IN_RECORD_SET);

    /**
     * The constant HDFS_DESTINATION_ID.
     */
    public static final String HDFS_DESTINATION_ID = "HDFS";

    /**
     * The constant HDFS_CLIENT_SCOPE.
     */
    public static final String HDFS_CLIENT_SCOPE = "HDFS_CLIENT_SCOPE";

    /**
     * The constant HDFS_CLIENT_CONFIG.
     */
    public static final String HDFS_CLIENT_CONFIG = "profiler.hdfs.client.enable";


    /**
     * The constant HDFS_CLIENT_ADMIN_CONFIG.
     */
    public static final String HDFS_CLIENT_ADMIN_CONFIG = "profiler.hdfs.client.admin.enable";

    /**
     * The constant HDFS_CLIENT_PARAMS_CONFIG.
     */
    public static final String HDFS_CLIENT_PARAMS_CONFIG = "profiler.hdfs.client.params.enable";

    /**
     * The constant clientMethodNames.
     */
    public static final String[] clientMethodNames = new String[]{"create", "append", "rename", "delete", "listPaths", "getFileInfo"};
}
