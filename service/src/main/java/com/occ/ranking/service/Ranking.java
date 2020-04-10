package com.occ.ranking.service;

import com.occ.ranking.constants.NameSelection;
import com.occ.ranking.model.NameInfo;
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

    public Ranking(){
        this.charMap = createAlphabetIndexMap();
    }

    /**
     * Calculate Sum
     * @param names Name list
     * @return Sum
     */
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

    /**
     * Parse the content of a file into a list of sorted names.
     * Also add offset for each name starting from the offset 1
     * @param str The content of the entire file passed by the user
     * @param nameSelect Should we sort using First name or Last name or Both
     * @param descending Should we sort the names in ascending or descending order
     * @return
     */
    public List<NameInfo> parseAndSortNames(String str, NameSelection nameSelect,
        Boolean descending){
        Comparator<String> comparator = descending == false ? Comparator.naturalOrder() :
            Comparator.reverseOrder();
        String[] names = str.split(",");
        Stream<String> stream = Arrays.stream(names);
        AtomicLong atomicLong = new AtomicLong(0);
        Stream<NameInfo> result = stream
            .map(x -> enrichName(x, nameSelect).replace("\"", "" )
                    .replace("\n", "").toLowerCase())
            .sorted(comparator)
            .map(x -> {
                Long index = atomicLong.incrementAndGet();
                return new NameInfo(x, index);
            });
        List<NameInfo> res = result.collect(Collectors.toList());
        return res;
    }

    /**
     * Parse a full name in first name (or) last name (or) both
     * @param name Name of a person
     * @param nameSelect Should we extract first name (or) last name (or)
     *                   both. Can be any of this value {@link NameSelection}
     * @return first name (or) last name (or) both
     */
    public String enrichName(String name, NameSelection nameSelect){
        String[] splitted = name.split(" ");
        String firstName = "";
        String lastName = "";
        if(splitted.length == 1){
            firstName = splitted[0].trim();
        }else if (splitted.length >= 2) {
            firstName = splitted[0].trim();
            lastName = splitted[1].trim();
        }
        if(nameSelect == NameSelection.FIRST_NAME){
            return firstName;
        }else if(nameSelect == NameSelection.LAST_NAME){
            return lastName;
        }else{
            return name;
        }
    }

    /**
     * Create Alphabet to Index mapping
     * Example: a -> 1, b -> 2, ...
     */
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

    public String info() {
        return "To score a list of names, you must sort it alphabetically and sum\n" +
            "the individual scores for all the names. To score a \n" +
            "name, sum the alphabetical value of each letter (A=1, B=2, \n" +
            "C=3, etc...) and multiply the sum by the name’s position in \n" +
            "the list (1-based). For example, for the sample data: \n" +
            "MARY,PATRICIA,LINDA,BARBARA,VINCENZO,SHON,LYNWOOD,JERE,HAI \n" +
            "is sorted into alphabetical order, LINDA, which is worth \n" +
            "12 + 9 + 14 + 4 + 1 = 40, is the 4th name in the list. \n" +
            "So, LINDA would obtain a score of 40 x 4 = 160. The \n" +
            "correct score for the entire list is 3194 \n";
    }

}

