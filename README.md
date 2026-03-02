# Spring Proxy & AOP 학습 프로젝트

> 김영한 - 스프링 핵심 원리 고급편 학습 정리  
> 프록시 패턴부터 시작해서 스프링 AOP까지 단계별로 직접 구현하며 학습

---

## 📌 학습 목표

- 프록시 패턴 / 데코레이터 패턴의 개념과 차이 이해
- JDK 동적 프록시와 CGLIB의 동작 원리 이해
- 스프링 ProxyFactory, Advisor, Pointcut, Advice 구조 이해
- BeanPostProcessor를 이용한 자동 프록시 등록 원리 이해
- `@Aspect`를 활용한 스프링 AOP 최종 적용

---

## 🛠 기술 스택

- Java 11
- Spring Boot 2.5.5
- Spring AOP (AspectJ 표현식)
- CGLIB
- Lombok
- Gradle

---

## 📁 프로젝트 구조

```
src/main/java/hello/proxy/
├── ProxyApplication.java          # 메인 애플리케이션
├── app/
│   ├── v1/                        # 인터페이스 기반 앱
│   ├── v2/                        # 구체 클래스 기반 앱
│   └── v3/                        # 컴포넌트 스캔 기반 앱
├── config/
│   ├── AppV1Config.java           # V1 수동 빈 등록
│   ├── AppV2Config.java           # V2 수동 빈 등록
│   ├── v1_proxy/                  # [1단계] 수동 프록시
│   ├── v2_dynamic/                # [2단계] JDK 동적 프록시
│   ├── v3_proxyfactory/           # [3단계] ProxyFactory
│   ├── v4_postprocessor/          # [4단계] BeanPostProcessor
│   ├── v5_autoproxy/              # [5단계] 자동 프록시 생성기
│   └── v6_aop/                    # [6단계] @Aspect AOP
└── trace/
    ├── TraceId.java
    ├── TraceStatus.java
    ├── logtrace/                  # LogTrace 인터페이스 & 구현체
    ├── callback/                  # 템플릿 콜백 패턴
    └── template/                  # 템플릿 메서드 패턴

src/test/java/hello/proxy/
├── pureproxy/
│   ├── proxy/                     # 프록시 패턴 테스트
│   ├── decorator/                 # 데코레이터 패턴 테스트
│   └── concreateproxy/            # 구체 클래스 프록시 테스트
├── jdkdynamic/                    # JDK 동적 프록시 테스트
├── cglib/                         # CGLIB 테스트
└── advisor/                       # Advisor/Pointcut 테스트
```

---

## 🚀 단계별 학습 흐름

### [1단계] 실제 앱 구성 (v1, v2, v3)

| 버전 | 특징 |
|------|------|
| v1 | 인터페이스 기반 + 수동 빈 등록 |
| v2 | 구체 클래스 기반 + 수동 빈 등록 |
| v3 | 구체 클래스 기반 + 컴포넌트 스캔 자동 등록 |

---

### [2단계] 프록시 패턴 & 데코레이터 패턴 (순수 자바)

- **프록시 패턴**: 접근 제어 목적 (캐시, 권한 등)
- **데코레이터 패턴**: 부가 기능 추가 목적 (로깅, 시간 측정 등)
- 둘 다 구조는 동일하지만 **의도(Intent)** 가 다름

---

### [3단계] 수동 프록시 적용 (v1_proxy)

**인터페이스 기반 프록시 (`InterfaceProxyConfig`)**
- `OrderControllerInterfaceProxy`, `OrderServiceInterfaceProxy`, `OrderRepositoryInterfaceProxy` 직접 구현
- 각 클래스마다 프록시 클래스를 따로 만들어야 해서 **클래스 수 폭발** 문제 발생

**구체 클래스 기반 프록시 (`ConcreateProxyConfig`)**
- 인터페이스 없이 상속을 이용해 프록시 구현
- 부모 생성자 호출 시 실제 객체를 사용하지 않으므로 `super(null)` 전달

> 💡 프록시는 실제 객체(target)에게 모든 로직을 위임하므로 부모 클래스의 필드를 직접 사용하지 않아 null 전달 가능

---

### [4단계] JDK 동적 프록시 (v2_dynamic)

- `InvocationHandler`를 구현하여 **런타임에 프록시 자동 생성**
- 인터페이스가 반드시 필요 (구체 클래스에는 적용 불가)

| 핸들러 | 설명 |
|--------|------|
| `LogTraceBasicHandler` | 모든 메서드에 LogTrace 적용 |
| `LogTraceFilterHandler` | 메서드 이름 패턴 매칭으로 필터링 후 LogTrace 적용 |

```java
// 핵심 원리
Object proxy = Proxy.newProxyInstance(
    target.getClass().getClassLoader(),
    new Class[]{인터페이스.class},
    new LogTraceBasicHandler(target, logTrace)
);
```

