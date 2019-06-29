package com.navercorp.pinpoint.plugin.hdfs;

import com.navercorp.pinpoint.bootstrap.async.AsyncContextAccessor;
import com.navercorp.pinpoint.bootstrap.instrument.*;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplate;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplateAware;
import com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor;
import com.navercorp.pinpoint.bootstrap.interceptor.scope.ExecutionPolicy;
import com.navercorp.pinpoint.bootstrap.logging.PLogger;
import com.navercorp.pinpoint.bootstrap.logging.PLoggerFactory;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginSetupContext;

import java.security.ProtectionDomain;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-27 14:52
 * @see
 * @since
 */
public class HdfsPlugin implements ProfilerPlugin, TransformTemplateAware {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());

    private TransformTemplate transformTemplate;

    @Override
    public void setTransformTemplate(TransformTemplate transformTemplate) {
        logger.info("HdfsPlugin setTransformTemplate...");
        this.transformTemplate = transformTemplate;
    }

    @Override
    public void setup(ProfilerPluginSetupContext context) {
        logger.info("HdfsPlugin setup...");
        HdfsPluginConfig config = new HdfsPluginConfig(context.getConfig());
        if (!config.isClientProfile()) {
            logger.info("{} disabled", this.getClass().getSimpleName());
            return;
        }
//        if (config.isAdminProfile()) {
//            addHdfsAdminTransformer();
//        }
        addHdfsClientTransformer();
    }

    private void addHdfsClientTransformer() {

        transformTemplate.transform("org.apache.hadoop.hdfs.DFSClient", HdfsClientTransform.class);
        logger.info("HdfsPlugin addHdfsClientTransformer...");
    }

    public static class HdfsClientTransform implements TransformCallback {
        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {

            InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);

            for (InstrumentMethod method : target.getDeclaredMethods(MethodFilters.name(HdfsPluginConstants.clientMethodNames))) {
                method.addScopedInterceptor(BasicMethodInterceptor.class, HdfsPluginConstants.HDFS_CLIENT_SCOPE);
            }
            return target.toBytecode();
        }
    }
}
