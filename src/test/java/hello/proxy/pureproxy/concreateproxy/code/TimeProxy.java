package hello.proxy.pureproxy.concreateproxy.code;

public class TimeProxy extends ConcreateLogic{
    private ConcreateLogic logic;

    public TimeProxy(ConcreateLogic logic) {
        this.logic = logic;
    }

    @Override
    public String operation() {
        long startTime = System.currentTimeMillis();
        String result = this.logic.operation();
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        System.out.println("TimeProxy 실행 시간 : " + resultTime + "ms");
        return result;
    }
}
