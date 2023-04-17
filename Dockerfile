FROM openjdk:17-oracle
ARG JAR_FILE=build/libs/Questioning-Musseukgi-1.0.0.jar
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} \
SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME} \
SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}  \
OPENAI_API_KEY=${OPENAI_API_KEY}
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]