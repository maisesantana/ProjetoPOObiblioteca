# Usa Java 17 com Jetty
FROM jetty:11-jdk17

# Copia o WAR gerado pelo Maven para o Jetty
COPY target/atlas.war /var/lib/jetty/webapps/ROOT.war

# Jetty roda na porta 8080
EXPOSE 8080