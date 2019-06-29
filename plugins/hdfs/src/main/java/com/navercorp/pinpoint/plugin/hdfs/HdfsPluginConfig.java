package com.navercorp.pinpoint.plugin.hdfs;

import com.navercorp.pinpoint.bootstrap.config.ProfilerConfig;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-27 14:55
 * @see
 * @since
 */
public class HdfsPluginConfig {

    private final boolean clientProfile;
    private final boolean adminProfile;
    private final boolean paramsProfile;

    public HdfsPluginConfig(ProfilerConfig config) {
        this.clientProfile = config.readBoolean(HdfsPluginConstants.HDFS_CLIENT_CONFIG, true);
        this.adminProfile = config.readBoolean(HdfsPluginConstants.HDFS_CLIENT_ADMIN_CONFIG, true);
        this.paramsProfile = config.readBoolean(HdfsPluginConstants.HDFS_CLIENT_PARAMS_CONFIG, false);
    }

    /**
     * If HDFS client profile enable
     * @return
     */
    public boolean isClientProfile() {
        return clientProfile;
    }

    public boolean isAdminProfile() {
        return adminProfile;
    }

    public boolean isParamsProfile() {
        return paramsProfile;
    }
}
