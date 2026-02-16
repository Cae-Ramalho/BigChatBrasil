# ===== STAGE 1 - build =====
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# copia tudo
COPY . .

# dá permissão ao mvnw
RUN chmod +x mvnw

# compila o projeto
RUN ./mvnw clean package -DskipTests

# ===== STAGE 2 - runtime =====
FROM eclipse-temurin:21-jre

WORKDIR /app

# copia apenas o jar gerado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
