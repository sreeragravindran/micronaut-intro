FROM openjdk:14-alpine
COPY build/libs/spock-example-*-all.jar spock-example.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "spock-example.jar"]