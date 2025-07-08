FROM eclipse-temurin:21-jre  
WORKDIR /app  

# Copia el JAR (ajusta el nombre si es diferente)  
COPY target/parcial-0.0.1-SNAPSHOT.jar app.jar  

# Expone el puerto definido en application.properties (4000)  
EXPOSE 4000  

# Inicia la aplicación sin variables de entorno  
ENTRYPOINT ["java", "-jar", "app.jar"]  