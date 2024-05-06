# 주식 배당금 정보를 제공하는 API 서비스 프로젝트

# 기술 스택

- SpringBoot 3.2.5
- Java 17
- JPA
- H2
- Redis
- Jsoup
- Docker

# 목표

- ✅ 웹 페이지를 분석하고 스크래핑 기법을 활용하여 필요한 데이터를
  추출/저장 -> [코드로 이동](https://github.com/JinhwanB/dividendpj/blob/main/src/main/java/com/jh/dividendpj/scraper/YahooScraper.java)
- ✅ 서비스에서 캐시의 필요성을 이해하고 캐시 서버를
  구성 -> [redis설정](https://github.com/JinhwanB/dividendpj/blob/main/src/main/java/com/jh/dividendpj/config/CacheConfig.java), [redis 적용 코드1](https://github.com/JinhwanB/dividendpj/blob/e1e78f35adbf220800b1bc88d317ec6787ec6384/src/main/java/com/jh/dividendpj/company/service/CompanyService.java#L74), [redis 적용 코드2](https://github.com/JinhwanB/dividendpj/blob/e1e78f35adbf220800b1bc88d317ec6787ec6384/src/main/java/com/jh/dividendpj/company/controller/CompanyController.java#L62)

# ERD

<img src="https://github.com/JinhwanB/dividendpj/assets/123534245/5fe56f5c-942c-469f-82b9-88236183840e" width="200" height="400">