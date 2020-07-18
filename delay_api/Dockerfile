FROM openjdk:14-alpine
COPY build/libs/delay_api-*-all.jar delay_api.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "delay_api.jar"]