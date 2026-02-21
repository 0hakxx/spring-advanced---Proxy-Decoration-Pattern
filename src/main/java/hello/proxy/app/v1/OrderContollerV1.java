package hello.proxy.app.v1;

import org.springframework.web.bind.annotation.*;

@RestController
public interface OrderContollerV1 {
    @GetMapping
    @RequestMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping
    @RequestMapping("/v1/no-log")
    String noLog();
}
