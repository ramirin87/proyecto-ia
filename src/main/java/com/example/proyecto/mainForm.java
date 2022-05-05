package com.example.proyecto;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

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
    MotorConsumer motorConsumer;
    List<Movie> selectedMovies;
    List<Integer> randomSelects;
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
    private JMenuItem recomend;
    private JMenuItem cargarRecomend;
    private JMenuBar topBar;
    private JMenu userMenu;
    private JMenu recomendacion;
    private JMenu buscarTop;
    private JMenu userTop;

    public static void main(String[] args) {
        mainForm form = new mainForm();
    }

    public mainForm() {
        movieRest = new MovieInfoConsumer();
        motorConsumer = new MotorConsumer();
        setContentPane(mainPanel);
        setTitle("Proyecto IA Grupo 4");
        setSize(1200,700);
        movie1.setSize(300,550);
        topBar = new JMenuBar();
        userMenu = new JMenu("Usuarios");
        buscarTop = new JMenu("Buscar");
        userTop = new JMenu("sin usuario");
        userTop.addSeparator();
        buscarTop.addActionListener(this);
        recomendacion = new JMenu("Motor");
        crearUsuario = new JMenuItem("Crear usuario");
        buscar = new JMenuItem("Buscar Título");
        recomend = new JMenuItem("Mandar Reviews");
        cargarRecomend = new JMenuItem("Cargar Recomendacion");
        cargarRecomend.addActionListener(this);
        recomend.addActionListener(this);
        crearUsuario.addActionListener(this);
        buscar.addActionListener(this);
        recomendacion.add(cargarRecomend);
        recomendacion.add(recomend);
        userMenu.add(crearUsuario);
        buscarTop.add(buscar);
        topBar.add(userMenu);
        topBar.add(buscarTop);
        topBar.add(recomendacion);
        topBar.add(Box.createHorizontalGlue());
        topBar.add(userTop);
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
        JOptionPane.showMessageDialog(mainPanel, "El Motor se realizó con Naive Bayes, " +
                "por lo que necesita al menos un like y un dislike para funcionar.",
            "Advertencia", JOptionPane.WARNING_MESSAGE);
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
        if(motorConsumer.uploadFile(Utils.BuildCSV(ManagerMovie.getInstance().movies))) {
            System.out.println("Cargo el archivo");
        }

        LoadRandomMovies();
    }

    void LoadRandomMovies() throws IOException {
        selectedMovies = new ArrayList<>();
        randomSelects = new ArrayList<>();
        Random random = new Random();
        int i = 0;
        while (i < 3) {
            int index = random.nextInt(25);
            if (!randomSelects.contains(index)) {
                randomSelects.add(index);
                var movie = ManagerMovie.getInstance().movies.get(index);
                switch (i) {
                    case 0: {
                        setMovieArea(posterArea1, puntos1, title1, genres1, movie);
                        break;
                    }
                    case 1: {
                        setMovieArea(posterArea2, puntos2, title2, genres2, movie);
                        break;
                    }
                    case 2: {
                        setMovieArea(posterArea3, puntos3, title3, genres3, movie);
                        break;
                    }
                }
                i++;
            }
        }
    }

    private void setMovieArea(JPanel panel, JComboBox<String> combo, JLabel title, JLabel genres, Movie movie) throws IOException {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        selectedMovies.add(movie);
        var path = movieRest.getProductAsJson(movie.title);
        if (!path.isEmpty()) {
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
            combo.removeAllItems();
            combo.addItem("<Seleccione un valor>");
            combo.addItem("like");
            combo.addItem("dislike");
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == crearUsuario) {
            System.out.println("Crear nuevo usuario");
            new userForm(this.userTop);
        }

        if (e.getSource() == buscar) {
            System.out.println("Buscar");
            new searchForm();
        }

        if (e.getSource() == recomend) {
            System.out.println("Mandar Recomendacion");
            mandarRecomendacion(puntos1,0);
            mandarRecomendacion(puntos2,1);
            mandarRecomendacion(puntos3,2);
        }

        if(e.getSource() == cargarRecomend) {
            if (ManagerMovie.getInstance().selectedUser != null) {
                var lista = motorConsumer
                    .getRecomendation(ManagerMovie.getInstance().selectedUser.userName);
                selectedMovies = new ArrayList<>();
                selectedMovies.add(ManagerMovie.getInstance().movies.stream()
                    .filter(movie -> movie.id.equals(lista.get(0))).findFirst().orElse(null));
                selectedMovies.add(ManagerMovie.getInstance().movies.stream()
                    .filter(movie -> movie.id.equals(lista.get(1))).findFirst().orElse(null));
                selectedMovies.add(ManagerMovie.getInstance().movies.stream()
                    .filter(movie -> movie.id.equals(lista.get(2))).findFirst().orElse(null));
                try {
                    setMovieArea(posterArea1,puntos1,title1,genres1, selectedMovies.get(0));
                    setMovieArea(posterArea2,puntos2,title2,genres2, selectedMovies.get(1));
                    setMovieArea(posterArea3,puntos3,title3,genres3, selectedMovies.get(2));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }




            } else {
                System.out.println("cargue random top 25");
                try {
                    LoadRandomMovies();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void mandarRecomendacion(JComboBox puntos, int order){
        if  (ManagerMovie.getInstance().selectedUser != null) {
            if (puntos.getSelectedItem() == "like") {
                motorConsumer.sendReview(ManagerMovie.getInstance().selectedUser
                    .userName, selectedMovies.get(order).id, true);
            }
            if (puntos.getSelectedItem() == "dislike") {
                motorConsumer.sendReview(ManagerMovie.getInstance().selectedUser
                    .userName, selectedMovies.get(order).id, false);
            }
        }
    }

}
