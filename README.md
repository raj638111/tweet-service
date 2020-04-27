# About

This web service provides two end points
1. Receive & Store the tweet from user into kafka
2. Retrieve the Hashtag trend list from Cassandra and return it to user

# Project Structure

`$HOME/service`: The code for the web service is available here
    
# Compilation

`mvn clean package` from `$PROJECT_HOME` produces,

```
$PROJECT_HOME/service/target/service-0.0.1-SNAPSHOT.jar
```

# Starting the Web Server

From `$PROJECT_HOME` run,

(Note: Use `--port <port no>` if you want to run the server on different port)

```
java -jar ./service/target/service-0.0.1-SNAPSHOT.jar
```
This should start the web service on `localhost:8080`

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.6.RELEASE)
...
```

# Project Structure

```
service
.
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── charter
    │   │           └── tweets
    │   │               ├── TweetServiceApplication.java
    │   │               ├── controller
    │   │               │   └── TweetController.java
    │   │               ├── helpers
    │   │               │   └── Param.java
    │   │               └── model
    │   │                   ├── TagInfo.java
    │   │                   ├── TagNCount.java
    │   │                   ├── TimeNCount.java
    │   │                   ├── TrendInfo.java
    │   │                   ├── TweetInfo.java
    │   │                   └── TweetRequest.java
    │   └── resources
    │       ├── application.conf
    │       ├── logback.xml
```