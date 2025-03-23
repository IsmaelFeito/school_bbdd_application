/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import javax.swing.*;
import java.awt.event.*;
/**
 *
 * @author ifeito-m y albertomh
 */
public class Interfaz extends JFrame{
    private ButtonsInterface buttonsPanel;

    public Interfaz() {
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setSize(400, 200);
       buttonsPanel = new ButtonsInterface();
       setContentPane(buttonsPanel);
       
       buttonsPanel.profesorButton.addActionListener(e -> buttonsPanel.openProfesor());
       buttonsPanel.alumnoButton.addActionListener(e -> buttonsPanel.openAlumno());
       
       setLocationRelativeTo(null);
       
       setVisible(true);

	
//	       // Acción para el botón "Salir"
//	salirButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//               System.exit(0);
//            }
//        });
    }
    

//   public static void main(String[] args) {
//   }
}

