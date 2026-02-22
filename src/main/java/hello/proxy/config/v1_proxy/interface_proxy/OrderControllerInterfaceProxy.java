package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderContollerV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderContollerV1 {

    private final OrderContollerV1 target;
    private final LogTrace logTrace;
    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try{
            status = logTrace.begin("orderController.request()");
            //target 호출
            String result = target.request(itemId);
            logTrace.end(status);
            return result;
        }
        catch(Exception e){
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}
