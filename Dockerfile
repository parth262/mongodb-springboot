FROM openjdk:11

WORKDIR /home/app
COPY ./build/libs/mongo-springboot-test-0.0.1-SNAPSHOT.jar .

CMD java -jar mongo-springboot-test-0.0.1-SNAPSHOT.jar

EXPOSE 9090