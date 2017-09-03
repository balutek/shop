FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD redis-0.0.1.jar app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 1001
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
