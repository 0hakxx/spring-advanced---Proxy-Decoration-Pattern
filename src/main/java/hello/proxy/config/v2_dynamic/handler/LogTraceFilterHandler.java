package hello.proxy.config.v2_dynamic.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


@Slf4j
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;

    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String... patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 메서드 이름 추출
        String methodName = method.getName();

        // 패턴 매칭: patterns에 포함된 메서드만 로그 적용
        boolean isMatch = false;
        for (String pattern : patterns) {
            if (methodName.startsWith(pattern)) {
                isMatch = true;
                break;
            }
        }

        // 패턴 매칭 실패 시 로그 없이 실제 메서드만 호출
        if (!isMatch) {
            return method.invoke(target, args);  // ✅ 실제 메서드 호출하고 결과 반환
        }

        // 패턴 매칭 성공 시 로그 적용
        TraceStatus status = null;
        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logTrace.begin(message);
            //target 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}