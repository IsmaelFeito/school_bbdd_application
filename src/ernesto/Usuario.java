/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author ifeito-m
 */
public class Usuario {
    private String  nombreUsuario;
    private String  contra;
    private String  tipoUsuario;
    private String  contraOriginal;
    
    public Usuario(){}
    
    public Usuario(String nombreUsuario, String contra, String tipoUsuario) throws NoSuchAlgorithmException {
        this.nombreUsuario = nombreUsuario;
        this.contraOriginal = generatePasswd();
        this.contra = md5(contraOriginal);
        this.tipoUsuario = tipoUsuario;
    }
    
    public Usuario(String NombreUsuario, String TipoUsuario) throws NoSuchAlgorithmException {
	this.nombreUsuario = NombreUsuario;
	this.tipoUsuario = TipoUsuario;//enlazarlo con un boton de eleccion para guararlo directamne
	this.contra = generatePasswd();
    }

    public String getNombreUsuario() {
	return nombreUsuario;
    }

    public void setNombreUsuario(String NombreUsuario) {
	this.nombreUsuario = NombreUsuario;
    }

    public String getContra() {
	return contra;
    }

    public void setContra(int Contra) throws NoSuchAlgorithmException {
	this.contra = changePasswd(Contra);
    }

    public String getTipoUsuario() {
	return tipoUsuario.toString();
    }

    public void setTipoUsuario(String TipoUsuario) {
	this.tipoUsuario = TipoUsuario;
    }

    public static String  generatePasswd(){
	int passwd = (int)(Math.random()*10000);// 99999 - 0;
	String text = String.valueOf(passwd);//enviar contra cuando se establece a un fd
	return text;
    }
    
    private String  changePasswd(int passwd) throws NoSuchAlgorithmException{
	String hash = md5(String.valueOf(passwd));//enviar contra cuando se cambia a un fd
	return hash;
    }
    
    //aplicar condición de la contraseña (podemos ponerlo en esta funcion la redirección al fd)
    public static String md5(String passwd) throws NoSuchAlgorithmException{
	try{
	    //genera los hases segun un algoritmo/manera de aplicarlo
	    MessageDigest algorithm = MessageDigest.getInstance("MD5");

	    byte[] digest = algorithm.digest(passwd.getBytes());
	    
	    return bytesToHex(digest);//casteo a cadena simle
	}catch (NoSuchAlgorithmException e){
	    throw new RuntimeException("Error con el algoritmo de encriptado (DM5)");
	}
	
    }
    
    public static String bytesToHex(byte[] bytes) {
	StringBuilder hexString = new StringBuilder();
	for (byte b : bytes) {
	    hexString.append(String.format("%02x", b)); // Convierte cada byte en dos caracteres hexadecimales
	}
	return hexString.toString();
    }
    
    public static void registrarEnFichero(String usuario, String mensaje) {
    try {
        // Obtener la ruta del escritorio
        String desktopPath = System.getProperty("user.home") + "/Desktop/login.log";  

	// Escribir en el archivo
        FileWriter writer = new FileWriter(desktopPath, true);
        writer.write(mensaje + " - " + usuario + "\n");
        writer.close();
	} catch (IOException e) {
	    System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
	}
    }

}

