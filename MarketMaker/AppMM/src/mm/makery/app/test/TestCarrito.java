package mm.makery.app.test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class TestCarrito {
	
	double precioTotalEsperado=36.0;
	static double precioTest=0.0;
	int idClient=254;
	//Prueba para comprobar el precio total de los productos del cliente con id 254. Precio esperado: 36.0
	
	public void getPrecioTotal() {
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");	
			//Calculo el total del carrito del cliente
				String productsQuery = "SELECT SUM(cantidad*precio_unitario) AS preciototal from carrito where id_cliente="+idClient;
				PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
				ResultSet totalSet = totalSta.executeQuery();
				if(totalSet.next()) {
					precioTest = Double.parseDouble(totalSet.getString("preciototal"));
					System.out.println(precioTest);
				}
				totalSet.close();
				totalSta.close();
				conexion.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//*****************************************************************TESTS************************************************************************************
	@Test
	public void precioTest() {
		getPrecioTotal();
		assertEquals(precioTotalEsperado, precioTest,0.001); //Usamos un delta (margen de error)
		
	}
	
	/*
	public static void main(String[]args) {
		try {
			System.out.println(precioTest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
}
