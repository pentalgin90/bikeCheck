package com.bragin.bike_theft_check.services.schedulers;

import com.bragin.bike_theft_check.configuration.props.RssProperties;
import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.services.BikeService;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
@Slf4j
@Service
@EnableConfigurationProperties(RssProperties.class)
@RequiredArgsConstructor
public class RssReader {

    private final RssProperties rssProperties;
    private final BikeService bikeService;

    @Scheduled(cron = "${cron.rss}")
    public void updateDataFromRssChannel() {
        try {
            URL feedSource = new URL(rssProperties.getBaseUrl());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));
            List<SyndEntryImpl> entries = feed.getEntries();
            log.info("Rss chanel was parsed");
            entries.stream()
                    .map(e -> {
                        BikeDto bikeDto = new BikeDto();
                        bikeDto.setUserId(1L);
                        String frameNumber = null;
                        String[] arrays = e.getDescription().getValue().split("Номер рамы");
                        if (arrays.length > 1) {
                            String[] split = arrays[1].split("</div>");
                            if (split.length > 1) {
                                bikeDto.setFrameNumber(split[1].replaceAll("\\s", ""));
                            }
                        }
                        bikeDto.setLink(e.getLink());
                        return bikeDto;
                    })
                    .filter(bike -> Strings.isNotBlank(bike.getFrameNumber()))
                    .filter(bike -> bike.getFrameNumber().length() > 1)
                    .filter(bike -> !bikeService.ifExist(bike.getFrameNumber()))
                    .forEach(bike -> {
                        try {
                            bikeService.createReport(bike);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (FeedException | IOException e) {
            log.error("Problem with RSS chanel: %s", e.getMessage());
        }
    }
}
