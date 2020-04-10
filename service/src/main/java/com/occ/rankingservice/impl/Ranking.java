package com.occ.rankingservice.impl;

import com.occ.rankingservice.utils.NameInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
@Qualifier("Ranking")
public class Ranking {

    protected Map<Character, Integer> charMap = null;

    Ranking(){
        this.charMap = createAlphabetIndexMap();
    }

    public BigInteger calculateSum(List<NameInfo> names){
        BigInteger result = new BigInteger("0");
        for(NameInfo nameInfo: names){
            String name = nameInfo.name;
            Integer nameSum = name.chars().reduce(0,
                (part, ch) -> {
                    Integer letterIndex = charMap.get((char)ch);
                    Integer newIndex = letterIndex == null ? 0 : letterIndex;
                    return part + newIndex;
                });
            Long product = nameInfo.offset * nameSum;
            result = result.add(BigInteger.valueOf(product.longValue()));
        }
        log.info("Result -> " + result);
        return result;
    }

    public List<NameInfo> parseAndSortNames(String str, String consider,
        Boolean descending){
        Comparator<String> comparator = descending == false ? Comparator.naturalOrder() :
            Comparator.reverseOrder();
        String[] names = str.split(",");
        Stream<String> stream = Arrays.stream(names);
        AtomicLong atomicLong = new AtomicLong(0);
        Stream<NameInfo> result = stream
                .map(x -> enrichName(x, consider).replace("\"", "" )
                        .replace("\n", "").toLowerCase())
                .sorted(comparator)
                .map(x -> {
                    Long index = atomicLong.incrementAndGet();
                    return new NameInfo(x, index);
                });
        List<NameInfo> res = result.collect(Collectors.toList());
        return res;
    }

    private String enrichName(String name, String considerOnly){
        String[] splitted = name.split(" ");
        String firstName = "";
        String lastName = "";
        if(splitted.length == 1){
            firstName = splitted[0].trim();
        }else if (splitted.length >= 2) {
            firstName = splitted[0].trim();
            lastName = splitted[1].trim();
        }
        if(considerOnly.equals("firstname")){
            return firstName;
        }else if(considerOnly.equals("lastname")){
            return lastName;
        }else{
            return name;
        }
    }

    private Map<Character, Integer> createAlphabetIndexMap(){
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        IntStream.rangeClosed('a', 'z')
            .forEach(c -> {
                Integer index = atomicInteger.incrementAndGet();
                map.put((char) c, index);
            });
        return map;
    }

}
