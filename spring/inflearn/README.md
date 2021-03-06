```java
public class OrderServiceImpl implements OrderService{

	/*
    private final MemberRepository memberRepository = new MemoryMemberRepository();
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //인터페이스와 구현체에도 둘 다 의존하고 있음, DIP 위반 -> OCP 위반
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
	*/
	private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

	public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
	this.memberRepository = memberRepository;
	this.discountPolicy = discountPolicy;
	}


    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = itemPrice - discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
```
```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(
                new MemoryMemberRepository(),
                new FixDiscountPolicy());
    }
}
```
##### 아래와 같이 변경 -> 역할과 구현 클래스가 한눈에 들어온다.
```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }
    
    private MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }

    public OrderService orderService() {
        return new OrderServiceImpl(
                memberRepository(),
                discountPolicy());
    }
    
    public DiscountPolicy discountPolicy(){
        return new FixDiscountPolicy();
    }
}
```

##### 제어권이 프레임워크에 있다면 그것은 프레임워크가 맞다
##### 반면 내가 작성한 코드가 직접 제어의 흐름을 담당한다면 그것은 라이브러리다

### 싱글톤 방식의 주의점
##### 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안된다.

##### 아래와 같은 코드를 짜도 싱글톤 보장
```java
    @Bean
    public MemberService memberService() {
        System.out.println("AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(
                memberRepository(),
                discountPolicy());
    }
```

##### 왜일까??
##### Config를 만들 때 Config 클래스를 상속받은 임의의 다른 클래스를 만들고 그 다른 클래스를 스프링 빈으로 등록한 것이다.
##### 자식 CGLIB 클래스에서 이미 스프링 컨테이너에 등록되어 있으면 스프링 컨테이너에서 찾아서 반환
##### 스프링 컨테이너에 없으면 기존 로직을 호출

##### @애노테이션에 따라 스프링은 부가기능 제공
##### @Controller : 스프링 MVC 컨트롤러로 인식
##### @Repository : 데이터 계층의 예외를 스프링 예외로 변환(서로 다른 DB의 오류를 스프링 에러로 변환)

##### 스프링 라이프 사이클
##### 1. 컨테이너 만들고 2. 빈 등록 3. 의존관계 설정

##### 의존성 필드 주입 -> 테스트 하기 힘듦
##### 테스트 코드에서는 가능

##### List, Map 사용해서 다형성 코드 유지하며 다양한 활용 가능
```java
    @Test
    void findAllBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(1000);

        int rateDiscountPrice = discountService.discount(member, 20000, "rateDiscountPolicy");
        assertThat(discountPrice).isEqualTo(2000);
    }
        
    static class DiscountService {
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }

        public int discount(Member member, int i, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            return discountPolicy.discount(member, i);
        }
    }
```
