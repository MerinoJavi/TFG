package mm.makery.app.model;

import java.util.Map;
import java.util.HashMap;

public class SesionUsuario {
	private Map<String,Integer> sesionesactivas;
	
	
	public SesionUsuario() {
		sesionesactivas = new HashMap<>();
	}
	
	public void addsesionusuario(int token,String username) {
		sesionesactivas.put(username, token);
	}
	
	public void eliminarsesionusuario(String username) {
		sesionesactivas.remove(username);
	}
	public Integer obtenerTokenPorUsuario(String username) {
		return sesionesactivas.get(username);
	}

	public String obtenerUsuarioDadoToken(Map<Integer, String> map, Integer token) {
		for (Map.Entry<String, Integer> entry : map.ke) {
			if (entry.getValue().equals(token)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
