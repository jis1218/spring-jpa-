##### Line에 OneToMany로 매핑된 List<Section> 즉 Sections의 경우
##### Line 객체에서 Section에 있는 것들을 꺼내올 때

```java
@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
private List<Section> sections = new ArrayList<>();
```

##### 이렇게 되어 있는 경우 테스트 코드에서는 failed to lazily initialize a collection of role라는 메시지가 뜨면서 LazyInitializationException이 뜬다.

##### 트랜잭션 밖에서 조회하면 이런 문제가 발생한다고 하는데
##### 그래서 원래는 테스트 코드 안에서 Section을 넣어줬던 것을 @Transaction이 걸려있는 Service 안에서 처리하였더니 깔끔하게 해결

##### TreeSet에서 Comparable compareTo return 0일 때 add가 안된다.
##### https://stackoverflow.com/questions/31334698/understanding-treeset-when-compareto-returns-0
##### 이걸 참고하자
##### treeset에서 add할 때 맨끝에 add하고 차례대로 비교하는데 만약 return이 0이면 거기서 끝나버림


##### https://pjh3749.tistory.com/283
##### VPC와 CIDR(싸이더)
##### https://namu.wiki/w/%EC%84%9C%EB%B8%8C%EB%84%B7%20%EB%A7%88%EC%8A%A4%ED%81%AC

##### nginx.conf 파일을 찬찬히 살펴보자
```
events {}

http {
  upstream app {
    server 172.17.0.1:8080; //docker gateway
  }

  server {
    listen 80;

    location / {
      proxy_pass http://app;
    }
  }
}
```

