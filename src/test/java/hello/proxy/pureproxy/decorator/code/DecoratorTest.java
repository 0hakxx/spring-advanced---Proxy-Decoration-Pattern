package hello.proxy.pureproxy.decorator.code;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorTest {
    @Test
    public void noDecoratorTest() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);
        client.execute();
    }
    @Test
    public void decoratorTest() {
        Component realComponent = new RealComponent();
        Component decoratorDecorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(decoratorDecorator);
        client.execute();
    }
    @Test
    public void timedecoratorTest() {
        Component realComponent = new RealComponent();
        Component decoratorDecorator = new MessageDecorator(realComponent);
        Component timeDecorator = new TimeDecorator(decoratorDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        client.execute();
    }
}