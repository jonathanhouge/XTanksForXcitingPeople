/* The Settings class. Created by the first client who connects via the Host Display, 
 * passed from the client to the server. Holds onto the chosen amount of players, map, and rules.
 * 
 * AUTHOR: Jonathan
 */

import java.io.Serializable;

public class Settings implements Serializable {
	
	private static final long serialVersionUID = 1L;
	int players;
	String map;
	String rules;
	
	// constructor
	public Settings(int players, String map, String rules) {
		this.players = players;
		this.map = map;
		this.rules = rules; }
}
