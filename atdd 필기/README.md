##### 페어프로그래밍, 자동화된 테스트, 인수테스트 - 계획한 시나리오대로 기능이 잘 동작하는지 검증

##### 인수테스트
##### 배포 없이 검증할 수 있음 -> 빠른 피드백
##### 팀 도메인과 비즈니스 플로우 파악에 도움
##### 인수테스트가 없었을 때는 로컬에 배포해서 기능 동작 확인(Swagger가 있던지 화면까지 개발해야 했음) -> 구현하고 피드백받는 사이클이 길어짐
##### 수동테스트를 줄이고 자동테스트를 늘려가자

##### 실패하는 테스트를 만들고 패스하는 테스트를 만든 후 리팩토링
##### TDD -> 검증뿐만 아니라 설계하는 과정도 포함시킬 수 있음
##### 내가 만들 코드의 설계를 하는 과정
##### TDD에 앞서 인수테스트를 작성한다면 그렇지 않은 TDD보다는 막막한 느낌을 덜 받을 수 있다.
##### TDD만 하면 어느 범위까지 해야할지 모르니깐

##### REST가 제안하는 제약이 6가지가 있는데

##### BDD? ATDD?
##### 명세, 요구사항, 스펙 관점이면 TDD는 테스트, ATDD는 인수테스트, BDD는 Behavior
##### 객체 지향 생활 체조
##### Growing Object-Oriented Software, Guided by Tests(테스트 주도 개발로 배우는 객체 지향 설계와 실천)
##### RESTful Web API
##### 인수테스트란?
##### 계약의 요구 사항이 충족되는지 확인하기 위해 수행되는 테스트
##### 사용자 스토리를 기능적으로 테스트하는 것을 의미

##### 인수테스트의 특징?
##### 너무 기술지향적인 용어로 작성을 하면 클라이언트가 읽기 힘듦
##### 명세는 기술적인 용어 없이 작성을 해야한다.
##### 그런 의미에서 블랙박스 테스트(내부 구조나 작동, 어떤 기술을 썼는지 모르게)해야 한다.
##### 결국 인수테스트는 클라이언트 입장에서 인수한다는 것이구만
##### 어떤 요구사항이 있는데 공통의 이해를 도모하는 것
##### 인수 -> 소프트웨어를 의뢰한 클라이언트
##### 인수 조건 -> 시나리오 기반으로 인수조건을 만듦

##### 기존에 데이터가 등록되어있어서 중복에 대해 에러를 발생시키는경우는 어떤식으로 테스트를 작성하나요?

##### given절을 이용하여 상황을 가정시키고....
##### 인수테스트 -> 유지보수하는데 도움을 주는거라고 생각하면 될듯

##### @SpringBootTest는 ApplicationContext을 그대로 테스트 환경에서 사용할 수 있음

##### RANDOM_PORT, DEFINED_PORT는 실제 톰캣을 띄운다.

##### MockMvc, WebTestClient, RestAssured
##### MockMvc - @SpringBootTest에서 webEnvironment.MOCK과 함께 사용, mocking된 web environment 구성
##### WebTestClient - Webflux가 포함되어 있는 starter package에 있음, 톰캣을 사용하는 환경(dispatcher servlet)이 아님, Netty를 기본으로 사용
##### RestAssured - 실제 web environment(Apache Tomcat)을 사용하여 test

##### 각각의 테스트를 어떻게 격리할 것인가?
##### @DirtiesContext -> 테스트가 각각 돌 때 context를 초기화해준다. 새로운 context를 로드해준다.
##### 왜 영향을 주는가? 테스트 할때마다 context를 새로 띄우면 비용이 많이 든다.
##### 캐싱하는 기능이 있으므로 테스트를 돌 떄 똑같은 context를 사용하므로 테스트가 격리가 안됨

##### Cucumber, JBehave와 같은 BDD 도구는 무엇??
---------------------------------------------------------------------------------------
### 3월 11일 2회차

##### LineService에서 Station 조회를 위해 참조하는 객체는 STationService vs StationRepository

##### Service로 하면 비즈니스 로직 중복을 막을 수 있다. 예를 들어 유효성 검사가 있는 경우엔 service를 사용하면 좋을듯 하지만 참조가 많다면 쓰면 안될 것 같다.

##### 인수 시나리오 Step에는 포함되진 않지만 테스트를 하면서 요청이나 응답을 통해 값을 추출해야할 필요가 있는 경우 어디에서 값을 가져오는게 좋을까요?
##### 다른 클래스에서 재사용이 필요한 메서드는 스텝클래스로 뺐음

##### 컨트롤러에서 서비스로 파라미터 넘길 때 DTO or Entity - 정답은 없음

