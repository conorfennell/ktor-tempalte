FROM gradle:6.4.0-jdk11 AS build
WORKDIR /app
COPY . .
RUN gradle build --stacktrace --info
RUN ls /app
RUN ls /app/build/libs/

FROM azul/zulu-openjdk-alpine:11
COPY --from=build /app/build/libs/app-all.jar /opt/app/app.jar
EXPOSE 8000

RUN addgroup -S -g 10001 appGrp \
    && adduser -S -D -u 10000 -s /sbin/nologin -h /opt/app/ -G appGrp app\
    && chown -R 10000:10001 /opt/app

USER 10000:10001

ENTRYPOINT exec java $JAVA_OPTS \
-XX:+UseContainerSupport \
-jar /opt/app/app.jar com.idiomcentric.AppKt
