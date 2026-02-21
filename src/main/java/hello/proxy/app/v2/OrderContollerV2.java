package hello.proxy.app.v2;

import hello.proxy.app.v1.OrderContollerV1;
import hello.proxy.app.v1.OrderServiceV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderContollerV2  {

    private final OrderServiceV2 orderService;

    public OrderContollerV2(OrderServiceV2 orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/v2/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }
    @GetMapping("/v2/no-log")
    public String noLog() {
        return "ok";
    }
}
