
-- Run Spring Boot

./mvnw spring-boot:run

-- Start Kakfa

cd /Users/raj/ws/stephane/schema-registry-avro/code/2-start-kafka
docker-compose up

-- Verify installation

http://localhost:3030/

-- Install Command line tools

# Get the command line for
   + kafka-avro-console-producer &
   + kafka-avro-console-consumer
docker run -it --rm --net=host confluentinc/cp-schema-registry:3.3.1 bash
docker exec -it cassandra-node-01  bash

root@docker-desktop:/#
root@docker-desktop:/# kafka-avro-console-
kafka-avro-console-consumer  kafka-avro-console-producer

-- List topic

kafka-topics.sh --zookeeper 127.0.0.1:2181 --list

-- Delete & Create

kafka-topics.sh --zookeeper 127.0.0.1:2181  --delete --topic
kafka-topics.sh --zookeeper 127.0.0.1:2181 --create --partitions 1 --replication-factor 1 --topic


-- Read from topic

kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --from-beginning --topic i1

kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic test print.key=true --property key.separator="|" --from-beginning


-- SQL

/var/folders/ty/297_tr6524nf3p8vynp7v1z00000gn/T/confluent.ZG5kpOeF


kafka-console-consumer --bootstrap-server 127.0.0.1:9092  --from-beginning     --formatter kafka.tools.DefaultMessageFormatter     --property print.key=true     --property print.value=true     --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer     --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer --topic output


