import java.io.Serializable;

public class Settings implements Serializable {
	
	private static final long serialVersionUID = 1L;
	int players;
	String map;
	String rules;
	
	public Settings(int players, String map, String rules) {
		this.players = players;
		this.map = map;
		this.rules = rules; }
}
