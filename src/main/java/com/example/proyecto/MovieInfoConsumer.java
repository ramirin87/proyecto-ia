package com.example.proyecto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MovieInfoConsumer {
    private RestTemplate restTemplate;

    public String getProductAsJson(String title) {
        RestTemplate restTemplate = new RestTemplate();

        String resourceUrl
                = "https://api.themoviedb.org/3/search/movie?api_key=15d2ea6d0dc1d476efbca3eba2b9bbfb&query="+title;

        // Fetch JSON response as String wrapped in ResponseEntity
        ResponseEntity<String> response
                = restTemplate.getForEntity(resourceUrl, String.class);

        try {
            var info = new ObjectMapper().readValue(response.getBody(), MovieInfo.class);

            if (!info.getResults().isEmpty()){
                if (info.getResults().get(0).getPosterPath() == null){
                    return "";
                }
                return "http://image.tmdb.org/t/p/w500"+ info.getResults().get(0).getPosterPath();
            } else {
                return "";
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
        return "";
    }
}
