FROM maven:3.8.1-openjdk-17-slim AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM tomcat:9.0.108-jdk17-corretto
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["[catalina.sh](http://catalina.sh/)", "run"]