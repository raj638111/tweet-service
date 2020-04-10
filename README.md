# The Design

Considering the user should be able to get a score from both the **Command Line** and from a **Web App** I have decided to create this utility based on a **client** / **server** (Web Service) architecture

# Project Structure

This project is divided into 2 Modules

1. `$HOME/client`
    - **command line utility** that will speak to the web service to get the answer         
2. `$HOME/service` 
    - **Web Service** that provides the ranking service. Web service is developed using `Spring Boot`
    
# Compilation

`mvn clean package` from `$HOME` produces,

```
...
/Users/raj/ws/ranking-service/client/target/client-1.0-jar-with-dependencies.jar
...
/Users/raj/ws/ranking-service/service/target/service-0.0.1-SNAPSHOT.jar
```

# Starting the Web Server

From `$HOME/service` run,

```
java -jar target/service-0.0.1-SNAPSHOT.jar
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

# CLI: `ls` Command 

Note: **Ensure Web service is running**

This command lists the available Ranking Service implementations. 
(Different teams in the organization would want to have different ranking algorithms)
 
From `$HOME\client`,
```
java -jar target/client-1.0-jar-with-dependencies.jar ls --host http://localhost:8080

Getting list of available Ranking service implementation from http://localhost:8080/ls...

== Service: com.occ.ranking.service.Ranking ==

To score a list of names, you must sort it alphabetically and sum
the individual scores for all the names. To score a
name, sum the alphabetical value of each letter (A=1, B=2,
C=3, etc...) and multiply the sum by the name’s position in
the list (1-based). For example, for the sample data:
MARY,PATRICIA,LINDA,BARBARA,VINCENZO,SHON,LYNWOOD,JERE,HAI
is sorted into alphabetical order, LINDA, which is worth
12 + 9 + 14 + 4 + 1 = 40, is the 4th name in the list.
So, LINDA would obtain a score of 40 x 4 = 160. The
correct score for the entire list is 3194


== Service: com.occ.ranking.service.RankingWithoutOffset ==

An alternative implementation derived from 'Ranking' service
Avoids mutiplying the sum of letters of a given word with
index / offset

**USE ^ any one one of the service as --service <service> in 'rank' command
```

# CLI: `rank` Command

This command provides the rank for a given input file. The calculation of rank varies based on the --service <service> selected

From `$HOME/client`,

```
java -jar target/client-1.0-jar-with-dependencies.jar rank \
--host http://localhost:8080 \
--service com.occ.ranking.service.Ranking \
--file /Users/raj/Desktop/occ/test.txt \
--nameselect FIRST_NAME

Getting rank for file /Users/raj/Desktop/occ/test.txt from http://localhost:8080/rank...

Rank for file is 3194

```

# CLI: Getting help

From `$HOME/client`,

**Get list of supported commands,**
```
java -jar target/client-1.0-jar-with-dependencies.jar
Usage: <main class> [COMMAND]
Commands:
  ls    'ls' command lists all the Ranking service implementation/algorithm
          available
  rank  'rank' command computes rank for a given file
```
**

**Get help for `ls` command,**

```
java -jar target/client-1.0-jar-with-dependencies.jar ls
Missing required option '--host=<host>'
Usage: <main class> ls --host=<host>
'ls' command lists all the Ranking service implementation/algorithm available
      --host=<host>   Reset service host name & port
                      Example: http://localhost:8080
```


**Get help for `rank` command,** 
```
java -jar target/client-1.0-jar-with-dependencies.jar rank
Missing required options [--host=<host>, --file=<file>, --service=<service>]
Usage: <main class> rank [--descending] --file=<file> --host=<host>
                         [--nameselect=<nameselect>] --service=<service>
'rank' command computes rank for a given file
      --descending          Should we sort the name in ascending or descending
                              order
      --file=<file>         Absolute path of the file (for which rank needs to
                              be calculated)
                            Data format: "fname1 lname1","fname2 lname2",...
      --host=<host>         Reset service host name & port
                            Example: http://localhost:8080
      --nameselect=<consider>
                            Should we sort only the first name, last name or
                              both. Available values are FIRST_NAME, LAST_NAME,
                              BOTH
      --service=<service>   Ranking Service to be used to calculate rank. Run
                              'ls' command to get the list of Ranking Service
                              available
```





