package com.bragin.bike_theft_check.services.schedulers;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.services.BikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class ParserWeb {
    private final BikeService bikeService;

    private static final String URL = "https://www.dviraciuregistras.lt";
    private static final String URL_FIRST = "https://www.dviraciuregistras.lt/pavogti-dviraciai";
    private static final String URL_ITERATION = "/puslapis-";
    @Scheduled(cron = "${cron.parser}")
    public void updateDataFromWebSite() {
        try {
            String url = URL_FIRST;
            Document doc = Jsoup.connect(url).get();
            List<BikeDto> dtos = parsePage(url);
            int countPage = getCountPage(doc);
            for (int i = 2; i < countPage; i++) {
                String urlLink = URL_FIRST + URL_ITERATION + i;
                dtos.addAll(parsePage(urlLink));
            }
            log.info("Dviraciuregistras.lt was parsed");
            dtos.stream()
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
        } catch (IOException e) {
            log.error("Problem with parsing Dviraciuregistras.lt: %s", e.getMessage());
        }
    }

    private List<BikeDto> parsePage(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select(".my_stuff");
        return elements.stream()
                        .map(element -> {
                            BikeDto bikeDto = new BikeDto();
                            bikeDto.setUserId(2L);
                            Elements h3 = element.select("h3");
                            String link = h3.select("a").get(0).attributes().get("href");
                            String[] split = element.html().split("<br><strong>Rėmo numeris:</strong>");
                            String frameNumber = split[1].split("\\s")[1];
                            bikeDto.setFrameNumber(frameNumber);
                            bikeDto.setLink(URL + link);
                            return bikeDto;
                        })
                        .collect(Collectors.toList());
    }

    public int getCountPage(Document doc) {
        Elements lastButton = doc.select(".last");
        Elements href = lastButton.select("a");
        Attributes attributes = href.first().attributes();
        String link = attributes.get("href");
        String[] split = link.split("-");
        return Integer.valueOf(split[2]);
    }
}
