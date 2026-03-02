package hello.hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BasicTest {
    @Test
    void basicConfig(){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);

    }

    @Slf4j
    @Configuration
    static class BasicConfig {
        @Bean(name = "beanA")
        public A a() {
            return new A();
        }
        @Bean(name = "beanB")
        public B b() {
            return new B();
    }


    static class A {
        public void call() {
            System.out.println("A 호출");
        }
    }
    static class B {
        public void call() {
            System.out.println("A 호출");
        }
    }
}
}
