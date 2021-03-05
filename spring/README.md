##### Eclipse에 Spring 환경을 구축 한 후 서버를 돌려보았다.
##### IE를 통해 서버를 호출하면 404 Error가 뜨는데
##### 그 이유는 path값이 적절하지 않았기 때문이다.
##### localhost:8080/{path} 가 오는데 path는 server.xml에서 Context path 값을 넣어야 한다.

```java
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate );

		return "home";
	}

```

##### return의 "home"은 수행결과의 응답을 어디로 보낼지를 명시해준다. "home"이라는 것은 jsp 파일명을 의미한다.
##### 서블릿 설정에서 자동으로 앞에 "/WEB-INF/views"를 붙여주고 (prefix), 뒤에 ".jsp"를 붙여주도록 되어있다.(suffix)
##### 따라서 src/main/webapp/WEB_INF/views/home.jsp가 호출되는 것이다.

##### home.jsp는 다음과 같이 작성되어있다.
```jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>


    <title>Home</title>


<h1>
    Hello world!  
</h1>

<p>  The time on the server is ${serverTime}. </p>
<br>
```

##### model에서 model.addAttribute("serverTime", formattedDate) 라는 방식으로 화면으로 결과값을 보내준다.
##### 그리고 jsp에서 ${serverTime}이라는 방식으로 사용된다.

##### web.xml은 서블릿 배포 기술자이고 영어로 DD (Deployment(배치) Descriptor)라고 한다. web.xml은 WAS(Web Application Server)(Tomcat)이 최초 구동될 때 WEB_INF 디렉토리에 존재하는 webxml을 읽고, 그에 해당하는 웹 애플리케이션 설정을 구성한다. 즉 각종 설정을 위한 설정파일이라고 이야기 할 수 있다.

##### servlet-context.xml은 서블릿 관련 설정인데 여기서 주목할 부분은
```xml
<beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
	<beans:property name="prefix" value="/WEB-INF/views/" />
	<beans:property name="suffix" value=".jsp" />
</beans:bean>
```
##### Controller를 설명할 때 서블릿 설정이 자동으로 prefix와 suffix를 붙여주는 역할을 담당한다.
```xml
<context:component-scan base-package="com.company.first" />
```
##### 이 부분은 스프링에서 사용하는 bean을 일일이 xml에 선언하지 않고도 필요한 것들을 annotation을 통해 자동으로 인식하게 한다.

##### 서블릿이란 자바에서 동적 웹 프로젝트를 개발할 때, 사용자의 요청과 응답을 처리해 주는 역할을 한다.

##### 인터셉터
#####인터셉터는 위 이미지의 빨간색 박스 부분에서 동작한다. 인터셉터의 정확한 명칭은 핸들러 인터셉터 (Handler Interceptor)이다. 인터셉터는 DispatcherServlet이 컨트롤러를 호출하기 전,후에 요청과 응답을 가로채서 가공할 수 있도록 해준다.
##### 예를 들어, 로그인 기능을 구현한다고 했을때, 어떠한 페이지를 접속하려고 할때, 로그인된 사용자만 보여주고, 로그인이 되어있지 않다면 메인화면으로 이동시키려고 해보자. 기존에는 로그인 체크 로직을 만들어서 각 화면마다 일일이 Ctrl + C,V로 만들기도 했다. 아마 누가 그렇게 하겠어~? 라고 생각할 수도 있겠지만 실제로 필자는 학생때 웹 프로젝트를 할때 그렇게 만들었었다.
##### 스프링에서는 인터셉터를 사용하여 위의 기능을 간단히 만들 수 있다.
##### 인터셉터에서 어떠한 요청이 들어올 때, 그 사람의 로그인 여부를 판단해서 로그인이 되어있으면 요청한 페이지로 이동시키고, 로그인이 되어있지 않을경우 바로 메인 페이지로 이동시키면 끝이다.
##### 즉, 단 하나의 인터셉터로 프로젝트내의 모든 요청에서 로그인여부를 관리할 수 있는것이다.
```java
@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		if(log.isDebugEnabled()) {
			log.debug("================== Start ==================");
			log.debug("Request URI \t: " + request.getRequestURI());
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		if(log.isDebugEnabled()) {
			log.debug("========================= END ====================");
		}
	}
```

