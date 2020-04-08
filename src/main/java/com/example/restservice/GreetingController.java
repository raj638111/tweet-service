package com.example.restservice;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javafx.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name){
        log.info("Greeting. " + name);
        return new Greeting(Thread.currentThread().getId(), String.format(template, name));
    }

    @PostMapping("/gpost")
    public Pair<String, BigInteger> gpost(@RequestParam("file") MultipartFile file) throws IOException {
        String fName = file.getOriginalFilename();
        log.info("fName -> " + fName);
        String str = new String(file.getBytes());
        List<Pair<String, Long>> names = parseAndSortNames(str);
        Map<Character, Integer> map = createAlphabetToIndexMap();
        BigInteger sum = calculateSum(names, map);
        return new Pair(fName, sum);
    }

    private BigInteger calculateSum(List<Pair<String, Long>> names, Map<Character, Integer> map){
        BigInteger result = new BigInteger("0");
        for(Pair<String, Long> pair: names){
            String name = pair.getKey();
            Integer nameSum = name.chars().reduce(0,
                (part, ch) -> {
                    Integer letterIndex = map.get((char)ch);
                    return part + letterIndex;
                });
            Long nameIndex = pair.getValue();
            Long product = nameIndex * nameSum;
            result = result.add(BigInteger.valueOf(product.longValue()));
        }
        log.info("Result -> " + result);
        return result;
    }

    private Map<Character, Integer> createAlphabetToIndexMap(){
        Map map = new HashMap<Character, Integer>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        IntStream.rangeClosed('a', 'z')
            .forEach(c -> {
                Integer index = atomicInteger.incrementAndGet();
                map.put((char)c, index);
            });
        return map;
    }

    private List<Pair<String, Long>> parseAndSortNames(String str){
        String[] names = str.split(",");
        Stream<String> stream = Arrays.stream(names);
        AtomicLong atomicLong = new AtomicLong(0);
        Stream<Pair<String, Long>> result = stream
            .map(x -> x.replace("\"", "" )
                .replace("\n", "").toLowerCase())
            .sorted(Comparator.naturalOrder())
            .map(x -> {
                Long index = atomicLong.incrementAndGet();
                return new Pair(x, index);
            });
        List<Pair<String, Long>> res = result.collect(Collectors.toList());
        return res;
    }

}

