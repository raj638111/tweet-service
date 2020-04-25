package com.occ.ranking.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RankingController {

    CqlSession session;

    @Autowired // Inject all 'Ranking' implementations
    RankingController() {
        log.info("Creating cassandra session");
        session = CqlSession.builder().build();
        log.info("Session created");
        Thread cleanup = new Thread(() -> {
                log.info("Closing cassandra session");
                session.close();
            }
        );
        Runtime.getRuntime().addShutdownHook(cleanup);
    }


    @PostMapping("/tweet")
    public Boolean gpost(@RequestParam(value = "tweet") String tweet) {
        List<Data> dataList = parseTweet(tweet);
        Properties properties = getPropery();
        KafkaProducer<String, String> producer =
                new KafkaProducer<String, String>(properties);
        for(Data data : dataList) {
            String concat = data.tweet + " |!| " + data.time;
            ProducerRecord<String, String> record =
                new ProducerRecord<String, String>("i1", data.hashtag, concat);
            producer.send(record);
        }
        producer.flush();
        producer.close();
        return false;
    }

    @GetMapping("/trending-hashtags")
    public List<String> getTrends() {
        List<String> lst = new ArrayList<String>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMinute = now.minusMinutes(1);
        String formattedDate = lastMinute.format(formatter);
        log.info("currentDate = " + now.format(formatter) +
                ", lastDate = " + formattedDate);
        String query = "select hashtag, count from charter.trends_countorder where tstamp = " +
            String.format("'%s' limit 25", now.format(formatter));
        log.info("Query = " + query);
        ResultSet result = session.execute(query);
        for(Row row: result){
            String hashtag = row.getString("hashtag");
            //String count = row.getString("count");
            lst.add(hashtag);
        }
        return lst;
    }

    public List<Data> parseTweet(String tweetWithHashTag) {
        List<Data> result = new ArrayList<Data>();
        String pattern = "(#[a-zA-Z0-9]+)";
        String tweet = tweetWithHashTag.replaceAll(pattern, "");
        log.info("tweetWithHashTag -> " + tweetWithHashTag + ", tweet -> " + tweet);
        Matcher m = Pattern.compile(pattern).matcher(tweetWithHashTag);
        log.info(String.valueOf(m.groupCount()));
        while(m.find()) {
            Data data = new Data();
            data.hashtag = m.group(1);
            data.tweet = tweet.trim();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dtime = sdf.format(timestamp);
            data.time = dtime;
            log.info("data -> " + data);
            result.add(data);
        }
        return result;
    }

    public Properties getPropery() {
        String bootstrapServers = "127.0.0.1:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        return properties;
    }
}

class Data{
    public String tweet;
    public String hashtag;
    public String time;

    @Override
    public String toString() {
        return "tweet = " + tweet + ", hashtag = " + hashtag + ", time = " + time;
    }
}