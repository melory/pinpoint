package com.navercorp.pinpoint.profiler.sender.localfilesender;


import com.navercorp.pinpoint.common.util.SystemProperty;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Logger cache, if the specified logger name dose not exit in cache and does not exist in logger configuration,
 * dynamically add appenders and loggers to logger configuration and start appender and update loggers.
 *
 * @author meilong.hml(梅熙)
 * @date 2019-06-26 09:53
 * @see
 * @since 1.9.0-p1
 */
public class LoggerCache {
    private static final Logger logger = LoggerFactory.getLogger(LoggerCache.class);
    private static ConcurrentHashMap<String, Logger> loggerCache = new ConcurrentHashMap<String, Logger>();

    public static Logger getLogger(String name) {
        if (null == loggerCache.get(name)) {
            final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            final Configuration config = ctx.getConfiguration();

            boolean loggerExist = false;
            // check if this logger exist in configuration
            logger.info("start checking loggers in configuration: ");
            for (String loggerName : config.getLoggers().keySet()) {
                logger.info("logger {}: ", loggerName);
                if (name.equals(loggerName)) {
                    logger.info("logger {} exists in configuration...", loggerName);
                    loggerExist = true;
                    break;
                }
            }

            if (loggerExist) {
                loggerCache.put(name, LoggerFactory.getLogger(name));
            } else {
                Layout layout = PatternLayout.createLayout(PatternLayout.SIMPLE_CONVERSION_PATTERN, config,
                        null, null, false, false, null, null);

                DefaultRolloverStrategy defaultRolloverStrategy = DefaultRolloverStrategy.createStrategy("10", "1", null, null, config);
                SizeBasedTriggeringPolicy sizeBasedTriggeringPolicy = SizeBasedTriggeringPolicy.createPolicy("10MB");
                RollingFileAppender rollingFileAppender = RollingFileAppender.createAppender("/usr/share/aliyun/emr/pinpoint-agent/log/" +
                                SystemProperty.INSTANCE.getProperty("pinpoint.agentId", "") + "/" + name + ".log",
                        "/usr/share/aliyun/emr/pinpoint-agent/log/" + SystemProperty.INSTANCE.getProperty("pinpoint.agentId", "") +
                                "/" + name + "-%d{MM-dd-yyyy}-%i.log.gz", "true", name,
                        "false", "", "false",
                        sizeBasedTriggeringPolicy, defaultRolloverStrategy, layout, null,
                        null, null, null, config);

                rollingFileAppender.start();
                config.addAppender(rollingFileAppender);
                AppenderRef ref = AppenderRef.createAppenderRef(name, null, null);
                AppenderRef[] refs = new AppenderRef[]{ref};
                LoggerConfig loggerConfig = LoggerConfig.createLogger("false", Level.INFO, name,
                        "true", refs, null, config, null);
                loggerConfig.addAppender(rollingFileAppender, null, null);
                config.addLogger(name, loggerConfig);
                ctx.updateLoggers();
                loggerCache.put(name, LoggerFactory.getLogger(name));
                logger.info("after adding logger {}, logger config is: ", name);
                for (String appenderName : config.getAppenders().keySet()) {
                    logger.info("appender {}", appenderName);
                }

                for (String loggerName : config.getLoggers().keySet()) {
                    logger.info("logger {}", loggerName);
                }
            }
        }
        return loggerCache.get(name);
    }
}


