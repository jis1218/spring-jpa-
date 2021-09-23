```java
@Entity
public class Post {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;
	
	private String title;
	private String content;
	private String writer;
	
	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<Comment>();
}

@Entity
public class Comment {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;
	
	private String content;
	
	private String writer;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
}
```

##### 이렇게 하고 comment를 insert 했더니 무한루프가 생긴다. comment 안에 post 안에 comment 안에 post 안에.... 계속 이런식으로....
```java
public Comment insertComment(@PathVariable Long id, @RequestBody Comment comment) {
		
	Optional<Post> optPost = postRepository.findById(id);
	if(optPost.isPresent()) {
		comment.setPost(optPost.get());
		Comment savedComment = commentRepository.save(comment);
		return savedComment;
	}else {
		return null;
	}		
}
```
```json
{
    "id": 1,
    "content": "bravo",
    "writer": "George Harrison",
    "post": {
        "id": 1,
        "title": "Yesterday",
        "content": "Yesterday, All my trouble seems so far away",
        "writer": "Paul Mccartney",
        "comments": [
            {
                "id": 1,
                "content": "bravo",
                "writer": "George Harrison",
                "post": {
                    "id": 1,
                    "title": "Yesterday",
                    "content": "Yesterday, All my trouble seems so far away",
                    "writer": "Paul Mccartney",
```

##### 해결방법
https://m.blog.naver.com/PostView.nhn?blogId=rorean&logNo=221593255071&proxyReferer=https:%2F%2Fwww.google.com%2F
http://keenformatics.blogspot.com/2013/08/how-to-solve-json-infinite-recursion.html

##### HandlerInterceptor https://bamdule.tistory.com/149
##### URI 요청, 응답 시점을 가로채서 전/후 처리를 하는 역할
##### Filter와 AOP도 같은 개념

##### Springboot에서 세션 사용하기 (redis에서 사용하기)
##### https://gofnrk.tistory.com/42
##### https://gofnrk.tistory.com/43?category=763542

##### table에 pk가 두개일 때
##### https://www.baeldung.com/jpa-composite-primary-keys

##### 동일한 컬럼을 여러 엔터티에서 사용할 때
##### https://sas-study.tistory.com/361
##### https://ict-nroo.tistory.com/129
##### https://www.baeldung.com/hibernate-inheritance
##### BaseEntity에 인자의 접근을 protected로 하자
##### 그리고 상속받는 자식 클래스에서 생성자에 넣어주자


##### 나는 데이터가 바뀌지 않았을 때 날짜를 그대로 하고 싶다
##### 어떻게 해야하는가?
##### 현재는 Backend에서 명시적으로 update date을 넣어주기 때문에
##### 다른 내용들이 바뀌지 않는다고 해도 update 쿼리가 날라간다.
##### 일단 이걸 참고했다
##### https://www.baeldung.com/spring-data-partial-update
```java
public Disease() {
		this.registerDate = LocalDateTime.now();
		this.updateDate = LocalDateTime.now();
}
```
##### JPA에서 자동으로 하기 위해서는 Auditing이 필요하다.
##### https://velog.io/@conatuseus/2019-12-06-2212-%EC%9E%91%EC%84%B1%EB%90%A8-1sk3u75zo9

##### DTO를 쓰는 이유
##### https://auth0.com/blog/automatically-mapping-dto-to-entity-on-spring-boot-apis/

##### ajax로 post, patch로 데이터 보낼 때 json이 아닌 object로 보내고 싶으면 어떻게 해야하나
##### Request에 각 인자를 담고 싶으면 Setter가 있어야 한다. Setter가 없으면 매핑이 안된다.
##### 그게 아니라면 json으로 보내줘야 한다. 그리고 @RequestBody annotation을 달아야 한다.
```java
public class DiseaseDetailRequest {
	
	private String diseaseCode;
	private String diseaseName;
	private String useYn;
	private String diseaseEngName;
	private String definition;
	private String factor;
	private String symptom;
	private String inspection;
	private String treatment;
}
```
```javascript
 $.ajax({
	url : "http://localhost:8080/disease",
	type : "PATCH",
	data : {
		diseaseCode : $('#diseaseCode').val(),
		diseaseName : $('#diseaseName').val(),
		diseaseEngName : $('#diseaseEngName').val(),
		definition : $('#definition').val(),
		factor : $('#factor').val(),
		symptom : $('#symptom').val(),
		inspection : $('#inspection').val(),
		treatment : $('#treatment').val(),
		useYn : $('input:checkbox[id="useYn"]').prop("checked")?'Y':'N'
	},
	success : function(data, status){
		console.log(data);
		console.log(status);
	}
})
```

