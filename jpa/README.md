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