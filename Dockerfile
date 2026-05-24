# ==========================================
# Etapa 1: Compilación (Build Stage)
# ==========================================
FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /usr/src/app

# Instalar Maven en Alpine para compilar con la JDK 25
RUN apk add --no-cache maven

# Copiar el archivo pom.xml para descargar y cachear las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copiar el código fuente
COPY src ./src

# Compilar y empaquetar el microservicio omitiendo los tests para acelerar el build
RUN mvn clean package -DskipTests -B


# ==========================================
# Etapa 2: Ejecución de Producción (Runner Stage)
# ==========================================
FROM eclipse-temurin:25-jre-alpine AS runner

WORKDIR /usr/app

# Copiar el archivo .jar generado en la etapa anterior
COPY --from=build /usr/src/app/target/*.jar app.jar

# Exponer el puerto estandarizado de Auth
EXPOSE 5050

# Regla de Seguridad DevOps: Crear un grupo y usuario del sistema sin privilegios
RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir -p /usr/app/data && chown -R spring:spring /usr/app/data
USER spring

# Comando de ejecución del microservicio
ENTRYPOINT ["java", "-jar", "app.jar"]