##### preHandler()는 컨트롤러가 호출되기 전에 실행, postHandle()은 컨트롤러가 실행되고 난 후에 호출된다.


출처: http://addio3305.tistory.com/43?category=772645 [흔한 개발자의 개발 노트]


##### RequestMapping 사용법

```java
private static final String WORD_DISPLAY = "/display";
	private static final String DEFAULT_URL = "/views/display/";

	/**
	 * 페이지 이동
	 * @param pageUrl 페이지 URL
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	@RequestMapping(value =WORD_DISPLAY+"/{html}", method = RequestMethod.GET) //인터넷 창에서 value를 치고 들어오면 함수에서 path를 return 해준다.
	public String go(@PathVariable("html") String pageUrl ,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String path = DEFAULT_URL + pageUrl;
		return path;
	}
```

##### return 한 path 값이 결국 서버내의 경로를 지정한다. https://hellogk.tistory.com/85 확인

## 서버 공부

##### 웹 프레임워크 or 웹 어플리케이션 프레임워크 - 웹 페이지를 개발하는 과정에서 겪는 어려움을 줄이는 것이 주 목적
##### Java - Spring
##### Python - Django
##### Node.js - Express
##### PHP - Laravel

### 웹 서버란?
##### https://developer.mozilla.org/ko/docs/Learn/Common_questions/What_is_a_web_server
##### https://gmlwjd9405.github.io/2018/10/27/webserver-vs-was.html
##### https://jeong-pro.tistory.com/84
##### 하드웨어, 소프트웨어 두개 모두를 의미함
##### 하드웨어 측면에서는 website의 컴포넌트 파일(HTML, image 파일, css, javascript 파일 등등)을 저장하는 물리적인 컴퓨터
##### 소프트웨어 측면에서는 클라이언트가 어떻게 호스트 파일에 접근하는지 관리(HTTTP 서버)
##### 웹 어플리케이션 서버와 데이터베이스로 구성되어 있음
##### Web Server는 파일 경로 이름을 받아 경로와 일치하는 file contents를 반환
##### 항상 동일한 페이지를 반환

### 웹 어플리케이션 서버(WAS)
##### 동적인 contents를 반환
##### 웹 서버에 의해서 실행되는 프로그램을 통해서 만들어진 결과물을 반환

##### Python을 사용한 웹서비스 개요
##### https://datascienceschool.net/view-notebook/f9b09998601441f4a7026e62353cf751/

##### 로드 밸런싱, 라운드 로빈에 대한 설명
##### http://ankyu.entersoft.kr/lecture/window/01_setting05.asp

##### Node.js의 동작 원리
##### https://yonghyunlee.gitlab.io/node/nodejs-structure/

##### Django
##### https://ldgeao99.tistory.com/201?category=864433
##### 웹개발을 하는데 사용할 수 있는 수십 개의 추가 기능이 미리 포함되어 있음(User Authentication, Content Administration, Site Maps, RSS feeds 등)
##### 보안적인 실수가 없게 해줌

##### Node.js
##### https://woowabros.github.io/woowabros/2017/09/12/realtime-service.html
##### Node.js는 디폴트 기능이 하나도 없어서 처음부터 다 만들어야 함
##### 리얼타임 서비스(웹소켓으로 통신하는 서버를 이용하여 웹페이지 및 모바일 앱에서 실시간으로 데이터를 주고 받고 갱신하는 시스템)가 주를 이루는 경우에 사용한다.
##### Single Thread 기반의 Non-Blocking IO

##### 아파치를 이용해 서버 개발할 경우
##### https://m.blog.naver.com/tmondev/220731906490
##### 아파치는 3가지 Multi-Processing Module이 있는데
##### prefork, worker, event
##### prefork는 1개의 메인 프로세스에 최대 1024개의 자식 프로세스를 생성하여 요청을 처리
##### 1개의 프로세스에 1개의 스레드만 있어 안정적이나 메모리를 많이 사용
##### worker는 프로세스당 여러개의 스레드를 연결

