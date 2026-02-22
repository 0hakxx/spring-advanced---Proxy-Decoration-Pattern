package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {
    @Test
    public void reflection0() {
        Hello target = new Hello();
        log.info("targetClass={}", target.getClass());
        String result1 = target.callA();
        log.info("result1={}", result1);
        String result2 = target.callB();
        log.info("result2={}", result2);
    }
    @Test
    public void reflection1() throws Exception {
        Class helloClass = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello target = new Hello();
        //callA 메서드 정보
        Method callA = helloClass.getMethod("callA");
        Object result1 = callA.invoke(target);
        log.info("result1={}", result1);

        //callB 메서드 정보
        Method callB = helloClass.getMethod("callB");
        Object result2 = callB.invoke(target);
        log.info("result2={}", result2);
    }

    @Test
    public void reflection2() throws Exception {
        Class helloClass = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello target = new Hello();
        //callA 메서드 정보
        Method callA = helloClass.getMethod("callA");
        dynamicCall(callA, target);
        //callB 메서드 정보
        Method callB = helloClass.getMethod("callB");
        dynamicCall(callB, target);
    }
    private void dynamicCall(Method method,  Object target) throws Exception {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }






    private class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }
        public String callB() {
            log.info("callB");
            return "B";
        }
    }

}
