package com.occ.ranking.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import com.occ.ranking.constants.NameSelection;
import com.occ.ranking.model.ServiceInfo;
import com.occ.ranking.service.Ranking;
import com.occ.ranking.model.NameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class RankingController {

    private Map<String, Ranking> serviceMap = new HashMap<String, Ranking>();

    @Autowired // Inject all 'Ranking' implementations
    RankingController(List<Ranking> services) {
        services.forEach(x -> {
             serviceMap.put(x.getClass().getName(), x);
        });
        log.info("No of Ranking Service / Algorithm available -> " + services.size());
    }

    @GetMapping("/ls")
    public List<ServiceInfo> getServicesNew() {
        List<ServiceInfo> services = new LinkedList<ServiceInfo>();
        serviceMap.forEach((key, value) -> {
            services.add(new ServiceInfo(key, value.info()));
        });
        log.info("Returning the list of available services");
        return services;
    }

    @PostMapping("/rank")
    public BigInteger gpost(@RequestParam("file") MultipartFile file,
        @RequestParam(value = "service", defaultValue = "com.occ.ranking.service.Ranking") String service,
        @RequestParam(value = "nameselect", defaultValue = "FIRST_NAME") NameSelection nameselect,
        @RequestParam(value = "descending", defaultValue = "false") Boolean descending)
            throws IOException, InterruptedException {
        String fName = file.getOriginalFilename();
        log.info("fName -> " + fName);
        log.info("service -> " + service);
        log.info("nameselect -> " + nameselect);
        log.info("descending -> " + descending);
        String str = new String(file.getBytes());
        Ranking serviceImpl = serviceMap.get(service);
        List<NameInfo> names = serviceImpl.parseAndSortNames(str, nameselect, descending);
        BigInteger sum = serviceImpl.calculateSum(names);
        return sum;
    }


}