package com.example.proyecto;

import com.intellij.uiDesigner.core.GridConstraints;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Objects;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class searchForm extends JFrame implements ActionListener {
    private final MovieInfoConsumer movieRest;
    private final MotorConsumer motorConsumer;
    private Movie selectedMovie;
    private JPanel searchPanel;
    private JComboBox<String> lista;
    private JButton buscar;
    private JLabel title1;
    private JLabel genres1;
    private JPanel posterArea1;
    private JComboBox<String> puntos1;
    private JLabel director;
    private JLabel rate;
    private JButton enviarPuntos;

    public searchForm(){
        this.setContentPane(searchPanel);
        motorConsumer = new MotorConsumer();
        movieRest = new MovieInfoConsumer();
        this.setTitle("Buscar");
        this.setSize(650,600);
        posterArea1.setSize(180,400);
        this.enviarPuntos.addActionListener(this);
        this.setVisible(true);
        this.buscar.addActionListener(this);
        ManagerMovie.getInstance().movies.stream().sorted(Comparator
                        .comparing(Movie::getTitleCompare)).toList().forEach(movie -> {
            lista.addItem(movie.title);
        });
        AutoCompleteDecorator.decorate(lista);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buscar){
            var movie = ManagerMovie.getInstance().movies.stream()
                    .filter( x -> x.title.equals(lista.getSelectedItem())).findFirst().orElse(null);
            if (movie != null) {
                try {
                    selectedMovie = movie;
                    setMovieArea(posterArea1, puntos1, title1, genres1, director, rate, movie);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (e.getSource() == enviarPuntos) {
            if  (ManagerMovie.getInstance().selectedUser != null) {
                System.out.println("Mande review");
                if (puntos1.getSelectedItem() == "like") {
                    motorConsumer.sendReview(ManagerMovie.getInstance().selectedUser
                        .userName, selectedMovie.id, true);
                }
                if (puntos1.getSelectedItem() == "dislike") {
                    motorConsumer.sendReview(ManagerMovie.getInstance().selectedUser
                        .userName, selectedMovie.id, true);
                }
            }
        }
    }

    private void setMovieArea(JPanel panel, JComboBox<String> combo
            , JLabel title, JLabel genres, JLabel director, JLabel rate, Movie movie) throws IOException {
        var path = movieRest.getProductAsJson(movie.title);
        try {
            panel.removeAll();
            panel.revalidate();
            panel.repaint();
            title.setText("Titulo: " + movie.title);
            genres.setText("Generos: " + movie.genres.toString());
            director.setText("Director: " + movie.director);
            rate.setText("IMDB Rank: " + movie.getIMDB_Score());
            if (!Objects.equals(path, "")){
                URL url = new URL(path);
                System.out.println(url);
                BufferedImage image = ImageIO.read(url);
                Image dimg = image.getScaledInstance(posterArea1.getWidth() - 10
                        , posterArea1.getHeight() - 10,
                        Image.SCALE_SMOOTH);

                JLabel label = new JLabel(new ImageIcon(dimg));
                panel.add(label, new GridConstraints());


                label.setVisible(true);
            }

            combo.removeAllItems();
            combo.addItem("<Seleccione un valor>");
            combo.addItem("like");
            combo.addItem("dislike");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
