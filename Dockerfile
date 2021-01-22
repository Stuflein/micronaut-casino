FROM amazoncorretto:15
COPY build/libs/casino-*-all.jar casino.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-jar", "casino.jar"]
