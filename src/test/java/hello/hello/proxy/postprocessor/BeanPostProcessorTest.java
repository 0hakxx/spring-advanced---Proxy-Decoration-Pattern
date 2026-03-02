package hello.hello.proxy.postprocessor;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanPostProcessorTest {
    @Test
    void postProcessor() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);
    }


    @Configuration
    @Slf4j
    private class BeanPostProcessorConfig {
        @Bean(name = "beanA")
        public A a() {
            return new A();
        }
        @Bean
        public AToBPostProcessor helloPostProcessor() {
            return new AToBPostProcessor();
        }
    }

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            log.info("beanName={}, bean={}", beanName, bean);
            if(bean instanceof A) {
                return new B();
            }
            return bean;
        }
    }


    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
    } }
    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
    }
    }
}
