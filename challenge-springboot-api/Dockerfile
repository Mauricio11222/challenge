# ---------- BUILD ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos solo pom primero (mejor cache)
COPY pom.xml .

# Descargamos dependencias
RUN mvn dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Empaquetamos
RUN mvn -DskipTests package


# ---------- RUN ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
