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
public class Interfaz {
   private JPanel mainPanel;
   private JButton profesorButton;
   private JButton alumnoButton;

    public Interfaz() {
	this.mainPanel = new JPanel();
	this.profesorButton = new JButton("Profesor");
	this.alumnoButton = new JButton("Alumno");
	
	mainPanel.add(profesorButton);
	mainPanel.add(alumnoButton);
	       // Acción para el botón "Profesor"
        profesorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // Al hacer clic, abre una nueva ventana
               JFrame profesorFrame = new JFrame("Ventana Profesor");
               profesorFrame.setSize(400, 200); // Tamaño de la nueva ventana
               profesorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
               profesorFrame.setVisible(true);
	       
            }
        });
    }
    

   public static void main(String[] args) {
       JFrame frame = new JFrame("Interfaz");
       frame.setContentPane(new Interfaz().mainPanel);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(400, 200);
//       frame.pack();//vamos a usar el mismo tamaño para todas las ventanas es mas facil que con el pack
       frame.setVisible(true);
   }
}

