# gosu

일상 서비스 매칭 플랫폼 백엔드

## 로컬 실행

1. `src/main/resources/application-secret.properties.example`을 복사해서 `application-secret.properties`로 이름을 바꾼다.
2. 팀 채널 고정 메시지의 값을 채운다.
3. `./gradlew bootRun` 또는 IntelliJ에서 `GosuApplication`을 실행한다.

| 주소                                        | 용도                                     |
|---------------------------------------------|------------------------------------------|
| http://localhost:8080/swagger-ui/index.html | API 문서                                 |
| http://localhost:8080/h2-console            | H2 콘솔 (JDBC URL: `jdbc:h2:mem:gosuDB`) |