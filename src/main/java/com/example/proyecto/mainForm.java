package com.example.proyecto;

import com.intellij.uiDesigner.core.GridConstraints;
import com.opencsv.CSVReader;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class mainForm extends JFrame implements ActionListener {
    MovieInfoConsumer movieRest;
    private JPanel mainPanel;
    private JPanel movie1;
    private JLabel title1;
    private JLabel genres1;
    private JPanel movie2;
    private JLabel title2;
    private JLabel genres2;
    private JPanel movie3;
    private JLabel title3;
    private JLabel genres3;
    private JPanel posterArea1;
    private JPanel posterArea2;
    private JPanel posterArea3;
    private JComboBox<String> puntos1;
    private JComboBox<String> puntos2;
    private JComboBox<String> puntos3;
    private JMenuItem crearUsuario;
    private JMenuItem buscar;
    private JMenuBar topBar;
    private JMenu userMenu;

    public static void main(String[] args) {
        mainForm form = new mainForm();
    }

    public mainForm() {
        movieRest = new MovieInfoConsumer();
        setContentPane(mainPanel);
        setTitle("Proyecto IA Grupo 4");
        setSize(1000,600);
        movie1.setSize(300,550);
        topBar = new JMenuBar();
        userMenu = new JMenu("Usuarios");
        crearUsuario = new JMenuItem("Crear usuario");
        buscar = new JMenuItem("Buscar");
        crearUsuario.addActionListener(this);
        buscar.addActionListener(this);
        userMenu.add(crearUsuario);
        userMenu.add(buscar);
        topBar.add(userMenu);
        this.setJMenuBar(topBar);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true); JFileChooser chooser = new JFileChooser();
        int responseChoose = chooser.showOpenDialog(null);
        if (responseChoose == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            try {
                loadData(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    void loadData(String filePath) throws FileNotFoundException, IOException {
        ArrayList<Movie> data = new ArrayList<>();
        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> lines = reader.readAll();
        for(int i = 1; i<lines.size(); i++){
            data.add(new Movie(lines.get(i), i));
        }
        var sort = new ArrayList<Movie>();
        for(int i = 0; i < data.size();i++){
            var temp = data.get(i);
            if(sort.stream().filter(x -> x.title.equals(temp.title)).count()==0){
                sort.add(temp);
            }
        }

        ManagerMovie.getInstance().movies = sort.stream().sorted(Comparator
                .comparing(Movie::getIMDB_Score).reversed())
                .toList();
        System.out.println(Utils.BuildCSV(ManagerMovie.getInstance().movies));
        LoadRandomMovies();
    }

    void LoadRandomMovies() {
        Random random = new Random();
        int i = 0;
        while (i < 3) {
            int index = random.nextInt(25);
            var movie = ManagerMovie.getInstance().movies.get(index);
            switch (i){
                case 0: {

                    var path = movieRest.getProductAsJson(movie.title);
                    try {
                        setMovieArea(path,posterArea1,puntos1,title1,genres1, movie);

                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                    break;
                }
                case 1: {
                    var path = movieRest.getProductAsJson(movie.title);
                    try {
                        setMovieArea(path,posterArea2,puntos2,title2,genres2,movie);

                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                    break;
                }
                case 2: {
                    var path = movieRest.getProductAsJson(movie.title);
                    try {
                        setMovieArea(path,posterArea3,puntos3,title3,genres3,movie);

                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                    break;
                }
            }
            i++;
        }
    }

    private void setMovieArea(String path, JPanel panel, JComboBox<String> combo, JLabel title, JLabel genres, Movie movie) throws IOException {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        title.setText("Titulo: " + movie.title);
        genres.setText("Generos: " + movie.genres.toString());
        if (path != ""){
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

        combo.addItem("<Seleccione un valor>");
        combo.addItem("0");
        combo.addItem("1");
        combo.addItem("2");
        combo.addItem("3");
        combo.addItem("4");
        combo.addItem("5");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == crearUsuario) {
            System.out.println("Crear nuevo usuario");
            new userForm();
        }

        if (e.getSource() == buscar) {
            System.out.println("Buscar");
            new searchForm();
        }
    }
}