> ⚠️ `noLog()` 같은 메서드도 인터페이스에 정의되어 있으면 JDK 동적 프록시가 무조건 가로챔  
> 패턴 불일치 시 `return null` 하면 심각한 장애 발생 → 반드시 타겟 메서드 그대로 호출해야 함

---

### [5단계] CGLIB

- 인터페이스 없이 **구체 클래스도 프록시 생성 가능** (바이트코드 조작)
- `MethodInterceptor`를 구현하여 사용
- `method.invoke(target, args)` 대신 **`methodProxy.invoke(target, args)` 사용 권장** (성능상 이유)

```java
// ⚠️ 잘못된 예 - target 대신 proxy 전달 시 StackOverflowError 무한루프 발생
Object result = method.invoke(proxy, args);

// ✅ 올바른 예
Object result = methodProxy.invoke(target, args);
```

---

### [6단계] ProxyFactory (v3_proxyfactory)

- JDK 동적 프록시 / CGLIB을 **스프링이 통합**하여 제공
- 인터페이스 있으면 JDK 동적 프록시, 없으면 CGLIB 자동 선택
- `Advice`, `Pointcut`, `Advisor` 개념 도입

```
Advisor = Pointcut (어디에?) + Advice (무엇을?)
```

| 개념 | 역할 |
|------|------|
| `Advice` | 프록시가 호출하는 부가 기능 로직 |
| `Pointcut` | 어떤 메서드에 적용할지 필터링 |
| `Advisor` | Pointcut + Advice 묶음 |

---

### [7단계] BeanPostProcessor (v4_postprocessor)

- 빈 생성 직후 프록시로 교체하는 후처리기
- `PackageLogTraceProxyPostProcessor`: 특정 패키지 내 빈만 프록시로 교체
- **Config 파일마다 프록시를 직접 등록하던 방식의 한계 해결**

```
빈 생성 → BeanPostProcessor.postProcessAfterInitialization() → 프록시로 교체 → 스프링 컨테이너에 등록
```

---

### [8단계] 자동 프록시 생성기 (v5_autoproxy)

- 스프링이 제공하는 `AnnotationAwareAspectJAutoProxyCreator` 사용
- **Advisor 빈만 등록하면 자동으로 프록시 적용**
- AspectJ 표현식으로 정밀한 포인트컷 지정 가능

```java
@Bean
public Advisor advisor() {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("execution(* hello.proxy.app..*(..))");
    return new DefaultPointcutAdvisor(pointcut, new LogTraceAdvice(logTrace));
}
```

---

### [9단계] @Aspect AOP (v6_aop) ✅ 최종

- `@Aspect` + `@Around`로 AOP를 가장 간결하게 적용
- 자동 프록시 생성기가 `@Aspect`를 자동 인식하여 Advisor로 변환 후 프록시 적용

```java
@Aspect
public class LogTraceAspect {

    @Around("execution(* hello.proxy.app..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);
            Object result = joinPoint.proceed(); // 실제 타겟 호출
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
```

---

## 🔄 전체 발전 흐름 요약

```
수동 프록시 (클래스 폭발 문제)
    ↓
JDK 동적 프록시 / CGLIB (런타임 자동 생성)
    ↓
ProxyFactory (JDK + CGLIB 통합)
    ↓
BeanPostProcessor (빈 등록 시점에 자동 프록시 교체)
    ↓
자동 프록시 생성기 (Advisor만 등록하면 끝)
    ↓
@Aspect AOP (최종, 가장 간결)
```

---

## ⚙️ 실행 방법

```bash
./gradlew bootRun
```

기본 포트: `7070`

```
http://localhost:7070/v1/request?itemId=hello
http://localhost:7070/v2/request?itemId=hello
http://localhost:7070/v3/request?itemId=hello
```

---

## 📝 핵심 개념 정리

### JDK 동적 프록시 vs CGLIB

| | JDK 동적 프록시 | CGLIB |
|---|---|---|
| 조건 | 인터페이스 필요 | 구체 클래스만 있어도 가능 |
| 방식 | 리플렉션 | 바이트코드 조작 |
| 구현 | `InvocationHandler` | `MethodInterceptor` |

### super(null) 을 쓰는 이유

구체 클래스 상속 프록시는 실제 객체(target)에게 모든 로직을 위임하므로  
부모 클래스 필드(`orderRepository` 등)를 직접 사용하지 않음.  
따라서 부모 생성자에 `null`을 전달해도 문제없음.

### 패턴 불일치 시 return null 금지

JDK 동적 프록시에서 패턴에 매칭되지 않는 메서드를 `return null`로 처리하면  
반환값이 필요한 메서드가 `null`을 반환하여 NPE 등 심각한 장애 발생.  
반드시 `method.invoke(target, args)`로 실제 메서드를 호출해야 함.

