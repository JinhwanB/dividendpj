FROM openjdk:17
WORKDIR /app
COPY build/libs/dividendpj-0.0.1-SNAPSHOT.jar dividendpj.jar
CMD ["java", "-jar", "dividendpj.jar"]