package hello.proxy.pureproxy.concreateproxy.code;

import org.junit.jupiter.api.Test;

class ConcreateProxyTest {
    @Test
    public void noProxy() {
        ConcreateLogic logic = new ConcreateLogic();
        ConcreateLogicClient client = new ConcreateLogicClient(logic);
        client.execute();
    }
    @Test
    public void addProxy() {
        ConcreateLogic logic = new ConcreateLogic();
        TimeProxy timeProxy = new TimeProxy(logic);
        ConcreateLogicClient client = new ConcreateLogicClient(timeProxy);
        client.execute();
    }
}