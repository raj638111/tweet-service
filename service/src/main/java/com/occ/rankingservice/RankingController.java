package com.occ.rankingservice;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import com.occ.rankingservice.impl.Ranking;
import com.occ.rankingservice.utils.NameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class RankingController {

    private Map<String, Ranking> serviceMap = new HashMap<String, Ranking>();

    @Autowired // Inject all Services implementations
    RankingController(List<Ranking> services) {
        services.forEach(x -> {
             serviceMap.put(x.getClass().getName(), x);
        });
        log.info("No of Ranking Service / Algorithm available -> " + services.size());
    }

    @GetMapping("/ls")
    public List<String> getServices() {
        List<String> services = new LinkedList<String>();
        serviceMap.forEach((key, value) -> services.add(key));
        log.info("Returning the list of available services");
        return services;
    }

    @PostMapping("/rank")
    public BigInteger gpost(@RequestParam("file") MultipartFile file,
        @RequestParam(value = "service", defaultValue = "com.occ.rankingservice.impl.Ranking") String service,
        @RequestParam(value = "consider", defaultValue = "firstname") String consider,
        @RequestParam(value = "descending", defaultValue = "false") Boolean descending)
        throws IOException {
        String fName = file.getOriginalFilename();
        log.info("fName -> " + fName);
        log.info("service -> " + service);
        log.info("consider -> " + consider);
        log.info("descending -> " + descending);
        String str = new String(file.getBytes());
        Ranking serviceImpl = serviceMap.get(service);
        List<NameInfo> names = serviceImpl.parseAndSortNames(str, consider, descending);
        BigInteger sum = serviceImpl.calculateSum(names);
        return sum;
    }


}