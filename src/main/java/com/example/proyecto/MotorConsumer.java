package com.example.proyecto;

import com.example.proyecto.Response.RecommendResponse;
import com.example.proyecto.request.SendFileRequest;
import com.example.proyecto.request.SendReviewRequest;
import com.example.proyecto.request.SignInRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class MotorConsumer {
  private RestTemplate restTemplate;
  private String baseUrl;

  public MotorConsumer(){
    restTemplate = new RestTemplate();
    baseUrl = "http://localhost:5000";
  }

  public boolean sendNewUser(String userName){
    var endpoint = baseUrl + "/users/signin";
    var request = new SignInRequest(userName);

    ResponseEntity<String> response
        = restTemplate.postForEntity(endpoint,request, String.class);

    if (response.getBody().contains("User register correctly")){
      return true;
    } else {
      return false;
    }
  }

  public boolean uploadFile(FileSystemResource file){
    var endpoint = baseUrl + "/file";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> body
        = new LinkedMultiValueMap<>();
    body.add("file", file);

    HttpEntity<MultiValueMap<String, Object>> requestEntity
        = new HttpEntity<>(body, headers);

    ResponseEntity<String> response
        = restTemplate.postForEntity(endpoint,requestEntity, String.class);

    if (response.getBody().contains("File uploaded succesfully")){
      return true;
    } else {
      return false;
    }
  }

  public boolean sendReview(String userName, int movie, boolean like){
    var endpoint = baseUrl + "/users/addReview/" + userName;
    var request = new SendReviewRequest(movie, like);

    ResponseEntity<String> response
        = restTemplate.postForEntity(endpoint,request, String.class);

    if (response.getBody().contains("Review added")){
      return true;
    } else {
      return false;
    }
  }

  public List<Integer> getRecomendation(String user) {

    String resourceUrl
        = baseUrl + "/users/getRecomendation/"+ user;

    ResponseEntity<RecommendResponse> response
        = restTemplate.getForEntity(resourceUrl, RecommendResponse.class);

      if (response.getBody().data.isEmpty()){
        return new ArrayList<>();
        }
      else {
        return response.getBody().data;
      }
  }
}
