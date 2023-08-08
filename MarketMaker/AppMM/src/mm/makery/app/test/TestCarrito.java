package mm.makery.app.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class TestCarrito {
	
	double precioTotalEsperado=36.0;
	static double precioTest=0.0;
	static Double precioTestNulo=0.0; //Variable para el tests que comprueba que el carrito es nulo
	int idClient=254; //Cliente del primer test
	int idClientPrecioNulo=208; //Cliente que no tiene nada en el carrito
	//Prueba para comprobar el precio total de los productos del cliente con id 254.
	public void getPrecioTotal() {
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");	
			//Calculo el total del carrito del cliente
				String productsQuery = "SELECT SUM(cantidad*precio_unitario) AS preciototal from carrito where id_cliente="+idClient;
				PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
				ResultSet totalSet = totalSta.executeQuery();
				if(totalSet.next()) {
					precioTest = Double.parseDouble(totalSet.getString("preciototal"));
			//		System.out.println(precioTest);
				}
				totalSet.close();
				totalSta.close();
				conexion.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getPrecioTotalNulo() {
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");	
			//Calculo el total del carrito del cliente
				String productsQuery = "SELECT SUM(cantidad*precio_unitario) AS preciototal from carrito where id_cliente="+idClientPrecioNulo;
				PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
				ResultSet totalSet = totalSta.executeQuery();
				if(totalSet.next()&&!(totalSet.getString("preciototal")==null)) {
					precioTestNulo= Double.parseDouble(totalSet.getString("preciototal"));
					
				}else {
					precioTestNulo=null;
				}
				totalSet.close();
				totalSta.close();
				conexion.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//En este metodo simulo una caida de la base de datos, donde en el test se maneja y captura la excepcion SQLException que se lanza
	public void getPrecioTotalError() throws SQLException {

		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");
		// Calculo el total del carrito del cliente
		String productsQuery = "SELECT SUM(cantidad*precio_unitario) AS preciototal from carrito where id_cliente="
				+ idClient;
		PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
		conexion.close();
		ResultSet totalSet = totalSta.executeQuery();
		if (totalSet.next()) {
			precioTest = Double.parseDouble(totalSet.getString("preciototal"));
		//	System.out.println(precioTest);
		}
		totalSet.close();
		totalSta.close();
		// conexion.close();

	}
	
	public void getPrecioTotalClienteNoExiste() throws SQLException {
		
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");	
			//Calculo el total del carrito del cliente
				String productsQuery = "SELECT SUM(cantidad*precio_unitario) AS preciototal from carrito where id_cliente="+99999;
				PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
				ResultSet totalSet = totalSta.executeQuery();
				if(totalSet.next()) {
					precioTest = Double.parseDouble(totalSet.getString("preciototal"));
				//	System.out.println(precioTest);
				}
				totalSet.close();
				totalSta.close();
				conexion.close();
			
		} 
	
	//La consulta SQL es incorrecta
	public void getPrecioTotalExcepcionConsulta() throws SQLException {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", "root", "9P$H7nI5!*8p");	
			//Calculo el total del carrito del cliente
				String productsQuery = "SELECT SUM(cantidad*precio_unitario) AS preciototal  where id_cliente="+idClient;
				PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
				ResultSet totalSet = totalSta.executeQuery();
				if(totalSet.next()) {
					precioTest = Double.parseDouble(totalSet.getString("preciototal"));
			//		System.out.println(precioTest);
				}
				totalSet.close();
				totalSta.close();
				conexion.close();
	}
	//*****************************************************************TESTS************************************************************************************//
																																																																					  //
	//Comprobacion de precio del carrito del cliente con id 254																																														 //
	@Test
	public void precioTest() {
		getPrecioTotal();
		assertEquals(precioTotalEsperado, precioTest,0.001); //Usamos un delta (margen de error)
		System.out.println("Test 1 correcto!");
		
	}
	//Test con cliente sin carrito.
	@Test
	public void testCarritoVacio() {
		getPrecioTotalNulo();
		assertNull("El carrito del cliente "+idClientPrecioNulo+" no está vacío", precioTestNulo);
		System.out.println("Test 2 correcto!");
	}
	
	//Simulacion de caida de la base de datos
	@Test
	public void testErrorConexion() {
		try {
			getPrecioTotalError();
			fail("Excepcion no lanzada, error en el test");
		}catch(SQLException e) {
			assertEquals("No operations allowed after statement closed.", e.getMessage());
			System.out.println("Test 3 correcto!");
		}
	}
	//Test con un cliente de la base de datos que no está registrado
	@Test
	public void testClienteNoExiste() {
		try {
			getPrecioTotalClienteNoExiste();
			fail("Excepcion no lanzada, error en el test");
		}catch(NullPointerException e) {
			assertTrue("Excepcion no capturada", e instanceof NullPointerException);
			System.out.println("Test 4 correcto!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Se esperaba una excepcion NullPointerException");
		}
	}
	
	@Test
	public void testTotalNoPositivo() {
		getPrecioTotal();
		assertTrue("El precio total no es un numero positivo", precioTest>0);
		System.out.println("Test 5 correcto!");
		
	}
	
	@Test
	public void TestConsultaSQLIncorrecta() {
		try {
			getPrecioTotalExcepcionConsulta();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			assertTrue("La excepcion de sintaxis incorrecta no ha sido lanzada",e instanceof SQLSyntaxErrorException);
			System.out.println("Test 6 correcto!");
		}
	}
	//***************************************************************************************************************************************************************************//
	
}
