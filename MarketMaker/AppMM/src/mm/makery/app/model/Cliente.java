package mm.makery.app.model;

public class Cliente {

	private String nombre;
	private String apellidos;
	private String correo;
	private String municipio;
	private String direccion;
	private char[] contrasenia;
	
	public Cliente(String n, String ap, String c, String mun, String dir, char[] contr) {
		this.nombre = n;
		this.apellidos = ap;
		this.correo = c;
		this.municipio = mun;
		this.direccion = dir;
		contrasenia=contr;
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
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public char[] getContrasenia() {
		return contrasenia;
	}
	public void setContrasenia(char[] contrasenia) {
		this.contrasenia = contrasenia;
	}
	
	
	
}