##### JPA domain에서 setter 안쓰고 다른 메서드를 이용한 업데이트
##### https://cheese10yun.github.io/spring-jpa-best-06/
##### https://velog.io/@aidenshin/%EB%82%B4%EA%B0%80-%EC%83%9D%EA%B0%81%ED%95%98%EB%8A%94-JPA-%EC%97%94%ED%8B%B0%ED%8B%B0-%EC%9E%91%EC%84%B1-%EC%9B%90%EC%B9%99
##### https://www.inflearn.com/questions/30076
##### 결국 entity 안에 이런 메서드를 만들어주었음
```java
public void updateDisease(String diseaseCode, String diseaseName, String diseaseEngName,
			String definition, String factor, String symptom, String inspection, String treatment, String useYn) {
			this.useYn = useYn;
			this.diseaseCode = diseaseCode;
			this.diseaseName = diseaseName;
			this.diseaseEngName = diseaseEngName;
			this.definition = definition;
			this.factor = factor;
			this.symptom = symptom;
			this.inspection = inspection;
			this.treatment = treatment;
	}
```

##### @Transactional

##### 아래와 같이 코드를 처리했을 경우 페이지가 넘어가며 select 쿼리 실행이 되므로 insert와 update 쿼리가 날라가지 않음, 트랜잭션 락이 발생하는 것 같다고 생각했는데 그게 아니라 페이지가 먼저 넘어가면서 그 다음 메서드가 실행이 안되는 것 같다.
##### 브라우저의 preserve log를 이용하면 전 페이지의 로그가 쌓인 것을 볼 수 있는데 요청이 canceled 된 것을 확인할 수 있다.
```javascript
function afterCodeCheck(isExist){
	console.log(isExist);
	if(isExist){
		updateData(); //update query 날라감
	}else{
		saveData(); //insert query 날라감
	}
	//window.location.href = `http://localhost:8080/view/mod-table.html`
}

function submitClick(){
	
	isCodeExist(afterCodeCheck); 
	//아래 코드를 넣으니 insert 코드가 날라가지 않음
	// select query 날라감
	window.location.href = `http://localhost:8080/view/mod-table.html`
}
```
##### then과 done을 이용한 해결
```js
function submitClick(){
        isCodeExist().then((isExist)=>{
          if(isExist){
            updateData();
          }else{
            saveData();
          }
        }).done(()=>{
          window.location.href = `http://localhost:8080/view/mod-table.html`
        });
      }
