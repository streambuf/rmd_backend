FROM openjdk:8-jdk-alpine

EXPOSE 8008

RUN mkdir /app
WORKDIR /app
COPY . /app
RUN ./gradlew build --no-daemon

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar ./build/libs/monolith-0.0.1-SNAPSHOT.jar" ]