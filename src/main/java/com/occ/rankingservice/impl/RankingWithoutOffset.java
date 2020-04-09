package com.occ.rankingservice.impl;

import com.occ.rankingservice.utils.NameInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

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


}
