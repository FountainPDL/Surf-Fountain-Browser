# Minimal clean gradlew without Xmx issues
#!/usr/bin/env sh
APP_HOME=$(dirname "$0")
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
JAVACMD=java
if [ -n "$JAVA_HOME" ]; then JAVACMD="$JAVA_HOME/bin/java"; fi
exec "$JAVACMD" $JAVA_OPTS "-Dorg.gradle.appname=gradlew" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"