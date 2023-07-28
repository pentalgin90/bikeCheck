package com.bragin.bike_theft_check.services.schedulers;

import com.bragin.bike_theft_check.services.BikeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class RunnerParse {

    private final BikeService bikeService;
    private final List<Parser> tasks = new ArrayList<>();

    public RunnerParse(BikeService bikeService, RssParser rssParser, DviraciuregistrasParser dviraciuregistrasParser) {
        this.bikeService = bikeService;
        tasks.add(rssParser);
        tasks.add(dviraciuregistrasParser);
    }

    @Scheduled(cron = "${cron.parser}")
    public void start() {
        tasks.stream()
                .map(Parser::parse)
                .flatMap(Collection::stream)
                .filter(bike -> !bikeService.ifExist(bike.getFrameNumber()))
                .forEach(bike -> {
                    try {
                        bikeService.createReport(bike);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
