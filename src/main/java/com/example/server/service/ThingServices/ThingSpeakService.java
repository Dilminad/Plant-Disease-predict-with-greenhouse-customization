package com.example.server.service.ThingServices;

import com.example.server.dto.Feed;
import com.example.server.dto.ThingSpeakResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ThingSpeakService {

    @Value("${thingspeak.api.url}")
    private String apiUrl;

    @Value("${thingspeak.channel.id}")
    private String channelId;

    @Value("${thingspeak.api.key.read}")
    private String readApiKey;

    private final RestTemplate restTemplate;

    public ThingSpeakService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Feed getLatestSensorData() {
        // Construct the URL to get the last entry from the channel feed
        String url = String.format("%s/channels/%s/feeds.json?api_key=%s&results=1",
                apiUrl, channelId, readApiKey);

        ThingSpeakResponse response = restTemplate.getForObject(url, ThingSpeakResponse.class);

        if (response != null && response.getFeeds() != null && !response.getFeeds().isEmpty()) {
            return response.getFeeds().get(0); // Return the most recent feed
        }
        
        return null; // Or throw an exception
    }
}