##### http://www.opennaru.com/jboss/apache-prefork-vs-worker/
##### Prefork MPM과 Worker MPM을 비교하는 그림에서 Worker MPM은 Process#1 안에 스레드가 여러개 있는 것이 보이는데
##### 이는 Process #1이 다량의 request에 대해 메모리를 공유하는 것으로 보인다. 그래서 Prefork보다 메모리 사용이 더 적다고 하는 것 같다.

##### 메모리 사용량이 적어 동시접속이 많은 경우 사용하지만 하나의 스레드라도 비정상적으로 동작할 경우 서비스 전체가 막히게 됨
##### event 방식의 대표격이 nginx이다.
##### 메모리를 훨씬 적게 사용하면서 동시접속에 효율적임


##### 스레드 처리에 대한 이야기들
##### http://www.gamecodi.com/board/zboard.php?id=GAMECODI_Talkdev&no=2099

##### 프록시(Proxy)란? https://brownbears.tistory.com/191
##### 프록시란 '대리'라는 의미로 보안 분야에서는 주로 보안상의 이유로 직접 통신할 수 없는 두 점 사이에서 통신을 할 경우 그 상이에 있어서 중계기로서 대리로 통신을 수행하는 기능을 가리켜 '프록시', 그 중계 기능을 하는 것을 '프록시 서버'라고 한다. 프록시 서버는 프록시 서버에 요청된 내용들을 캐시를 이용하여 저장한다. 이렇게 하면 캐시 안에 있는 정보를 요구하는 요청에 대해서는 원격 서버에 접속하여 데이터를 가져올 필요가 없게 됨으로써 전송 시간을 절약할 수 있고 동시에 불필요하게 외부와의 연결을 하지 않아도 된다. 또한 외부와의 트래픽을 줄이게 됨으로써 네트워크 병목 현상을 방지하는 효과도 누릴 수 잇다.

##### Jquery keyUp, keyDown Event에 대한 설명
https://circus7.tistory.com/6
##### IME는 한글을 입력 시 컴포징이라는 단계를 거칩니다. 예를 들어, "ㄱ"을 입력하면 아직 글자가 완성되지 않았다고 판단하고 글자가 완성될 때까지 조합을 하는데 이것을 컴포지션이라고 부릅니다.
##### 그런데 문제는 컴포징이 진행 중일 때에도 keydown 및 keyup 이벤트를 보낸다는 것입니다. 컴포징이 시작되었을 때 미완성이라는 메시지로 keyCode로 229를 보내는 것입니다. 다음은 textarea에서 한글"ㅁ"을 입력했을 때 keydown과 keyup의 event를 console.log에 찍은 내용입니다.

##### 입력시 'input' handler took 6985ms 와 같은 에러가 뜨며 창이 어는 현상
##### https://www.it-swarm.dev/ko/javascript/%EC%9C%84%EB%B0%98-javascript-%EC%8B%A4%ED%96%89-%EC%8B%9C%EA%B0%84%EC%9D%B4-%EC%98%A4%EB%9E%98-%EA%B1%B8%EB%A0%B8%EC%8A%B5%EB%8B%88%EB%8B%A4-xx-ms/828944611/

##### 웹폰트 처리 방법
##### https://web-atelier.tistory.com/4

##### Mybatis에서 insert 또는 update 시 map과 다른 파라미터 처리 방법
##### 처음에 다음과 같이 했더니 에러가 난다.
##### 그 이유는 _parameter는 mapper.java의 메소드의 파라미터 전부를 불러오는 키워드이기 때문이다.
```xml
<update id="updateDataToDB" parameterType="java.util.Map">
	UPDATE #{tb_name} SET
	<foreach collection="_parameter_.entrySet()" item="value" index="key" separator=",">
		${key} = #{value}    	
	</foreach>
	WHERE ykiho=#{ykiho, jdbcType=VARCHAR}
</update>
```
##### 그래서 .java에서 @Param으로 명시를 해줘야 한다.
```java
	void updateDataToDB(@Param("map")Map<String, String> param, @Param("ykiho")String ykiho);
```
```xml
<update id="updateDataToDB" parameterType="java.util.Map">
	UPDATE #{tb_name} SET
	<foreach collection="map.entrySet()" item="value" index="key" separator=",">
		${key} = #{value}    	
	</foreach>
	WHERE ykiho=#{ykiho, jdbcType=VARCHAR}
</update>
```

