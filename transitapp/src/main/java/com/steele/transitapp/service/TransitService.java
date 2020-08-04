package com.steele.transitapp.service;

import java.util.Arrays;
import java.util.List;

import com.steele.transitapp.model.Bus;
import com.steele.transitapp.model.DistanceResponse;
import com.steele.transitapp.model.GeocodingResponse;
import com.steele.transitapp.model.Location;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransitService {

    @Value("${transit_url}")
    public String transitUrl;
	
    @Value("${geocoding_url}")
    public String geocodingUrl;
	
    @Value("${distance_url}")
    public String distanceUrl;
	
    @Value("${google_api_key}")
    public String googleApiKey;

    private List<Bus> getBuses(){
        RestTemplate restTemplate = new RestTemplate();
        Bus[] buses = restTemplate.getForObject(transitUrl, Bus[].class);
        return Arrays.asList(buses);
    }

    private Location getCoordinates(String description) {
        description = description.replace(" ", "+");
        String url = geocodingUrl + description + "+GA&key=" + googleApiKey;
        RestTemplate restTemplate = new RestTemplate();
        GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);
        return response.results.get(0).geometry.location;
    }

    private double getDistance(Location origin, Location destination) {
        String url = distanceUrl + "origins=" + origin.lat + "," + origin.lng 
        + "&destinations=" + destination.lat + "," + destination.lng + "&key=" + googleApiKey;
        RestTemplate restTemplate = new RestTemplate();
        DistanceResponse response = restTemplate.getForObject(url, DistanceResponse.class);
        return response.rows.get(0).elements.get(0).distance.value * 0.000621371;
    }
    
}