##### JPA는 리플렉션을 사용ㄴ

##### 현재 저희가 진행하는 미션의 경우 Station을 생성, 수정하는 API가 존재하기 때문에, 테스트 픽스처를 생성할 때 블랙박스 테스트에 맞게 STation 생성 API를 호출하여 테스트 픽스처를 생성할 수 있는데, 실무에서 사용중인 프로젝트에는 그렇지 않은 케이스가 많더라구요 이런 경우는 테스트 픽스처를 어떻게 생성하는게 가장 Best Practice?

##### => repository나 data.sql 등으로 할 수도 있지만 블랙박스 테스트를 할 수 있는 환경을 만드는게 중요

##### domain 관련 custom exception은 어디에 두나?
##### 해당 도메인 패키지에 exception을 두는걸 추천

##### ExceptionHandler도 컨트롤러로 처리?

##### 이름 중복 시 어떤 응답 상태 코드를 내려줄 것인가?

##### 인수 테스트 이후의 TDD
##### 단위 테스트와 도구
##### Outside In TDD vs Inside Out TDD
##### 테스트 더블

##### 단위 테스트 - 특정 단위(테스트 대상)가 의도한대로 작동하는지 검증
##### 단위란? 소프트웨어 시스템의 작은 부분에 초점을 맞춘 저수준
##### private 메소드는 어떻게 검증을 할 것인가? private을 검증할 필요가 있는가?
##### 결국 public 단위?

##### 단위테스트 종류 통합 단위 테스트, 고립 단위 테스트?

```java
void setUp(){
	lines = new Lines(lines, lines2, lines3) //Sociable UnitTest 진짜 객체를 이용하여 만듦
}

public  void findPath(){
	Graph graph = new Graph(lines);
}
```

```java
void setUp(){
	lines = mock(Lines.class) //고립 단위 테스트 (Solitary Unit Test)
}

```

##### Test Double
##### 테스트 목적으로 실제 객체 대신 사용되는 객체에 대한 일반 용어
##### 실제를 가짜 버전으로 대채한다는 의미

##### Mock, Spy, Stub 등등

##### Stubbing? -> 테스트 동안 호출이 되면 미리 지정된 답변으로 동작
##### DB에 연결이 되어 있지 않더라도 stubbing을 통해 테스트 가능, 상태를 검증

##### Mocking -> 상태가 아닌 행위를 검증

##### Spying -> 내가 지정한 부분만 다르게 동작

##### Mock과 Stub의 차이?

##### SpringExtension -> SpringContext를 사용하므로 느리다.

##### 실제 객체를 사용하는 것을 추천하지만 테스트 작성이 어렵거나 흐름이 잘 이어지지 않는다면 테스트 더블을 활용하는 방법도 추천

##### 테스트가 프로덕션 코드에 의존적이면 잘 깨질 수도 있음

##### 통합 테스트란?
##### 각 모듈이, 독립된 단위가 서로 연결될 때 올바르게 잘 작동하는지 확인
##### 넓은 의미 vs 좁은 의미
##### 여러 모듈로 구성된 시스템이 잘 돌아가는가? vs 개별된 모듈로 구성된 시스템이 잘 돌아가는가?
##### 내가 수정할 수 없는 부분과 잘 어울려 동작하는가? -> 좁은 의미이 통합 테스트
##### 예를 들어 실제 DB가 아닌 테스트만을 위한 DB를 구성해서 동작하는지 검증을 많이 함, 별도의 설치가 필요없는 H2와 같은 것들

##### TDD -> 인수테스트 후 어떻게 개발할까?
##### Outside In 외부 바깥 영역에서 시작하여 안쪽으로 접근하는 방법, test 작성하며 서비스, 컨트롤러 등 만들어주는듯
```java
@ExtendWith(MockitoExtension.class)
class LineServiceTest{
	@Mock
	LineRepository lineRepository; //가짜 객체

	@Test
	void saveLine(){
		//디비에 line을 저장한다.
		// LineREsponse를 응답한다.

		//when
		LineService lineService = new LineService();
		LineRequest lineRequest = new LineRequest("name", "color", 1L, 2L, 10);
		//LineRepository lineRepository = new LineRepository(); 실제 객체로 만들어주기보다는 @Mock을 써서 가짜 객체를 만들어주자
		
		when(lineRepository.save(any())).thenReturn(new Line(1L, "name", "color")); //stubbing 방식  테스트할 대상을 미리지정하고 관계를 맺을 협력 객체를 미리 지정
		//any() 어떤 객체든 상관 없다

		LineResponse lineResponse = lineService.saveLine(lineRequst);

		//then
		assertThat(lineResponse).isNotNull();
		assertThat(lineREsponse.getId()).isNotNull();
	}
}
```

