/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author ifeito-m
 */
public class Querys {

    public Querys() {}
    
    
    //debe recibir una opción de la interfaz ya que va a ir todo con botones
    //cada boton pasa un numero distinto y coge la sentencia correspondiente
    public String makeQuerys(int chosen) throws NoSuchElementException {
	String result = "";
	switch (chosen){
	    case 1:
		return result = "SELECT";
	    case 2:
		return result = "UPDATE";
	    case 3:
		return result = "DELETE";
	    case 4:
		return result = "INSERT";
	    default:
		throw new NoSuchElementException("Error al recibir el imput");
	}
	
    }
}
