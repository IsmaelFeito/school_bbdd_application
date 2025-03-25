/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author ifeito-m y albertomh
 */
public class Interfaz extends JFrame{
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    public Interfaz() {
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setSize(500, 300);
       setLocationRelativeTo(null);

       cardLayout = new CardLayout();
       mainPanel = new JPanel(cardLayout);

       ButtonsInterface buttonsPanel = new ButtonsInterface(cardLayout, mainPanel);
       mainPanel.add(buttonsPanel, "buttonsPanel");
       
       setContentPane(mainPanel);
       
       cardLayout.show(mainPanel, "buttonsPanel");
       setVisible(true);
       
    }
}