##### 중복제거 -> 서비스단? 디비단?(엔터티 unique) => 상황에 따라

##### insdie Out tdd => 전통적인 방법 , test 작성하며 도메인의 메서드 추가

```java
public class LineTest{
	@Test
	void addSection(){
		//given
		Station gangnam = new Station("강남역");
		Station jeongja = new Station("정자역");
		Station gwangyo = new Station("광교역");
		Line line = new Line("신분당선", "red", gangnam, jeongja, 3);

		//when
		assertThatThrownBy(() -> {
			line.addSection(gangnam, gwangyo, 10);
		}).isInstanceOf(RuntimeException.class);

		//then
		assertThat(line.getSTations().size()).isEqualTo(2);
	}
}
```
##### 즉 테스트에서 명세(체크리스트)를 어느정도 하고 비즈니스 로직을 구현하는 것이 tdd 이구만

##### 서비스 레이어에 단위 테스트를 만들기는 쉽지 않다고 하네
##### 복잡한 비즈니스가 있는 경우에만 서비스 레이어 tdd를 하자라고 정할 수도 있음

----------------------------------------------------------------------------------------------------------
##### 3월 18일 강의 atdd 리팩터링

##### stubbing(mocking)
##### ReflectionTestUtils

##### 테스트하기 어려운 코드가 있을 때 테스트 하기 쉬운 코드로 바꿔보자(객체 지향적인 생각)
##### 인수테스트시 중복되는 것들이 많을 것이다.
##### 리팩터링을 어떻게 할 것인가?
##### 인수테스트를 통합하자(생성부터 삭제까지 한번에) -> 사용자의 관점
##### 1. 테스트 비용 절감
##### 2. 중복을 효과적으로 제거
##### 3. 사용자 스토리에 대한 검증 가능
##### 4. 사이드 케이스는 단위 테스트에서 수행하게 유도
##### 5. 단점은 관리하기가 좀 어려움

##### 통합방법은
##### 1. 따로만든다음 하나로 통합, 2. 하나의 테스트 메서드로 한 스텝씩 검증하며 구현

##### Cucumber 툴 참고하자

##### 레거시 리팩터링
##### 레거시 코드를 기반으로 테스트하는 경우가 많음(기존 코드 수정 등등)
##### 인수테스트가 없다면 인수테스트를 먼저 만들고 한다.
##### 세부구현에 의존하지 않는 블랙박스 테스트이므로 깨지지 않음

##### 인수테스트 다음 스텝은 인수테스트를 성공시키기 위한 단위테스트 작성
##### 단위테스트를 성공 시키기 위한 기능구현
##### 단위테스트 성공 후 리팩터링
##### 그리고 반복

##### 기존 코드를 바로 수정하면
##### 변경한 부분을 의존하는 부분은 전부 빨간불이 뜰 것이다.
##### 기존 테스트 코드 모두 실패할 것이다.

##### 테스트 TDD는 비효율적이라고 느낄 수 있다.
##### 기존 기반의 테스트는 삭제하지 않는다. 그리고 새로운 인수테스트를 만든다.
##### 어쩄든 새로운 테스트를 만들며 TDD 한다.
##### 기존 테스트와 혼재는 짧게
##### 기능을 대체 했을 때 완전 교체

##### What is Ubiquitous Language?

##### 인증 방식
##### HTML form 로그인 -> 세션 기반
##### 1. 데이터 요청
##### 2. 서버에서 인증 요구(401)
##### 3. 인증정보 포함 데이터 요청(인증정보 + PW)를 base64로 인코딩하여 authorizaion 헤더에 넣어줌

##### Bearer Auth(토큰 방식)

##### 세션은 서버에서 관리
##### 토큰은 확장성이 뛰어남(클라이언트가 가지고 있음)
##### 그래서 나온 방식이 JWT


##### 미션하며 궁금한점
```java
    public List<String> getShortestPathList(WeightedMultigraph<String, DefaultWeightedEdge> graph,
                                            Long source, Long target) throws NullPointerException {
        return new DijkstraShortestPath(graph).getPath(source.toString(), target.toString()).getVertexList();
    }


    public int getShortestPathLength(WeightedMultigraph<String, DefaultWeightedEdge> graph,
                                     Long source, Long target) throws NullPointerException {
        return (int) new DijkstraShortestPath(graph)
                .getPath(source.toString(), target.toString()).getWeight();
    }

	public void validateSourceTargetSame(Long source, Long target) throws SameSourceTargetException {
        if (source == target) {
            throw new SameSourceTargetException("출발역과 도착역이 같습니다");
        }
    }
```
##### validateSourceTargetSame코드를 서비스단에서 처리하는게 맞는가? 아니면 도메인단에서 처리하는게 맞는가?
##### 도메인단에서 처리하면 같은 메서드가 두개가 들어감
##### 그러지 말고 그래프를 만드는 과정에서 처리하자
```java
    public GraphPath<String, DefaultWeightedEdge> getGraphPath(WeightedMultigraph<String, DefaultWeightedEdge> graph, Long source, Long target){
        validateSourceTargetSame(source, target);
        GraphPath<String, DefaultWeightedEdge> graphPath = new DijkstraShortestPath(graph).getPath(source.toString(), target.toString());
        validateSourceTargetReachable(graphPath);
        return graphPath;
    }
```

