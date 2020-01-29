FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD mapp-eureka-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
RUN mkdir /logs
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5555 -jar /app.jar --spring.profiles.active=prod" ]