FROM openjdk:17
EXPOSE 9000
WORKDIR /app
ADD /build/libs/auth-service-0.0.1-SNAPSHOT.jar auth-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","auth-service-0.0.1-SNAPSHOT.jar"]