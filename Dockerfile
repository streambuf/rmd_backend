FROM openjdk:8-jdk-alpine

ARG MONGO_PASSWORD
ARG SERVER_HOST
ENV MONGO_PASSWORD=${MONGO_PASSWORD}
ENV SERVER_HOST=${SERVER_HOST}

EXPOSE 8008

RUN mkdir /app
WORKDIR /app
COPY . /app
RUN ./gradlew build --no-daemon

ENTRYPOINT ["java",\
"-Dspring.profiles.active=prod",\
"-Dspring.data.mongodb.password=$MONGO_PASSWORD",\
"-Dspring.app.serverHost=$SERVER_HOST"\
,"-jar","./build/libs/monolith-0.0.1-SNAPSHOT.jar"]