```

##### 트랜잭션 격리 수준(isolation level) - 특정 트랜잭션이 다른 트랜잭션에서 변경하거나 조회하는 데이터를 볼 수 있도록 허용할지 말지 결정하는 것(https://nesoy.github.io/articles/2019-05/Database-Transaction-isolation)
##### 격리수준은 4개로 나뉜다
##### READ UNCOMMITTED
##### READ COMMITTED
##### REPEATABLE READ
##### SERIALIZABLE
##### https://joont92.github.io/db/%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EA%B2%A9%EB%A6%AC-%EC%88%98%EC%A4%80-isolation-level/

##### AbstractPersistable 클래스를 보면
```java
@MappedSuperclass
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

	private static final long serialVersionUID = -5554308939380869754L;

	@Id @GeneratedValue private @Nullable PK id;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	@Nullable
	@Override
	public PK getId() {
		return id;
	}

	/**
	 * Sets the id of the entity.
	 *
	 * @param id the id to set
	 */
	protected void setId(@Nullable PK id) {
		this.id = id;
	}

	/**
	 * Must be {@link Transient} in order to ensure that no JPA provider complains because of a missing setter.
	 *
	 * @see org.springframework.data.domain.Persistable#isNew()
	 */
	@Transient // DATAJPA-622
	@Override
	public boolean isNew() {
		return null == getId();
	}
}
```

### 트랜잭션
##### @Test 어노테이션과 @Transactional 어노테이션을 함꼐 사용했을 경우 테스트가 끝나면 rollback 됨

##### 기존의 spring-test와 동일
##### 만약 롤백하고 싶지 않다면 아래와 같이 함
```java
@Test
@Rollback(false)
public void insetTest(){
    // ...
}
```
##### 하지만 webEnvironment의 RANDOM_PORT나 DEFINED_PORT로 테스트를 설정하면 테스트가 별도의 스레드에서 수행되기 때문에 rollback이 수행되지 않음
##### https://joont92.github.io/spring/spring-boot-test/
##### 관련해서 읽어볼만한 글
##### https://stackoverflow.com/questions/46729849/transactions-in-spring-boot-testing-not-rolled-back

##### many to many 관계에서 연관테이블을 놔뒀는데 insert가 안된다.
##### 이유가 뭔지 찾기 위해 일단 JPA 책 8장을 살펴보자
##### 일단 용어 정리
##### 객체 그래프(object graph) : Objects have references to other objects which may in turn have references to more objects including the starting object. This creates a graph of objects, useful in reachability analysis.
##### 객체는 객체 그래프로 연관되어 있는 객체를 탐색할 수 있음. 즉 객체에 레퍼런스 주소를 가지고 있으면 그 레퍼런스로 탐색이 가능하고 또 그 안에 레퍼런스가 있으면 또 가능하고
##### 하지만 모든 객체에 대해서 데이터를 가져올 수는 없다. 그러면 비용이 많이 들것이다.
##### 따라서 JPA 구현체들은 이 문제를 해결하기 위해 프록시 기술을 사용하였다.
##### 프록시를 사용하면 연관된 객체를 처음부터 데이터베이스에서 조회하는 것이 아니라 실제 사용하는 시점에 데이터베이스에서 조회할 수 있음. 그래서 JPA는 즉시 로딩(eager)과 지연 로딩(lazy)을 지원한다.

##### 그러면 프록시란 무엇인가?
##### 일단 하이버네이트는 지연 로딩을 위해 프록시를 사용하는 방법과 바이트코드를 수정하는 두 가지 방법을 제공함
##### 프록시 클래스는 실제 클래스를 상속(또는 인터페이스를 구현) 받아 만들어짐
##### 프록시 객체는 실제 객체에 대한 참조를 보관. 그리고 프록시 객체의 메소드를 호출하면 프록시 객체는 실제 객체의 메서드를 호출
##### 프록시 객체는 member.getName()처럼 실제 사용될 때 데이터베이스를 조회해서 실제 엔티티 객체를 생성하는데 이것을 프록시 객체의 초기화라고 한다.

```java
@Entity
public class Memeber {
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TEAM_ID")
	private Team team;

}
```

```java
Member member = em.find(Member.class, "member1");
Team team = member.getTeam(); //객체 그래프 탐색
```
##### FetchType.EAGER로 설정했으므로 team에 대한 탐색이 즉시 이루어진다.
##### 쿼리는 두번 보내지 않고 join을 이용하여 한번만 날린다.
##### Null 조건과 JPA 조인 전략
##### 외래키가 Nullable이면 JPA는 left outer join을 사용
##### 외래기카 Nullable = false 이면 inner join을 사용
##### inner join이 성능과 최적화에서 더 유리하다.

##### 근데 지연로딩을 하면 당연히 쿼리는 두번 나가게 된다.(마지막 한번은 필요할 때)
##### 그리고 지연로딩을 하면 실제 엔티티 대신에 프록시로 조회하게 된다.

##### 언제 Eager를 쓰고 Lazy를 쓸지는 잘 판단해야 하겠따.

##### 하이버네이트는 엔티티를 영속 상태로 만들 때 엔티티에 컬렉션이 있으면 컬렉션을 추적하고 관리할 목적으로 원본 컬렉션을 하이버네이트가 제공하는 내장 컬렉션으로 변경하는데 이것을 컬렉션 래퍼라고 한다.
##### 지연로딩을 할 때 컬렉션 래퍼가 지연로딩을 처리해줌, 프록시와 하는 방식은 같으므로 컬랙션 래퍼도 프록시라 할 수 있음
```java
member.getOrders()
```
##### 이것만 호출해서는 데이터베이스를 탐색하지 않는다.
```java
member.getOrders().get(0)
```
##### 이렇게 해야 한다.


##### JPA 기본 페치(fetch) 전략
##### @ManyToOne, @OneToOne : 즉시 로딩(FetchType.EAGER)
##### @OneToMany, @ManyToMany : 지연 로딩(FetchType.LAZY)

##### 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶으면 영속성 전이(transitive persistence)를 이용한다.
##### 즉 영속성 전이를 사용하면 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장할 수 있음
##### CascadeType.PERSIST로 설정하면 연관된 자식도 함께 persist 됨
```java
@Entity
public class Parent{
	@OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
	private List<Child> children = new ArrayList<Child>();
}

