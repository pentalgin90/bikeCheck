package com.bragin.bike_theft_check.services.schedulers;

import com.bragin.bike_theft_check.configuration.props.RssProperties;
import com.bragin.bike_theft_check.dto.BikeDto;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableConfigurationProperties(RssProperties.class)
@RequiredArgsConstructor
public class RssParser implements Parser {

    private final RssProperties rssProperties;

    private static BikeDto apply(SyndEntryImpl e) {
        BikeDto bikeDto = new BikeDto();
        bikeDto.setUserId(1L);
        String[] arrays = e.getDescription().getValue().split("Номер рамы");
        if (arrays.length > 1) {
            String[] split = arrays[1].split("</div>");
            if (split.length > 1) {
                bikeDto.setFrameNumber(split[1].replaceAll("\\s", ""));
            }
        }
        bikeDto.setLink(e.getLink());
        return bikeDto;
    }

    @Override
    public List<BikeDto> parse() {
        try {
            URL feedSource = new URL(rssProperties.getBaseUrl());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));
            List<SyndEntryImpl> entries = feed.getEntries();
            log.info("Rss chanel was parsed");
            return entries.stream()
                    .map(RssParser::apply)
                    .filter(bike -> Strings.isNotBlank(bike.getFrameNumber()))
                    .filter(bike -> bike.getFrameNumber().length() > 1)
                    .collect(Collectors.toList());
        } catch (FeedException | IOException e) {
            log.error("Problem with RSS chanel: %s", e.getMessage());
            return new ArrayList<>();
        }
    }
}
