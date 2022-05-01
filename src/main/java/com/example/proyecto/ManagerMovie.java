package com.example.proyecto;

import java.util.List;

public class ManagerMovie {

    private static ManagerMovie instance;
    public List<Movie> movies;
    public List<User> users;
    public User selectedUser;

    private ManagerMovie(){
    }

    public static ManagerMovie getInstance(){
        if(instance == null) {
            synchronized (ManagerMovie.class){
                if(instance == null) {
                    instance = new ManagerMovie();
                }
            }
        }
        return instance;
    }
}
