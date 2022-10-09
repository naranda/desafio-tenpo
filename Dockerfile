FROM amazoncorretto:11
ENV TZ=America/Santiago
ENV JAR_FILE=./target/*.jar
COPY ${JAR_FILE} application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/application.jar"]