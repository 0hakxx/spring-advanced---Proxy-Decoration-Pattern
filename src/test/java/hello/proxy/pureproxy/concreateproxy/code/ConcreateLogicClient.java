package hello.proxy.pureproxy.concreateproxy.code;

public class ConcreateLogicClient {
    private ConcreateLogic logic;

    public ConcreateLogicClient(ConcreateLogic logic) {
        this.logic = logic;
    }
    public void execute() {
        this.logic.operation();
    }
}
