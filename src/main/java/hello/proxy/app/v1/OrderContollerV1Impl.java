package hello.proxy.app.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderContollerV1Impl implements OrderContollerV1 {

    private final OrderServiceV1 orderService;

    public OrderContollerV1Impl(OrderServiceV1 orderService) {
        this.orderService = orderService;
    }
    @Override public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }
    @Override public String noLog() {
        return "ok";
    }
}
