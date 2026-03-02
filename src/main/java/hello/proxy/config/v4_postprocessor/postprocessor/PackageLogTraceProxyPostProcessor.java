package hello.proxy.config.v4_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class PackageLogTraceProxyPostProcessor implements BeanPostProcessor {
    private final String basePacage;
    private final Advisor advisor;

    public PackageLogTraceProxyPostProcessor(String basePacage, Advisor advisor) {
        this.advisor = advisor;
        this.basePacage = basePacage;
    }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        log.info("parameter beanName={}, bean={}", beanName, bean.getClass());
        // 프록시 적용 대상 체크
        // 프록시 적요 대상이 아니라면 원본 호출
        String packageName = bean.getClass().getPackageName();
        if(packageName == null || !packageName.startsWith(basePacage)){
            return bean;
        }
        // 프록시 대상이라면
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvisor(advisor);
        Object proxy = proxyFactory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", bean.getClass(), proxy.getClass());
        return proxy;
    }
}
