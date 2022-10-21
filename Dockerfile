ARG BUILD_HOME=/build

FROM gradle:7.4.2-jdk17-alpine as build

ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts gradle.properties $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src
COPY --chown=gradle:gradle gradle $APP_HOME/gradle
COPY --chown=gradle:gradle .git $APP_HOME/.git

# build polybot
RUN gradle --no-daemon shadowJar

#
# Use temurin alpine to run polybot
#
FROM eclipse-temurin:18-jre-alpine

ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME

WORKDIR /app/

# copy from build
COPY --from=build $APP_HOME'/build/libs/*-all.jar' /polybot/polybot.jar

VOLUME /app/

ENTRYPOINT ["java", "-jar", "/polybot/polybot.jar"]