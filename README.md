# 주식 배당금 정보를 제공하는 API 서비스 프로젝트

## 기술 스택

- SpringBoot 3.2.5
- Java 17
- JPA
- H2
- Redis
- Jsoup
- Docker

## 목표

- ✅ 웹 페이지를 분석하고 스크래핑 기법을 활용하여 필요한 데이터를
  추출/저장 -> [코드로 이동](https://github.com/JinhwanB/dividendpj/blob/main/src/main/java/com/jh/dividendpj/scraper/YahooScraper.java)
- ✅ 서비스에서 캐시의 필요성을 이해하고 캐시 서버를
  구성 -> [redis설정](https://github.com/JinhwanB/dividendpj/blob/main/src/main/java/com/jh/dividendpj/config/CacheConfig.java), [redis 적용 코드1](https://github.com/JinhwanB/dividendpj/blob/e1e78f35adbf220800b1bc88d317ec6787ec6384/src/main/java/com/jh/dividendpj/company/service/CompanyService.java#L74), [redis 적용 코드2](https://github.com/JinhwanB/dividendpj/blob/e1e78f35adbf220800b1bc88d317ec6787ec6384/src/main/java/com/jh/dividendpj/company/controller/CompanyController.java#L62)
- ✅ Spring Security JWT 토큰을 이용한 로그인 인증/인가
  처리 -> [security 설정](https://github.com/JinhwanB/dividendpj/tree/main/src/main/java/com/jh/dividendpj/auth)
- ✅ 로그인 인증 Exception
  처리 -> [403 에러 핸들러](https://github.com/JinhwanB/dividendpj/blob/main/src/main/java/com/jh/dividendpj/auth/JwtAuthenticationEntryPoint.java)
- ✅ 프로젝트 Docker 이미지화 -> [dockerfile](https://github.com/JinhwanB/dividendpj/blob/main/Dockerfile)
- ✅ ControllerAdvice 에서 에러
  처리하기 -> [exceptionHandler](https://github.com/JinhwanB/dividendpj/blob/main/src/main/java/com/jh/dividendpj/config/GlobalExceptionHandler.java)

## ERD

<p align="center">
  <img src="https://github.com/JinhwanB/dividendpj/assets/123534245/5fe56f5c-942c-469f-82b9-88236183840e" width="40%" height="0%">
</p>

## 파일 구조

```angular2html
├─build
├─gradle
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─jh
    │  │          └─dividendpj
    │  │              ├─auth
    │  │              ├─company
    │  │              │  ├─controller
    │  │              │  ├─domain
    │  │              │  ├─dto
    │  │              │  ├─exception
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─config
    │  │              ├─dividend
    │  │              │  ├─domain
    │  │              │  ├─dto
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              ├─member
    │  │              │  ├─controller
    │  │              │  ├─domain
    │  │              │  ├─dto
    │  │              │  ├─exception
    │  │              │  ├─repository
    │  │              │  └─service
    │  │              └─scraper
    │  │                  ├─exception
    │  │                  └─scheduler
    │  └─resources 
    └─test
```

## 최종 구현 API

1. GET -
   finance/dividend/{companyName} -> [service](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/service/CompanyService.java#L76), [controller](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/controller/CompanyController.java#L85)

- 회사 이름을 인풋으로 받아서 해당 회사의 메타 정보와 배당금 정보를 반환
- 잘못된 회사명이 입력으로 들어온 경우 400 status 코드와 에러메시지 반환

2. GET -
   company/autocomplete -> [service](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/service/CompanyService.java#L91), [controller](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/controller/CompanyController.java#L72)

- 자동완성 기능을 위한 API
- 검색하고자 하는 prefix 를 입력으로 받고, 해당 prefix 로 검색되는 회사명 리스트 중 10개 반환

3. GET -
   company -> [service](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/service/CompanyService.java#L104), [controller](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/controller/CompanyController.java#L102)

- 서비스에서 관리하고 있는 모든 회사 목록을 반환
- 반환 결과는 Page 인터페이스 형태

4. POST -
   company -> [companyService](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/service/CompanyService.java#L40), [dividendService](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/dividend/service/DividendService.java#L20), [controller](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/controller/CompanyController.java#L38)

- 새로운 회사 정보 추가
- 추가하고자 하는 회사의 ticker 를 입력으로 받아 해당 회사의 정보를 스크래핑하고 저장
- 이미 보유하고 있는 회사의 정보일 경우 400 status 코드와 적절한 에러 메시지 반환
- 존재하지 않는 회사 ticker 일 경우 400 status 코드와 적절한 에러 메시지 반환

5. DELETE -
   company/{ticker} -> [service](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/service/CompanyService.java#L62), [controller](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/controller/CompanyController.java#L51)

- ticker 에 해당하는 회사 정보 삭제
- 삭제시 회사의 배당금 정보와 캐시도 모두
  삭제함 -> [캐시 삭제](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/company/controller/CompanyController.java#L108)

6. POST -
   auth/signup -> [service](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/member/service/MemberService.java#L33), [controller](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/member/controller/MemberController.java#L26)

- 회원가입 API
- 중복 ID 는 허용하지 않음
- 패스워드는 암호화된 형태로 저장됨

7. POST -
   auth/signin -> [service](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/member/service/MemberService.java#L47), [controller](https://github.com/JinhwanB/dividendpj/blob/697009aa24d4e9a1b75befe2a6fec38ec560f4e1/src/main/java/com/jh/dividendpj/member/controller/MemberController.java#L34)

- 로그인 API
- 회원가입이 되어있고, 아이디/패스워드 정보가 옳은 경우 JWT 발급