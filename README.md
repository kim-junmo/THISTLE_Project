# THISTLE_Project
화장품 쇼핑몰 사이트 프로젝트(시작 : 24.06.20 ~ 
1. 24.06.17
    DB구성 (카테고리 및 유저테이블), 프로젝트 기본 구성 작업 (회원 기본 패키지 생성  (userVO, UserContorller, UserMapper, UserService))
2. 24.06.18 ~ 24.06.19
   쇼핑몰 메인 페이지 구성
![메인페이지](https://github.com/kim-junmo/THISTLE_Project/assets/163493377/3494a5cf-8b88-44d1-8721-aacf19e5465c)
3. 24.06.20
   쇼핑몰 메인 페이지 구성 完, 회원가입 페이지 구성 完, 아이디 중복체크 기능 작업 完
![회원가입 페이지](https://github.com/kim-junmo/THISTLE_Project/assets/163493377/76356f54-af8b-4e4b-a467-004692e906ff)
4. 24.06.22
   회원가입 페이지 리뉴얼
![image](https://github.com/kim-junmo/THISTLE_Project/assets/163493377/df33f4f4-80bc-43ba-b79b-f70d4c4acd55)
5. 24.06.23(일) ~ 24.06.24(월)
-로그인 기능
  1) 페이지 구성 完
  2) 기능 구현 完
6. 24.06.25(화)
- 아이디 및 비밀번호 찾기 기능
  1) 페이지 구현 完
  2) 이메일 전송 기능 구현 完
  3) 기능 구현 完
## Redis 세션 설정
로그인 세션을 톰캣 메모리 대신 **Redis**에 저장합니다. (Spring Session + Redis)
- 서버를 재시작해도 로그인이 유지되고, 서버를 여러 대로 늘려도 세션이 공유됩니다.
- 브라우저에는 세션 ID만 `THISTLE_SESSION` 쿠키(HttpOnly, SameSite=Lax)로 저장됩니다.
- 세션 만료: 30분 (`RedisSessionConfig`)

### 실행 방법
1. Redis 실행: `docker compose up -d` (Docker가 없다면 Redis를 직접 설치해 6379 포트로 실행)
2. 애플리케이션 실행
3. Redis 접속 정보가 기본값(localhost:6379)과 다르면 `application.properties`에 추가
   ```properties
   spring.data.redis.host=localhost
   spring.data.redis.port=6379
   # spring.data.redis.password=비밀번호
   ```

### 저장된 세션 확인
```bash
docker exec -it thistle-redis redis-cli
keys thistle:session:*
```
