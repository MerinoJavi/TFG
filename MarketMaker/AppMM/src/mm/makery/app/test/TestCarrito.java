package mm.makery.app.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mm.makery.app.view.LoginController;

@RunWith(JUnit4.class)
public class TestCarrito {
	
	double precioTotalEsperado=1338.3;
	static double precioTest=0.0;
	static Double precioTestNulo=0.0; //Variable para el tests que comprueba que el carrito es nulo
	int idClient=254; //Cliente del primer test
	int idClientPrecioNulo=208; //Cliente que no tiene nada en el carrito
	String root="root";
	String pass= "9P$H7nI5!*8p";
	
	//Prueba para comprobar el precio total de los productos del cliente con id 254.
	public void getPrecioTotal() throws SQLException{
		
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG",root,pass);	
			//Calculo el total del carrito del cliente
				String productsQuery = "SELECT SUM(cantidad*precio_unitario) AS preciototal from carrito where id_cliente="+idClient;
				PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
				ResultSet totalSet = totalSta.executeQuery();
				if(totalSet.next()) {
					precioTest = Double.parseDouble(totalSet.getString("preciototal")); //Almaceno el precio total del carrito del cliente
			//		System.out.println(precioTest);
				}
				//Manejo de cierre de recursos
				totalSet.close();
				totalSta.close();
				conexion.close();
			
		
	}
	//Prueba en la cual se introduce un id de un cliente que no tiene ningun producto en el carrito
	public void getPrecioTotalNulo() throws SQLException {
		
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", root, pass);	
			//Calculo el total del carrito del cliente
				String productsQuery = "SELECT SUM(cantidad*precio_unitario) AS preciototal from carrito where id_cliente="+idClientPrecioNulo;
				PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
				ResultSet totalSet = totalSta.executeQuery();
				if(totalSet.next()&&!(totalSet.getDouble("preciototal")==0.0)) {
					precioTestNulo= Double.parseDouble(totalSet.getString("preciototal"));
					
				}else {
					precioTestNulo=null;
				}
				totalSet.close();
				totalSta.close();
				conexion.close();
			
		
	}
	//En este metodo simulo una caida de la base de datos, donde en el test se maneja y captura la excepcion SQLException que se lanza
	public void getPrecioTotalError() throws SQLException {

		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", root, pass);
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
	//Se intenta obtener el precio total del carrito de un cliente que no está registrado en la base de datos
	public void getPrecioTotalClienteNoExiste() throws SQLException {
		
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", root, pass);	
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
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", root, pass);	
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
	//Actualiza la cantidad del producto y le suma uno más
	public void actualizarCantidadProducto() throws SQLException {
		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", root, pass);
		String quantityUpdateQuery = "UPDATE carrito SET cantidad= cantidad+1 WHERE id_producto=70";
		PreparedStatement quantitySta = conexion.prepareStatement(quantityUpdateQuery);
		quantitySta.execute();
		
		quantitySta.close();
		conexion.close();
	}
	
	//Se obtiene el precio del carrito de un cliente que no tiene productos en el carrito.id del cliente 375
	public int totalCarritoVacioCliente() throws SQLException{
		int total=0;
		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", root, pass);	
		//Calculo el total del carrito del cliente
			String productsQuery = "SELECT SUM(cantidad*precio_unitario) AS preciototal from carrito  where id_cliente=375";
			PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
			ResultSet totalSet = totalSta.executeQuery();
			if(totalSet.next()) {
				 total=totalSet.getInt("preciototal");
			}
			
			
			return total;
	}
	
	//Elimina un producto del carrito con id de carrito 48 y producto 387
	public void eliminarProducto() throws SQLException {
		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", root, pass);	
		String getProductoEliminarQuery = "DELETE FROM carrito where idcarrito=48 and id_producto=387";
		Statement staEliminarProducto = conexion.prepareStatement(getProductoEliminarQuery);
		
	}
	//sumar la cantidad total de productos del cliente con id 254 y comprobar el resultado
	public int totalCantidadProducto() throws SQLException {
		int totalcantidad=0;
		Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/TFG", root, pass);	
		//Calculo el total del carrito del cliente
			String productsQuery = "SELECT SUM(cantidad) as totalcantidad from carrito where id_cliente=254";
			PreparedStatement totalSta = conexion.prepareStatement(productsQuery);
			ResultSet totalSet = totalSta.executeQuery();
			if(totalSet.next()) {
				 totalcantidad=totalSet.getInt("totalcantidad");
			}
			return totalcantidad;
	}
	//*****************************************************************TESTS************************************************************************************//
																																																																					  //
	//Comprobacion de precio del carrito de un cliente registrado en la base de datos																																					 //
	@Test
	public void precioTest() {
		try {
			getPrecioTotal();
			assertEquals(precioTotalEsperado, precioTest,0.001); //Usamos un delta (margen de error)
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Error en la base de datos en el Test 1");
		}
		
	}
	//Test con cliente sin carrito.
	@Test
	public void testCarritoVacio() {
		try {
			getPrecioTotalNulo();
			assertNull("El carrito del cliente "+idClientPrecioNulo+" no está vacío", precioTestNulo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Error en el test 2");
		}
	}
	
	//Simulacion de caida de la base de datos
	@Test
	public void testErrorConexion() {
		try {
			getPrecioTotalError();
			fail("Excepcion no lanzada, error en el test");
		}catch(SQLException e) {
			assertEquals("No operations allowed after statement closed.", e.getMessage());
//			System.out.println("Test 3 correcto!");
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
//			System.out.println("Test 4 correcto!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Se esperaba una excepcion NullPointerException");
		}
	}
	
	//Test para comprobar que el precio es positivo
	@Test
	public void testTotalPositivo() {
		try {
			getPrecioTotal();
			assertTrue("El precio total no es un numero positivo", precioTest>0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Error en el test 5");
		}
		
//		System.out.println("Test 5 correcto!");
	}
	
	//Test donde se captura la excepcion lanzada por sintaxis incorrecta
	@Test
	public void TestConsultaSQLIncorrecta() {
		try {
			getPrecioTotalExcepcionConsulta();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			assertTrue("La excepcion de sintaxis incorrecta no ha sido lanzada",e instanceof SQLSyntaxErrorException);
//			System.out.println("Test 6 correcto!");
		}
	}
	//***************************************************************************************************************************************************************************//
	//Actualizar la cantidad de un producto con id 70 del carrito del cliente con id de carrito 48
	@Test
	public void TestActualizarCantidadProducto() {
		try {
			actualizarCantidadProducto();
//			System.out.println("Test 7 correcto!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Excepcion no esperada");
		}
	}
	//Test para calcular el total de un carrito vacio (por ende no tiene productos)
	@Test
	public void TestTotalCarritoVacio() {
		try {
			int total = totalCarritoVacioCliente();
			assertEquals(total, 0);
//			System.out.println("Test 8 correcto");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("No se esperaba este error");
		}
	}
	//Elimina un producto del carrito con id de carrito 48 y producto 387
	@Test
	public void TestEliminacionProducto() {
		try {
			eliminarProducto();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("El producto no se ha podido eliminar");
		}
	}
	//Test para sumar la cantidad total de productos del cliente con id 254 y comprobar el resultado
	@Test
	public void TestCantidadProducto() {
		try {
			int cantidad=totalCantidadProducto();
			assertEquals(10, cantidad);
//			System.out.println("Test 10 correcto!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Excepcion no esperada");
			
		}
	}
	

}
