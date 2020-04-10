package com.occ.ranking.service;

import com.occ.ranking.helpers.NameInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

/**
 *  An example of just another implementation of 'Ranking' class
 */
@Slf4j
@Service
@Qualifier("RankingWithoutOffset")
public class RankingWithoutOffset extends Ranking{

    @Override
    public BigInteger calculateSum(List<NameInfo> names){
        log.info("Ranking without offset calculation");
        BigInteger result = new BigInteger("0");
        for(NameInfo nameInfo: names){
            String name = nameInfo.name;
            Integer nameSum = name.chars().reduce(0,
                (part, ch) -> {
                    Integer letterIndex = this.charMap.get((char)ch);
                    Integer newIndex = letterIndex == null ? 0 : letterIndex;
                    return part + newIndex;
                });
            result = result.add(BigInteger.valueOf(nameSum.longValue()));
        }
        log.info("Result (Ranking without offset) -> " + result);
        return result;
    }

    @Override
    public String info() {
        return "An alternative implementation derived from 'Ranking' service \n" +
                "Avoids mutiplying the sum of letters of a given word with \n" +
                "index / offset";
    }

}
