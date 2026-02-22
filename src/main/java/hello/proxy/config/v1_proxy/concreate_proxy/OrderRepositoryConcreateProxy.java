package hello.proxy.config.v1_proxy.concreate_proxy;

import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderRepositoryConcreateProxy extends OrderRepositoryV2 {
    private final OrderRepositoryV2 target;
    private LogTrace logTrace;

    public OrderRepositoryConcreateProxy(OrderRepositoryV2 target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void save(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("orderRepository.request()");
            target.save(itemId);
            logTrace.end(status);
        }
        catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
