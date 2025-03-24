/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;

/**
 *
 * @author ifeito-m
 */
public class Usuario {
    private int	    UsuarioID = 0;
    private String  NombreUsuario;
    private String  Contra;
    private UserType  TipoUsuario;

    public Usuario(){}
    
    public Usuario(String NombreUsuario, UserType TipoUsuario) throws NoSuchAlgorithmException {
	this.NombreUsuario = NombreUsuario;
	this.TipoUsuario = TipoUsuario;//enlazarlo con un boton de eleccion para guararlo directamne
	this.Contra = generatePasswd();
    }

    public int getUsuarioID() {
	return UsuarioID;
    }

    public void setUsuarioID(int UsuarioID) {
	this.UsuarioID = UsuarioID;
    }
    
    public String getNombreUsuario() {
	return NombreUsuario;
    }

    public void setNombreUsuario(String NombreUsuario) {
	this.NombreUsuario = NombreUsuario;
    }

    public String getContra() {
	return Contra;
    }

    public void setContra(int Contra) throws NoSuchAlgorithmException {
	this.Contra = changePasswd(Contra);
    }

    public String getTipoUsuario() {
	return TipoUsuario.toString();
    }

    public void setTipoUsuario(UserType TipoUsuario) {
	this.TipoUsuario = TipoUsuario;
    }

    private String  generatePasswd() throws NoSuchAlgorithmException{
	int passwd = (int)(Math.random()*10000);// 99999 - 0;
	String hash = dm5(String.valueOf(passwd));//enviar contra cuando se establece a un fd
	return hash;
    }
    
    private String  changePasswd(int passwd) throws NoSuchAlgorithmException{
	String hash = dm5(String.valueOf(passwd));//enviar contra cuando se cambia a un fd
	return hash;
    }
    
    //aplicar condición de la contraseña (podemos ponerlo en esta funcion la redirección al fd)
    private String dm5(String passwd) throws NoSuchAlgorithmException{
	try{
	    //genera los hases segun un algoritmo/manera de aplicarlo
	    MessageDigest algorithm = MessageDigest.getInstance("MD5");
	    algorithm.update(passwd.getBytes());
	    //mas manejable para codificar en este caso
	    //(se puede sobreescribir facil y sin tamaño fijo)
	    byte[] b = algorithm.digest();
	    StringBuilder str = new StringBuilder();
	    //for each para guardar cada codificar cada nbr 1x1 con el algoritmo
	    for (byte c : b){
		str.append(String.format("%05x", b));//2digitos por numero
	    }
	    
	    return str.toString();//casteo a cadena simle
	}catch (NoSuchAlgorithmException e){
	    throw new RuntimeException("Error con el algoritmo de encriptado (DM5)");
	}
	
    }
}
