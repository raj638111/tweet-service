package com.charter.tweets.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.charter.tweets.model.*;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TweetController {

    CqlSession session;
    KafkaProducer<String, String> producer;
    final String TOPIC_TWEETS = "tweets";
    final String TABLE_TSTAMP = "charter.trends_tstamp";
    final String TABLE_TRENDS_BY_COUNT = "charter.trends_bycount";
    final String TABLE_TRENDS_BY_TAG = "charter.trends_bytag";

    @Autowired
    TweetController() {
        log.info("Creating cassandra session");
        session = CqlSession.builder().build();
        log.info("Session created");
        log.info("Creating Kafka producer");
        Properties properties = getPropery();
        producer = new KafkaProducer<String, String>(properties);
        log.info("Kafka producer created");
        Thread cleanup = new Thread(() -> {
            log.info("Closing cassandra session");
            session.close();
            log.info("Closing kafka producer");
            producer.close();
        });
        Runtime.getRuntime().addShutdownHook(cleanup);
    }


    @PostMapping("/tweet")
    public Boolean gpost(@RequestBody TweetRequest req) {
        List<TweetInfo> dataList = parseTweet(req.getTweet());
        for(TweetInfo data : dataList) {
            String concat = data.tweet + " |!| " + data.time;
            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>(TOPIC_TWEETS, data.hashtag, concat);
            producer.send(record);
        }
        producer.flush();
        return true;
    }

    @GetMapping("/trending-hashtags")
    public TrendInfo getTrends(
        @RequestParam(value = "tstamp", defaultValue = "") String tstamp
    ) throws Exception {
        log.info("tstamp -> " + tstamp);
        if(tstamp.equals("")) {
            String latestTime = getLatestTime();
            return retieveCountFromDB(latestTime);
        }else {
            return retieveCountFromDB(tstamp);
        }
    }

    @GetMapping("/gettag")
    public TagInfo getTag(
            @RequestParam(value = "tag", defaultValue = "") String tag
    ) throws Exception {
        log.info("tag -> " + tag);
        if(tag.equals("")) {
            throw new Exception("tag parameter needed. Pls specify");
        }else {
            return retrieveDatesForAtag(tag);
        }
    }

    public TagInfo retrieveDatesForAtag(String tag) {
        ArrayList<TimeNCount> lst = new ArrayList<TimeNCount>();
        String query = "select tstamp, count from " + TABLE_TRENDS_BY_TAG +
                " where hashtag = " + String.format("'%s' limit 25", tag);
        log.info("Query = " + query);
        ResultSet result = session.execute(query);
        for(Row row: result){
            String tstamp = row.getInstant("tstamp").toString();
            String count = Long.toString(row.getLong("count"));
            TimeNCount info = new TimeNCount();
            info.setTime(tstamp);
            info.setCount(count);
            lst.add(info);
        }
        TagInfo info = new TagInfo();
        info.setTag(tag);
        info.setCountList(lst);
        return info;
    }

    public TrendInfo retieveCountFromDB(String tstamp){
        ArrayList<TagNCount> lst = new ArrayList<TagNCount>();
        String query = "select hashtag, count from " + TABLE_TRENDS_BY_COUNT +
            " where tstamp = " + String.format("'%s' limit 25", tstamp);
        log.info("Query = " + query);
        ResultSet result = session.execute(query);
        for(Row row: result){
            String hashtag = row.getString("hashtag");
            String count = Long.toString(row.getLong("count"));
            TagNCount info = new TagNCount();
            info.setTag(hashtag);
            info.setCount(count);
            lst.add(info);
        }
        TrendInfo trendInfo = new TrendInfo();
        trendInfo.setTime(tstamp);
        trendInfo.setCountList(lst);
        return trendInfo;
    }

    public String getLatestTime() throws Exception {
        String query = "select tstamp from " + TABLE_TSTAMP + " where dummy = '-' limit 1";
        log.info("query -> " + query);
        ResultSet result = session.execute(query);
        Iterator<Row> iterator = result.iterator();
        if(iterator.hasNext()) {
            String tstamp = iterator.next().getString("tstamp");
            log.info("tstamp -> " + tstamp);
            return tstamp;
        }else {
            throw new Exception("No results available in table -> " + TABLE_TSTAMP);
        }
    }

    public List<TweetInfo> parseTweet(String tweetWithHashTag) {
        String dtime = nearest5minutes(new Timestamp(System.currentTimeMillis()));
        List<TweetInfo> result = new ArrayList<TweetInfo>();
        String pattern = "(#[a-zA-Z0-9]+)";
        String tweet = tweetWithHashTag.replaceAll(pattern, "");
        log.info("tweetWithHashTag -> " + tweetWithHashTag + ", tweet -> " + tweet);
        Matcher m = Pattern.compile(pattern).matcher(tweetWithHashTag);
        log.info(String.valueOf(m.groupCount()));
        while(m.find()) {
            TweetInfo data = new TweetInfo();
            data.hashtag = m.group(1);
            data.tweet = tweet.trim();
            data.time = dtime;
            log.info("data -> " + data);
            result.add(data);
        }
        return result;
    }

    public String nearest5minutes(Timestamp timestamp) {
        SimpleDateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        Integer min = (Integer.parseInt(minuteFormat.format(timestamp)) / 5) * 5;
        String result = hourFormat.format(timestamp) + ":" + String.format("%02d", min);
        log.info("dtime -> " + result);
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