### CentOS6에 톰캣 서버 구축 https://zetawiki.com/wiki/CentOS6_%ED%86%B0%EC%BA%A36_%EC%84%A4%EC%B9%98

#### 자바 설치
##### sftp 연결하여 자바 설치 
##### jdk-7u80-linux-x64.tar.gz 압축 푼다
```
$ tar xvfz jdk-7u80-linux-x64.tar.gz
```

##### 환경설정 한다.
```
$ vi ~/.bash_profile
```

##### 자바 정상 동작하는지 확인
```
$ java -version
```

#### 톰캣 설치
##### apache-tomcat-6.0.53.zip 파일을 업로드하고 압축을 해제
##### 환경변수 및 alias 설정
```
$ vi .bash_profile
```

```
CATALINA_HOME=/mnt/home/mediview/apache-tomcat-6.0.53
export CATALINA_HOME

JAVA_HOME=/mnt/home/mediview/pkg/jdk1.7.0_80
export JAVA_HOME
PATH=$JAVA_HOME/bin:$PATH:$HOME/bin
export PATH

alias tstart=$CATALINA_HOME/bin/startup.sh
alias tstop=$CATALINA_HOME/bin/shutdown.sh
alias tlog='tail -f $CATALINA_HOME/logs/catalina.out'

SPRING_PROFILES_ACTIVE=prod
export SPRING_PROFILES_ACTIVE
```

##### 톰캣 환경 설정
```
$ /home/사용자계정/apache-tomcat-6.0.53/conf
$ vi server.xml
```
```xml
<Connector port="8080" protocol="HTTP/1.1"
			connectionTimeout="20000"
			redirectPort="8443"
			URIEncoding="UTF-8">
```

##### 주의할 점 - 80 포트를 해주고 싶은데 안되는 이유...
##### 리눅스에서는 1024 포트 아래는 root 계정에서 다룰 수 있으므로 80을 쓸려면 redirect 해줘야 한다.
##### Spring profiles 를 설정 (위에 .bash_profile 참고)

##### 방화벽 설정 https://blog.miyam.net/7

##### vi /etc/sysconfig/iptables 하여 다음 줄 추가
```
-A INPUT -m state --state NEW -m tcp -p tcp --dport 8080 -j ACCEPT
```

##### 같은 ip 서버에 톰캣 2개 올리기

##### 폴더를 복사해서 이름을 바꿔준다.
##### 방화벽 풀어주고, server.xml 포트 수정해주고
##### bin 폴더에 catalina.sh를 손봐야 한다. https://goddaehee.tistory.com/76 환경변수 수정
```
PRG="$0"

while [ -h "$PRG" ]; do

  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

export CATALINA_HOME=/usr/local/tomcat8.5
export TOMCAT_HOME=/usr/local/tomcat8.5
export CATALINA_BASE=/usr/local/tomcat8.5
```

