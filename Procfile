web: java -jar target/*.jar --spring.profiles.active=prod --server.port=$PORT
release: cp -R src/main/resources/config config && ./mvnw -Pprod clean verify
