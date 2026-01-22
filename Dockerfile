# Build
FROM maven:3.9.12-amazoncorretto-21-debian AS build
WORKDIR /api
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime
FROM amazoncorretto:21
WORKDIR /usr/src/app
COPY --from=build /api/target/*.jar ./vuttr-api.jar
ENTRYPOINT ["java", "-jar", "/usr/src/app/vuttr-api.jar"]
EXPOSE 3000