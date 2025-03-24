/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ernesto;
import java.sql.*;
import javax.swing.JFrame;

/*
 *
 * @author ifeito-m
 */
public class Ernesto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

	try {
	    //charge MariaDB's driver
	    Class.forName("org.mariadb.jdbc.Driver");
	    //clase
//	    Connection connection = DriverManager.getConnection("jdbc:mariadb://10.227.189.195:3307/alberto_isma", "root", "alumno");

	    //local:
	    Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost/ernesto?socket=/run/mysqld/mysqld.sock", "root", "8001");
	    //Querys consulta = new Querys();
	    
	//implementar interfaz tras la conexión a mariaDB    
	    JFrame frame = new Interfaz();

	    
//	    consulta.makeQuerys(0);//esta mal el int de dentro de la función
	    //create statement (sentencia)
	    Statement stmt = connection.createStatement();
	    //save query's text on a str (after will exe it)
	    String query = "SELECT * FROM Ciclos";
            ResultSet rs = stmt.executeQuery(query);
	    
//	    printear tablas
	    System.out.println("CicloID  - Ciclos");
	    while (rs.next()){
		System.out.println(rs.getString("CicloID")+"        - "+rs.getString("NombreCiclo"));
	    }
	    
	    rs.close();
	    stmt.close();
	    connection.close();
	} catch (ClassNotFoundException e){
	    System.out.println("Error: MariaDB's driver not found");
	    e.printStackTrace();
	} catch (SQLException e) {
	    System.out.println("Error: connection with Database failed");
            e.printStackTrace();
        }
	
    }
    
}
