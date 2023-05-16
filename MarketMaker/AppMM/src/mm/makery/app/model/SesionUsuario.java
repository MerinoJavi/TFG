package mm.makery.app.model;

import java.util.Map;
import java.util.HashMap;

public class SesionUsuario {
	private Map<String,String> sesionesactivas;
	
	
	public SesionUsuario() {
		sesionesactivas = new HashMap<>();
	}
	
	public void addsesionusuario(String token,String username) {
		sesionesactivas.put(token, username);
	}
	
	public void eliminarsesionusuario(String username) {
		sesionesactivas.remove(username);
	}
	public String  obtenerUsuarioPorToken(String token) {
		return sesionesactivas.get(token);
	}

	public String obtenerTokenPorUsuario(Map<String, String> map, String username) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue().equals(username)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
