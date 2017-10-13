# sip-registry


### Clone the git repository

```Bash
git clone git@github.com:sysadmin4j/sip-registry.git
cd sip-registry
```

### Build and Test the application

```Bash
docker run -it --rm --name my-maven-project -v "$PWD":/usr/src/mymaven -w /usr/src/mymaven maven:3.5.0-jdk-8 mvn clean install
```

### Run the application

```Bash
docker run --rm -v "$PWD":/usr/src/myapp -w /usr/src/myapp -p 10000:10000 openjdk:8u141 java -jar target/sip-registry-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

### Connect to the registry service

```Bash
telnet localhost 10000
```
