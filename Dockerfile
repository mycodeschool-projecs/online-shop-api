#Use a maintained Temurin JDK base image (matches project Java 17 requirement)
FROM eclipse-temurin:17-jdk-jammy

#Metadata as described above
LABEL maintainer = "olibadescu@gmail.com"
LABEL version = "1.0"
LABEL description = "Docker image for kube-land Srping boot application"

#Set the current working directory inside the image
WORKDIR /app
EXPOSE 8080

#Copy maven execeutable to the image
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

#Asigura-te ca mvnw are permisiuni de executie
RUN chmod +x mvnw

#Build all the dependencies in preparation to go offline
RUN ./mvnw dependency:go-offline -B

#Copy the project source
COPY src src

#Package the application
RUN ./mvnw package -DskipTests

#Specify the start command and entry point of the Spring Boot application
ENTRYPOINT ["java","-jar","/app/target/online_shop_api-0.0.1-SNAPSHOT.jar"]
