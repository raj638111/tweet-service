package com.occ.ranking.unittests.service;
import com.occ.ranking.constants.NameSelection;
import com.occ.ranking.service.Ranking;
import com.occ.ranking.model.NameInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class RankingTests {

    Ranking ranking = new Ranking();

    @Test
    public void enrichName(){
        assert(ranking.enrichName("fname lname",
                NameSelection.FIRST_NAME).equals("fname"));
        assert(ranking.enrichName("fname lname",
                NameSelection.LAST_NAME).equals("lname"));
        assert(ranking.enrichName("fname lname",
                NameSelection.BOTH).equals("fname lname"));
        assert(ranking.enrichName("fname",
                NameSelection.LAST_NAME).equals(""));
    }

    @Test
    public void calculateSumSpecialCharacters() {
        List<NameInfo> names = new LinkedList<NameInfo>();
        names.add(new NameInfo("first'x", 1L));
        names.add(new NameInfo("second`x", 2L));
        assert (ranking.calculateSum(names).toString().equals("264"));
    }
}
