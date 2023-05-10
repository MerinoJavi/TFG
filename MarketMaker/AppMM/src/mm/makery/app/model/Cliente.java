package mm.makery.app.model;

import java.time.LocalDate;
import java.util.Arrays;

public class Cliente {

	private String nombre;
	private String apellidos;
	private String correo;
	private String provincia;
	private String direccion;
	private String salt;
	private LocalDate fechanacimiento;
	
	public Cliente(String n, String ap, String c, String pr, String dir,String s,LocalDate fc) {
		this.nombre = n;
		this.apellidos = ap;
		this.correo = c;
		this.provincia = pr;
		this.direccion = dir;
		salt=s;
		fechanacimiento=fc;
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
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setMunicipio(String provincia) {
		this.provincia= provincia;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	/*
	public String getContrasenia() {
		return hashedpassword;
	}
	/* Lo dejo para cuando haya que hacer cambio de credenciales.
	public void setContrasenia(char[] contrasenia) {
		this.contrasenia = contrasenia;
	}
	*/
	//Deberia guardar la contraseña hasheada con la salt en el objeto Cliente cread
	public LocalDate getFechanacimiento() {
		return fechanacimiento;
	}
	public void setFechanacimiento(LocalDate fechanacimiento) {
		this.fechanacimiento = fechanacimiento;
	}
	public String getSalt() {
		return salt;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	
	
}