##### 다음과 같은 에러가 떴다
```
[hira@bplustest apache-tomcat-6.0.53_test]$ t_tstart
Using CATALINA_BASE:   /home/hira/pkg/apache-tomcat-6.0.53_test
Using CATALINA_HOME:   /home/hira/pkg/apache-tomcat-6.0.53_test
Using CATALINA_TMPDIR: /home/hira/pkg/apache-tomcat-6.0.53_test/temp
Using JRE_HOME:        /home/hira/pkg/jdk1.7.0_80
Using CLASSPATH:       /home/hira/pkg/apache-tomcat-6.0.53_test/bin/bootstrap.jar
touch: cannot touch `/home/hira/pkg/apache-tomcat-6.0.53_test/logs/catalina.out': 허가 거부
[hira@bplustest apache-tomcat-6.0.53_test]$ /home/hira/pkg/apache-tomcat-6.0.53_test/bin/catalina.sh: line 406: /home/hira/pkg/apache-tomcat-6.0.53_test/logs/catalina.out: 허가 거부
```
##### 일반계정에 권한을 줘야한다. $chown 계정명:그룹명 /usr/local/apache-tomcat-6.0.18 -R 

##### JPA란 무엇인가?
##### https://velog.io/@adam2/JPA%EB%8A%94-%EB%8F%84%EB%8D%B0%EC%B2%B4-%EB%AD%98%EA%B9%8C-orm-%EC%98%81%EC%86%8D%EC%84%B1-hibernate-spring-data-jpa

##### DB 롤백
##### https://m.blog.naver.com/PostView.nhn?blogId=minki0127&logNo=220783883574&proxyReferer=http:%2F%2F203.229.225.135%2Ftm%2F%3Fa%3DCR%26b%3DWIN%26c%3D300024535648%26d%3D32%26e%3D5303%26f%3DbS5ibG9nLm5hdmVyLmNvbS9taW5raTAxMjcvMjIwNzgzODgzNTc0%26g%3D1599188304679%26h%3D1599188304267%26y%3D0%26z%3D0%26x%3D1%26w%3D2020-04-08%26in%3D5303_1158_00012509%26id%3D20200904

##### 트랜잭션
##### https://goddaehee.tistory.com/162


# 스프링부트

##### Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

##### application.properties에 다음과 같이 하면 됨
```
spring.datasource.url=jdbc:log4jdbc:mysql://14.63.197.117:11001/dev_new_areyousick_algorithm?autoReconnect=true&useSSL=false&serverTimezone=Asia/Seoul
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=net.sf.log4jdbc.sql.jdbcapi.DriverSpy
```

##### 이게 있어야 로컬 실행시 앱이 안죽고 서버가 뜨는구만
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

##### Springboot에서 paging시 pageable을 파라미터로 받는데
##### pageable의 구현체는 AbstractPageRequest라는 것이고
```java
AbstractPageRequest implements Pageable, Serializable {
    ...
    private final int page;
    private final int size;

    public long getOffset() {
        return (long)this.page * (long)this.size;
    }
    ...
```
##### 이렇게 생겼다.
##### 따라서 파라미터에 page와 size를 넘겨야 한다.
##### https://stackoverflow.com/questions/57956154/spring-boot-getmapping-with-pageable-as-request-parameter-dont-work-as-expect

##### nginx 설정
##### https://velog.io/@damiano1027/Nginx-Nginx%EC%99%80-SpringBoot-%EB%82%B4%EC%9E%A5-Tomcat-%EC%97%B0%EB%8F%99
##### https://milhouse93.tistory.com/70

##### 일단 윈도우에서는
```conf

	#리버스 프록시 방법으로 localhost:8080으로 매핑됨
	location / {            
		proxy_pass http://localhost:8080;
		proxy_set_header X-Real0IP $remote_addr;
		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		proxy_set_header Host $http_host;
	}

	location /src {
		root    template; #이렇게 하면 template 폴더 안에 src 폴더 안에서 찾게 됨 (리눅스는 다른 것 같음)
		index   index.html index.html; #아무것도 안넣으면 index.html로 매핑
	}
```
##### https://stackoverflow.com/questions/1011101/nginx-location-directive-doesnt-seem-to-be-working-am-i-missing-something

##### DTO 사용

##### com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class kr.areyousick.dto.DiseaseResponse and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: org.springframework.data.domain.PageImpl["content"]->java.util.Collections$UnmodifiableRandomAccessList[0])
##### 이럴경우 getter를 만들어줘야함. 그 이유는 field가 private으로 되어 있어서 json으로 변환 시 접근하지 못함
##### https://steady-hello.tistory.com/90

##### Entity를 DTO 클래스를 이용하여 필요한 Response만 응답하는 방법 Page<Entity> -> Page<DTO>
```java
@Getter
public class DiseaseResponse {
	
	private String diseaseCode;
	private String diseaseName;
	private LocalDateTime registerDate;
	private LocalDateTime updateDate;
	
	public DiseaseResponse(String diseaseCode, String diseaseName, LocalDateTime registerDate, LocalDateTime updateDate){
		this.diseaseCode = diseaseCode;
		this.diseaseName = diseaseName;
		this.registerDate = registerDate;
		this.updateDate = updateDate;
	}
	
	public static DiseaseResponse of(Disease disease) {
		return new DiseaseResponse(disease.getDiseaseCode(),
				disease.getDiseaseName(),
				disease.getRegisterDate(),
				disease.getUpdateDate());
	}

	public static Page<DiseaseResponse> changeToResponse(Page<Disease> pageDisease) {
		return pageDisease.map(DiseaseResponse::of);
	}
}

@RestController
public class DiseaseController {
	
	@Autowired
	DiseaseRepository repository;
	
	@GetMapping("/disease")
	public Page<DiseaseResponse> findAll(Pageable pageable) {
		pageable.getSort().stream().forEach(System.out::println);
		return DiseaseResponse.changeToResponse(repository.findAll(pageable));
	}
	
	@GetMapping("/disease/{diseaseId}")
	public Disease findOne(@PathVariable("diseaseId") String diseaseId) {
		Optional<Disease> optDisease = repository.findById(diseaseId);
		return optDisease.orElse(new Disease());
	}
	
	@PatchMapping("/disease")
	public Disease update(Disease disease) {
		repository.save(disease);
		return disease;		
	}

}
```

##### test 코드 작성
##### rest api testing의 기초
##### https://www.baeldung.com/integration-testing-a-rest-api
```java
@Test
public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()
  throws ClientProtocolException, IOException {
 
    // Given
    String name = RandomStringUtils.randomAlphabetic( 8 );
    HttpUriRequest request = new HttpGet( "https://api.github.com/users/" + name );

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

    // Then
    assertThat(
      httpResponse.getStatusLine().getStatusCode(),
      equalTo(HttpStatus.SC_NOT_FOUND));
}
```

##### 스프링 부트 테스트
##### https://kplog.tistory.com/250
##### MockMvc란?
##### 실제 객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜 객체를 만들어서 애플리케이션 서버에 배포하지 않고도 스프링 MVC 동작을 재현할 수 있는 클래스 https://shinsunyoung.tistory.com/52

##### @Runwith annotation에 대해
##### https://4whomtbts.tistory.com/

##### @BeforeEach
##### Test 할 때마다 실행됨

##### Test 끝날때마다 롤백이 된다. 결국 각각의 테스트는 독립적

##### Test 시 insert하고 추가적인 업데이트 쿼리가 날라감
```java
	@DisplayName("질환 리스트 테스트")
	@Test
	void findDisease() {
		log.info("==========================findDisease start=================================");

		diseaseService.save(diseaseDetailRequest);
		diseaseService.save(new DiseaseDetailRequest("bb", "고고", "Y", "gggh", "하하", "호호", "히히", "후후", "캬캬"));
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<DiseaseTableResponse> disease = diseaseService.findAllDisease(pageable);
		
		assertThat(disease).hasSize(6);
		assertThat(disease).extracting("diseaseCode").contains("a", "bb");
		log.info("==========================findDisease end=================================");

	}
```

##### Timezone 때문인가??
##### https://stackoverflow.com/questions/50194996/lastmodifieddate-causes-additional-update-in-database/50204962#50204962

##### @Transactional을 테스트에 사용하였더니 insert나 update 쿼리가 안날라간다.
##### rollback이 되면 쿼리가 안날라가는듯
##### 테스트할 때에는 DB에 접근할 일이 있을 때에만 insert 또는 update 쿼리가 날라가네
##### https://dev.to/henrykeys/don-t-use-transactional-in-tests-40eb
```java
	public DiseaseDetailResponse save(DiseaseDetailRequest diseaseDetailRequest) {
		Disease savedDisease = repository.save(diseaseDetailRequest.toDisease());

		//여기서 이게 있어야 쿼리가 날라간다. 왜냐면 전체 데이터를 가지고 오기 위해 db를 뒤져야 하므로
		//Page<DiseaseTableResponse> disease = diseaseService.findAllDisease(pageable);	

		return DiseaseDetailResponse.of(savedDisease);
	}
	
	public DiseaseDetailResponse update(DiseaseDetailRequest diseaseDetailRequest) {
		Disease disease = new Disease();
		disease.updateDisease(
				diseaseDetailRequest.getDiseaseCode(), 
				diseaseDetailRequest.getDiseaseName(), 
				diseaseDetailRequest.getDiseaseEngName(), 
				diseaseDetailRequest.getDefinition(), 
				diseaseDetailRequest.getFactor(), 
				diseaseDetailRequest.getSymptom(), 
				diseaseDetailRequest.getInspection(), 
				diseaseDetailRequest.getTreatment(), 
				diseaseDetailRequest.getUseYn()
				);
		return DiseaseDetailResponse.of(disease);
	}
```
```java
	@DisplayName("질환 저장 테스트")
	@Test
	void saveDisease() {
		log.info("==========================saveDisease start=================================");

		DiseaseDetailResponse disease = diseaseService.save(diseaseDetailRequest);
		
		diseaseService.findByDiseaseId(disease.getDiseaseCode());
		
		assertThat(disease.getDiseaseCode()).isNotNull();
		
		
		
		log.info("==========================saveDisease end=================================");

	}
	
	@DisplayName("질환 업데이트 테스트")
	@Test
	void updateDisease() {
		log.info("==========================updateDisease start=================================");
		diseaseDetailRequest = new DiseaseDetailRequest("a", "멀미", "N", "cough", "가나다라", "마바사아ㅏ", "히히", "후후", "캬캬");

		DiseaseDetailResponse disease = diseaseService.update(diseaseDetailRequest);
		
		assertThat(disease.getSymptom()).isEqualTo("멀미");
		log.info("==========================updateDisease end=================================");

	}
```

##### Spring에서는 IoC 컨테이너(빈 컨테이너)를 이용해 빈 객체를 관리한다. ApplicationContext 인터페이스의 구현체를 통해 빈을 자동 생성함
##### Spring Boot 전에는 다음과 같이 ApplicationContext 인터페이스의 구현체인 context 클래스에서 빈을 가져왔음
##### 아래 예시는 xml에 빈을 등록 후 ClassPathXmlApplicationContext를 통해 빈을 가져오는 방법임
```java
ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("exam/test21/beans.xml");
Car car = (Car) ctx.getBean("car");
```
##### Spring Boot의 경우에는 @Configuration이 붙은 곳에서 @Bean을 통해 빈을 생성할 수 있음, 정확히 말하면 Spring 3.0 이상에서임
```java
@Configuration
public class AccountConfig {

  @Bean
  public AccountService accountService() {
    return new AccountService(accountRepository());
  }
```

##### Baeldung에 다음과 같은 글이 있다. https://www.baeldung.com/spring-application-context
##### Furthermore, it provides more enterprise-specific functionalities. The important features of ApplicationContext are resolving messages, supporting internationalization, publishing events, and application-layer specific contexts. This is why we use it as the default Spring container.

##### So, should we configure all the objects of our application as Spring beans? Well, as a best practice, we should not. As per Spring documentation, in general, we should define beans for service layer objects, data access objects (DAOs), presentation objects, infrastructure objects such as Hibernate SessionFactories, JMS Queues, and so forth.

##### @SpringBootApplication에 대해서 알아보자 https://jhkang-tech.tistory.com/44

##### 예를 들어 다음과 같은 클래스가 있다고 하자
```java

@Data
public class SequenceGenerator{
	private string prefix;
	private int initial;

	public String getSequence(0{
		StringBuilder builder = new StringBuilder();
		builder.append(prefix).append(initial);
		return builder.toString();
	})
}
```

##### 어떤 객체를 bean으로 등록하기 위해서는 @Configuration을 붙인 클래스를 만들어주고 그 안에 @Bean을 붙어 등록한다.

```java
@Configuration
public class SequenceGeneratorConfiguration{

	@Bean
	public SequenceGenerator sequenceGenerator(){
		SequenceGenerator seqgen = new SequenceGenerator();
		seqgen.setPrefix("30");
		seqgen.setInital("100");
		return seqgen;
	}
}
```
##### 그럼 어떻게 컨테이너를 초기화하고 Annotation을 초기화하느냐? Annotation을 붙인 자바 클래스를 스캐닝하려면 우선 IoC컨테이너를 인스턴스화 해야 한다.
##### @Configuration, @Component를 찾을 수 있는 ApplicationContext의 구현체인 AnnotationConfigApplicationContext를 인스턴스화 한다.
```java
ApplicationContext context = new AnnotationConfigApplicationContext(SequenceGeneratorConfiguration.class);

SequeunceGenerator generator = (SequenceGenerator) context.getBean("SequenceGenerator");
//or
// SequeunceGenerator generator = context.getBean("SequenceGenerator", SequenceGenerator.class);

System.out.println(generator.getSequence()); // 30100
```

##### 좋은 사이트인듯?
https://atoz-develop.tistory.com/entry/Spring-Autowired-%EB%8F%99%EC%9E%91-%EC%9B%90%EB%A6%AC-BeanPostProcessor?category=869243


##### AOP(Aspect Oriented Programming) 관점 지향 프로그래밍 https://engkimbs.tistory.com/746
##### Spring AOP는 1. JDK 동적 프록시 2. CGLIB을 활용하여 AOP 제공
##### https://gmoon92.github.io/spring/aop/2019/01/15/aspect-oriented-programming-concept.html
##### Spring AOP는 메서드 단위
##### Filter, Interceptor, AOP 차이 https://goddaehee.tistory.com/154

##### Spring Introduction
##### https://stackoverflow.com/questions/60221207/how-does-spring-declareparents-annotation-work-how-does-it-implement-the-metho

##### Thread
##### ExecutorService는 JDK API
##### Executors라는 팩토리 클래스를 이용하여 Class 생성

##### ExecutorService.submit을 하게 되면 result트로 Future Interface형의 결과를 반환
##### Callable일 경우 result를 반환하고 Runnable일 경우 null 반환
##### https://www.baeldung.com/java-executor-service-tutorial

##### CachedThreadPool
##### If there is an idle thread waiting on the queue, then the task producer hands off the task to that thread. Otherwise, since the queue is always full, the executor creates a new thread to handle that task.        

##### FixedThreadPool
##### Therefore, instead of an ever-increasing number of threads, the fixed thread pool tries to execute incoming tasks with a fixed amount of threads.

##### SingleThreadExecutor
##### 단일 쓰레드로 동작하는 Executor

##### ScheduledThreadPool
##### 일정 시간 이후에 실행하거나 주기적으로 작업을 실행

##### proxy pattern을 사용할 때 좋은 점?
##### 1. 객체 생성 시 많은 자원이 들 때 - 즉 필요할 때만 initialization하면 되므로 성능이 좋아짐
##### 2. Proxy를 캐쉬 개념으로 사용할 수 있다.

##### proxy를 사용하는 spring의 예들 : Transactions, Caching, Java Configuration
##### https://spring.io/blog/2012/05/23/transactions-caching-and-aop-understanding-proxy-usage-in-spring
##### https://velog.io/@gwontaeyong/Spring-AOP%EC%97%90%EC%84%9C-Proxy%EB%9E%80

##### 왜 프록시 패턴을 사용하나?

##### 스프링부트에 h2 설정하는 방법
##### https://kamang-it.tistory.com/entry/H2%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0%EB%AA%A8%EB%93%9C-%ED%8F%AC%ED%8A%B8

##### h2를 localhost에 띄워 tcp로 접근할 때 에러가 뜬다.
##### url: jdbc:h2:tcp://localhost:10080/~/localadmin

##### https://www.inflearn.com/questions/11283
##### http://www.h2database.com/html/tutorial.html#creating_new_databases




