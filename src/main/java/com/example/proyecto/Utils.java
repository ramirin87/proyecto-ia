package com.example.proyecto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Utils {

    public static final char CSV_SEPARATOR = ','; // it could be a comma or a semi colon

    public static String BuildCSV(List<Movie> list) {

        try {
            var fileWriter = new FileWriter("test.csv");
            BufferedWriter writer = new BufferedWriter(fileWriter);
            list.forEach(movie -> {
                try {
                    writer.append(movie.id.toString()).append(CSV_SEPARATOR)
                            .append(movie.title.replace(",","").trim()).append(CSV_SEPARATOR)
                            .append(movie.color).append(CSV_SEPARATOR)
                            .append(movie.plot_keywords.toString().replace(",","-")
                                    .replace("[","").replace(" ", "")
                                    .replace("]","")).append(CSV_SEPARATOR)
                            .append(movie.genres.toString().replace(",","-")
                                    .replace("[","").replace(" ", "")
                                    .replace("]","")).append(CSV_SEPARATOR)
                            .append(movie.director).append(CSV_SEPARATOR)
                            .append(movie.critics_reviews.toString()).append(CSV_SEPARATOR)
                            .append(movie.Actor1).append(CSV_SEPARATOR)
                            .append(movie.Actor2).append(CSV_SEPARATOR)
                            .append(Double.toString(movie.getIMDB_Score())).append(CSV_SEPARATOR)
                            .append(movie.year).append(System.lineSeparator());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return new File("test.csv").getAbsolutePath();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

}
