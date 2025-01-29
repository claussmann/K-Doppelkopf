FROM debian as buildcontainer
RUN apt update && sudo apt install openjdk-17-jdk maven
RUN mkdir /compile
COPY . /compile
WORKDIR /compile
RUN mvn package

#--

FROM debian:bookworm-slim
RUN mkdir /app
COPY --from=buildcontainer /compile/target/* /app
WORKDIR /app
RUN java -jar K-Doppelkopf-0.0.1.jar
