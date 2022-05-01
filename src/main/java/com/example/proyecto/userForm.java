package com.example.proyecto;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class userForm extends JFrame implements ActionListener {
    private JLabel lblNombre;
    private JTextField nombre;
    private JButton btnAgregar;
    private JButton btnSalir;
    private JPanel userPanel;

    public userForm(){
        this.setContentPane(userPanel);
        this.setTitle("Insertar Usuario");
        this.setSize(400,400);
        this.setVisible(true);
        if(ManagerMovie.getInstance().users == null) {
            ManagerMovie.getInstance().users = new ArrayList<>();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAgregar && nombre.getText().trim().length() > 0) {
            var user = new User(nombre.getText().trim());
            ManagerMovie.getInstance().users.add(user);
        }
    }
}
