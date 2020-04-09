package com.occ.rankingservice;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import com.occ.rankingservice.impl.Ranking;
import com.occ.rankingservice.utils.NameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/services")
    public List<String> getServices() {
        List<String> services = new LinkedList<String>();
        serviceMap.forEach((key, value) -> services.add(key));
        return services;
    }

    @PostMapping("/ranking")
    public BigInteger gpost(@RequestParam("file") MultipartFile file,
        @RequestParam(value = "service", defaultValue = "com.occ.rankingservice.impl.Ranking") String service,
        @RequestParam(value = "consider", defaultValue = "firstname") String consider,
        @RequestParam(value = "naturalsort", defaultValue = "true") Boolean naturalsort)
        throws IOException {
        String fName = file.getOriginalFilename();
        log.info("fName -> " + fName);
        log.info("service -> " + service);
        log.info("consider -> " + consider);
        log.info("naturalsort -> " + naturalsort);
        String str = new String(file.getBytes());
        Ranking serviceImpl = serviceMap.get(service);
        List<NameInfo> names = serviceImpl.parseAndSortNames(str, consider, naturalsort);
        BigInteger sum = serviceImpl.calculateSum(names);
        return sum;
    }


}