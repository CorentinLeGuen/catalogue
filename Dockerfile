# Build
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /build
COPY . .
RUN ./mvnw clean package

# Runtime
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY . ./
RUN chmod +x mvnw && ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install
CMD ["sh", "-c", "java -jar target/*.jar"]
