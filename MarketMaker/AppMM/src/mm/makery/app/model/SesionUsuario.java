package mm.makery.app.model;

import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SesionUsuario {
	private String nombre;
	private String apellidos;
	private Date fechanacimiento;
	private String usuario;
	private String email;
	private String provincia;
	private String municipio;
	private String pais;
	private String direccion;
	private String nif;
	private String codigopostal;
	private String telefono;
	
	public static ArrayList<SesionUsuario> usuarios;
	public static String usuarioABuscar;
	
	
	public SesionUsuario(String nombre, String apellidos, Date fechanacimiento, String usuario, String email,
			String provincia, String pais, String direccion,String codigop,String n,String mun) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechanacimiento = fechanacimiento;
		this.usuario = usuario;
		this.email = email;
		this.provincia = provincia;
		this.pais = pais;
		this.direccion = direccion;
		this.codigopostal=codigop;
		this.nif=n;
		this.municipio=mun;
		
		usuarios = new ArrayList<>();
	}
	
	public ArrayList<SesionUsuario> getUsuarios(){
		return usuarios;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public Date getFechanacimiento() {
		return fechanacimiento;
	}
	public void setFechanacimiento(Date fechanacimiento) {
		this.fechanacimiento = fechanacimiento;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public void addSesion(SesionUsuario se) {
		usuarios.add(se);
	}
	
	
	
}
