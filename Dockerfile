FROM debian as buildcontainer
RUN apt update && apt install -y openjdk-17-jdk maven
RUN mkdir /compile
COPY . /compile
WORKDIR /compile
RUN mvn package

#--

FROM debian:bookworm-slim
RUN mkdir /app
COPY --from=buildcontainer /compile/target/* /app
WORKDIR /app
RUN apt update && apt install -y openjdk-17-jre
RUN ls
EXPOSE 8080
ENTRYPOINT java -jar K-Doppelkopf-0.1-alpha.jar --port=8080
