package hello.proxy.pureproxy.proxy.code;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ProxyPatternTest {
    @Test
    void noProxy() {
        ProxyPatternClient client = new ProxyPatternClient(new RealSubject());
        client.execute();
        client.execute();
        client.execute();
    }

    @Test
    void cacheProxyTest(){
        RealSubject realSubject = new RealSubject();
        CacheProxy cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();
    }
}