package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.concreate_proxy.OrderControllerConcreateProxy;
import hello.proxy.config.v1_proxy.concreate_proxy.OrderRepositoryConcreateProxy;
import hello.proxy.config.v1_proxy.concreate_proxy.OrderServiceConcreateProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

    @Configuration
    public class ConcreateProxyConfig {
        @Bean
        public OrderControllerV2 orderControllerV1(LogTrace logTrace) {
            OrderControllerV2 orderControllerV2 = new OrderControllerV2(orderService(logTrace));
            return new OrderControllerConcreateProxy(orderControllerV2, logTrace);
        }
        @Bean
        public OrderServiceV2 orderService(LogTrace logTrace) {
            OrderServiceV2 serviceImpl = new OrderServiceV2(orderRepository(logTrace));
            return new OrderServiceConcreateProxy(serviceImpl, logTrace);
        }
        @Bean
        public OrderRepositoryV2 orderRepository(LogTrace logTrace) {
        OrderRepositoryV2 repositoryImpl = new OrderRepositoryV2();
        return new OrderRepositoryConcreateProxy(repositoryImpl, logTrace);
    }
}
