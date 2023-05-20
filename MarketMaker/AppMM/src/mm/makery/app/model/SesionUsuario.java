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

	public static String obtenerUsuarioDadoToken(HashMap<String, Integer> map, Integer token) {
		for (HashMap.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue().equals(token)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
