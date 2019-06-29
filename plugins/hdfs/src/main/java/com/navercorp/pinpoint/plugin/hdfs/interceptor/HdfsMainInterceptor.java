package com.navercorp.pinpoint.plugin.hdfs.interceptor;

import com.navercorp.pinpoint.bootstrap.context.*;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;
import com.navercorp.pinpoint.bootstrap.interceptor.scope.InterceptorScope;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-27 15:31
 * @see
 * @since
 */
public class HdfsMainInterceptor implements AroundInterceptor {
    private final MethodDescriptor descriptor;
    private final TraceContext traceContext;
    private final InterceptorScope scope;

    /**
     * Instantiates a new Hbase client main interceptor.
     *
     * @param traceContext the trace context
     * @param descriptor   the descriptor
     * @param scope        the scope
     */
    public HdfsMainInterceptor(TraceContext traceContext, MethodDescriptor descriptor, InterceptorScope scope) {
        this.traceContext = traceContext;
        this.descriptor = descriptor;
        this.scope = scope;
    }

    @Override
    public void before(Object target, Object[] args) {
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        SpanEventRecorder recorder = trace.traceBlockBegin();
        recorder.recordApi(descriptor, args);

        // To trace async invocations, you have to create AsyncContext like below, automatically attaching it to the current span event.
        AsyncContext asyncContext = recorder.recordNextAsyncContext();

        // Finally, you have to pass the AsyncContext to the thread which handles the async task.
        // How to do this depends on the target library implementation.
        scope.getCurrentInvocation().setAttachment(asyncContext);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        try {
            if (throwable != null) {
                SpanEventRecorder recorder = trace.currentSpanEventRecorder();
                recorder.recordException(throwable);
            }
        } finally {
            trace.traceBlockEnd();
        }
    }
}
