package com.example.proyecto;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class userForm extends JFrame implements ActionListener {
    private JLabel lblNombre;
    private JTextField nombre;
    private JButton btnAgregar;
    private JPanel userPanel;
    private JComboBox<String> usuarios;
    private JButton selecciona;
    private JMenu userMen;

    public userForm(JMenu userTop){
        this.setContentPane(userPanel);
        userMen = userTop;
        this.setTitle("Insertar Usuario");
        this.setSize(400,400);
        this.btnAgregar.addActionListener(this);
        this.selecciona.addActionListener(this);
        this.setVisible(true);
        if(ManagerMovie.getInstance().users == null) {
            ManagerMovie.getInstance().users = new ArrayList<>();
        } else {
            ManagerMovie.getInstance().users.forEach(us -> {
                usuarios.addItem(us.userName);
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAgregar && nombre.getText().trim().length() > 0) {
            var user = new User(nombre.getText().trim());
            ManagerMovie.getInstance().users.add(user);
            ManagerMovie.getInstance().users.forEach(us -> {
                usuarios.addItem(us.userName);
            });

        }
        if (e.getSource() == selecciona) {
            ManagerMovie.getInstance().selectedUser = ManagerMovie.getInstance()
                    .users.stream().filter( u -> u.userName == usuarios
                            .getSelectedItem().toString()).findFirst().orElse(null);
            if (ManagerMovie.getInstance().selectedUser != null) {
                userMen.setText(Objects.requireNonNull(ManagerMovie.getInstance().selectedUser.userName));
                userMen.repaint();
                userMen.revalidate();
            }

        }
    }
}
