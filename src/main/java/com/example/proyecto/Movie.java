package com.example.proyecto;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author panch
 */
public class Movie {

    /**
     * @return the likeliness
     */
    public double getLikeliness() {
        return likeliness;
    }

    /**
     * @param likeliness the likeliness to set
     */
    public void setLikeliness(double likeliness) {
        this.likeliness = likeliness;
    }

    /**
     * @return the unlikeliness
     */
    public double getUnlikeliness() {
        return unlikeliness;
    }

    /**
     * @param unlikeliness the unlikeliness to set
     */
    public void setUnlikeliness(double unlikeliness) {
        this.unlikeliness = unlikeliness;
    }

    /**
     * @return the IMDB_Score
     */
    public double getIMDB_Score() {
        return IMDB_Score;
    }

    public String getTitleCompare() { return title; }

   Integer id;
   String color;
   String title;
   List<String> plot_keywords;
   List<String> genres;
   String director;
   Integer critics_reviews;
   String Actor1;
   String Actor2;   
   private double IMDB_Score;
   Integer facebook_likes;
   String year;
   //prior probability cold start¿?
   double priorLikeliness = Math.log(0.5);
   double priorUnlikeliness = Math.log(0.5);
   //acumulated probability after cold start
   private double likeliness = 0.0;
   private double unlikeliness = 0.0;   
   
   public Movie(String[] data, int id){
       try {
           this.id = id;
           this.color = data[0].trim();
           this.director = data[1].trim();
           this.critics_reviews = data[2].trim().equals("")? 0: Integer.parseInt(data[2].trim());
           this.Actor2 = data[6].trim();
           this.Actor1 = data[10].trim();
           this.genres = new ArrayList<>();
           this.genres.addAll(Arrays.asList(data[9].replace("|", ",").split(",")));
           this.title = data[11].substring(0,data[11].length()-1).trim();
           this.plot_keywords = new ArrayList<>();
           this.plot_keywords.addAll(Arrays.asList(data[16].replace("|", ",").split(",")));
           this.year =  data[23].trim();
           this.IMDB_Score = data[25].trim().equals("")? 0.0: Double.parseDouble(data[25].trim());
           this.facebook_likes = data[27].trim().equals("")? 0: Integer.parseInt(data[27].trim());
       } catch (Exception ex) {
           throw ex;
       }

   }    
   
   @Override
   public String toString(){
       StringBuilder src = new StringBuilder();
       src.append("Title: ");
       src.append(this.title);
       src.append(System.lineSeparator());
       src.append("Director: ");
       src.append(this.director);
       src.append(System.lineSeparator());
       src.append("Year: ");
       src.append(this.year);
       src.append(System.lineSeparator());
       src.append("IMDB Score: ");
       src.append(String.valueOf(this.getIMDB_Score()));
       src.append(System.lineSeparator());
       src.append("Genres: ");
       for(String gen : genres){
           src.append(gen);
           src.append(" |");
       }
       src.append("Actors: ");
       src.append(System.lineSeparator());
       src.append(this.Actor1);
       src.append(", ");
       src.append(this.Actor2);
       
       
       return src.toString();
   }
   
   
   public Movie(Movie data){
       this.Actor1 = data.Actor1;
       this.Actor2 = data.Actor2;
       this.IMDB_Score = data.IMDB_Score;
       this.color = data.color;
       this.critics_reviews = data.critics_reviews;
       this.director = data.director;
       this.facebook_likes = data.facebook_likes;
       this.likeliness = data.likeliness;
       this.title = data.title;
       this.unlikeliness = data.unlikeliness;
       this.priorLikeliness = data.priorLikeliness;
       this.priorUnlikeliness = data.priorUnlikeliness;
       this.genres = data.genres;
       this.plot_keywords = data.plot_keywords;
       this.year = data.year;
   }
   
  
}