Child child1 = new Child();

Parent parent = new Parent();
child1.setParent(parent); //Child table에 insert 됨
```

##### JPA는 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제하는 기능을 제공하는데 이것을 고아 객체(ORPHAN)제거라 한다. 이 기능을 사용해서 부모 엔티티의 컬렉션에서 자식 엔티티의 참조만 제거하면 자식 엔티티가 자동으로 삭제된다.
##### 즉 부모 엔티티를 삭제하게 되면 자식 엔터티에서는 FK를 가지고 있지만 참조할 수가 없다. 이를 orphan이라고 하는데 orphanRemoval = true로 설정하면 연관관계가 끊어졌을 때(부모를 제거했을 때) 자식도 같이 삭제가 

##### 해답은 @Service에 @Transactional을 붙여야 한다는것!!!!!!!!!!!!!!!!!!!!!!!!

##### 1:N 관계에서 부모를 삭제하게 되면 자식에서 참조할 id가 없음(설정을 orphanRemove=true로 했을 경우)
##### 그러면 자식 테이블에서 데이터 먼저 삭제 되고(id로 조회 함)
##### 그 다음 부모 테이블 삭제 됨


##### @Transactional에 관하여...
##### https://mommoo.tistory.com/92

##### Dynamic Proxy와 CGlib에 관하여
##### https://yeti.tistory.com/225

##### Java Reflection
##### 객체를 통해 클래스의 정보를 분석해 내는 프로그램 기법


##### 사용자 로그인 -> jwt 발급(TokenAuthenticationInterceptor)
##### 사용자 즐겨찾기 -> 


##### 10 tips for choosing between a surrogate and natural primary key
##### https://www.techrepublic.com/blog/10-things/10-tips-for-choosing-between-a-surrogate-and-natural-primary-key/
##### https://www.inflearn.com/questions/27694

##### 현재 상황... 부모 클래스인 Keyword가 있고 자식 클래스인 MainKeyword, PartKeyword, SubKeyword가 있는 상황
##### 한 Repository에서 이걸 다 처리할 수 있는지 확인하였으나 불가능한 것 같다. Save는 가능하다.
##### 하지만 Select 해서 불러오는 것은 안된다.
##### 스프링 Reference에 보니 각각의 Repo를 만들어서 해야 하는데 아래 링크를 참고하자
##### https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query.spel-expressions

##### jpql이란? rorcpwlgidznjfl... 테이블이 아닌 객체를 검색하는 객체지향 쿼리, SQL을 추상화하였음
##### https://victorydntmd.tistory.com/205

##### @Entity와 @Table의 차이...
##### https://walkingtechie.blogspot.com/2019/06/difference-between-entity-and-table.html

##### 우아한 형제들의 Querydsl 사용법
##### https://velog.io/@youngerjesus/%EC%9A%B0%EC%95%84%ED%95%9C-%ED%98%95%EC%A0%9C%EB%93%A4%EC%9D%98-Querydsl-%ED%99%9C%EC%9A%A9%EB%B2%95
