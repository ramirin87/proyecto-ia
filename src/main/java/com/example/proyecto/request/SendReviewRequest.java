package com.example.proyecto.request;

public class SendReviewRequest {
  public int movie;
  public boolean liked;

  public SendReviewRequest(int movie, boolean liked) {
    this.movie = movie;
    this.liked = liked;
  }
}