##### 궁금증 - 도메인과 서비스단 처리 기준이 무엇인가?

##### 이걸 하면 request에 id와 password만 들어있음
```java
String result = new BufferedReader(new InputStreamReader(request.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
```

--------------------------------------------------------------------------------------------
# 3월 25일 문서 자동화

##### 문서 자동화를 어떻게 atdd에 녹일 것인가?

##### List index는 디비에 커밋된 후 새로 조회할 경우 순서가 보장되지 않음
##### 보장되기 위해서는 별도의 index 컬럼이 필요하다.
##### 아니면 @OrderColumn이 있어야 함

##### 3주차 패키지 의존성
##### 불필요한 의존성을 제거하는게 목적이고
##### 양방향보다는 단방향이 복잡성을 줄일 수 있다.
##### 의존을 줄이면 다른 모듈화가 가능하기 때문에 -> 유연성이 극대화됨 -> 재사용이 가능함
##### 객체의 결합성이 늘어나면 ORM 사용시 복잡도가 늘어남
##### 객체참조를 하지 않으면 Repository를 통한 탐색으로 결합도가 낮아질 수 있음
##### 어떤 상황에 객체 참조를 할 것인가?
##### 함께 생성되고 함께 삭제되는 객체들을 함께 묶어라
##### AuthenticationFilter -> 우리 코드에서는 AuthInterceptor

##### 테스트하기 쉬운 코드
##### 외부 상태를 변경하지 않는 코드
##### 같은 입력에 같은 결과를 반환하는 코드
##### 테스트 하기 쉬운 코드와 어려운 코드를 분리
##### 테스트 하기 어려운 코드는 검증하지 않는다.
##### 전체 기능은 인수테스트로 검증할 수 있다.

##### MockBean은 스프링 컨텍스트에 있는 빈을 사용한다.
##### SpringExtension은 내가 필요한 빈 객체를 지정해야 하는데
##### SpringBootTest는 모든 빈을 사용하므로 장단점이 있음
##### WebMvcTest 스프링의 웹과 관련된 Presentation Layer에 속하는 빈(Controller, Interceptor등)만 사용
##### Presentation에 속하는 빈들만 활용할 수 있음, 보통 api 테스트에 많이 씀
##### 인수테스트는 전체 영역을 다 검증, WebMvc는 컨트롤러단까지만 검증한다.
##### @DataJpaTest -> Repository와 관련된 빈을 Context에 등록하여 테스트 시 활용 가능
##### @MockBean과 @Mock의 차이?

##### 문서 테스트에서 필요한건 @MockBean
##### 문서 자동화 & 테스트
##### 문서화 장점
##### 병렬 작업이 가능 (백엔드, 프론트엔드 개발자간 병력 작업)
##### Swagger와 Spring Rest Docs
##### Spring Rest Docs
##### 테스트 코드 기반 문서라 프로덕션 코드에 영향이 적음
##### Swagger에 비해 작성할 것이 많다.

##### 보통 문서 작성할 때 비지니스 로직이 완성이 안되어있기 때문에
##### 내가 필요한 서비스 로직은 Stubbing 해서 문서를 작성하는 것이 좋다.

-----------------------------------------------

##### 궁금증 - 
##### 왜 세션할 때랑 토큰할 때 저장하는 객체가 다른 것인가?

----------------------------------------------

##### 마이크로서비스 - 배포, 모니터링, 장애처리, 실제 데이터베이스에 저장되었을 때 일관성에 대한 보장, 분산 시스템에 대한 구축이 되어야 한다.
##### MonolithFirst - 마이크로서비스는 유용한 아키텍처이지만 마이크로서비스 프리미엄(마이크로서비스를 위한 부가적인 요소들, 위험 요소들)이 발생하기 때문에 비용이 발생하므로 이것을 능가할 복잡한 시스템에서 유용하다.
##### 따라서 모듈화가 잘된 Monolith를 고려해보자
##### 복잡하지 않은 서비스에서는 Monolith가 나을 수도 있다.

