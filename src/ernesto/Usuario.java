/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ernesto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Objects;

/**
 *
 * @author ifeito-m
 */
public class Usuario {
    private int	    UsuarioID;
    private String  NombreUsuario;
    private String  Contra;
    private String  TipoUsuario;

    public Usuario(String NombreUsuario, String TipoUsuario) throws NoSuchAlgorithmException {
	this.NombreUsuario = NombreUsuario;
	this.TipoUsuario = TipoUsuario;
	this.Contra = generatePasswd();
    }

//    HashSet<String> passw = new HashSet(Contra);
    private String  generatePasswd() throws NoSuchAlgorithmException{
	int passwd = (int)(Math.random()*10000);
	String hash = dm5(String.valueOf(passwd));
	return hash;
    }
    
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
		str.append(String.format("%02x", b));//2digitos por numero
	    }
	    
	    return str.toString();//casteo a cadena simle
	}catch (NoSuchAlgorithmException e){
	    throw new RuntimeException("Error con el algoritmo de encriptado (DM5)");
	}
	
    }
}
