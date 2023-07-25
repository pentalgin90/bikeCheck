package com.bragin.bike_theft_check.configuration.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rss")
public class RssProperties {
    private String baseUrl;
}
