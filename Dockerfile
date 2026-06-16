FROM openjdk:17

WORKDIR /app

COPY . .

RUN chmod +x gradlew

CMD ["bash"]
