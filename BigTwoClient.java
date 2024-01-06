import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

/**
 * class to model a big two game client that is reponsible for communicating with the game server
 * @author Yerdaulet
 */
public class BigTwoClient implements NetworkGame {

	private BigTwo game;
	private BigTwoGUI gui;
	private Socket sock;
	private ObjectOutputStream oos;
	private int playerID = -1;
	private String playerName;
	private String serverIP = "127.0.0.1";
	private int serverPort = 2396;
	
	/**
	 * a function to construct a client for the BigTwo game
	 * @param game BigTwo game
	 * @param gui gui of the game
	 */
	BigTwoClient(BigTwo game, BigTwoGUI gui) {
		this.game = game;
		this.gui = gui;
		playerID = -1;
	}
	
	@Override
	/**
	 * getter function for the player Id
	 * @retunr id of the player
	 */
	public int getPlayerID() {
		return playerID;
	}

	@Override
	/**
	 * setter function for the player ID
	 * @param playerID id of the player
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	@Override
	/**
	 * getter function for the player name
	 * @return name of the player
	 */
	public String getPlayerName() {
		return playerName;
	}

	@Override
	/**
	 * setter function for the player name
	 * @param playerName name of the player
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	/**
	 * getter function for the server IP
	 * @return server IP
	 */
	public String getServerIP() {
		return serverIP;
	}

	@Override
	/**
	 * setter function for the server IP
	 * @param serverIP server IP
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	@Override
	/**
	 * getter function for the server port
	 * @return server port
	 */
	public int getServerPort() {
		return serverPort;
	}

	@Override
	/**
	 * setter function for the server port
	 * @param serverPort serverPort
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	/**
	 * function to connect a client to the server
	 */
	public void connect() {
		if (sock != null && sock.isConnected()) return;
		
		try {
			sock = new Socket(serverIP, serverPort);
			oos = new ObjectOutputStream(sock.getOutputStream());
			Thread serverHandlerThread = new Thread(new ServerHandler());
            serverHandlerThread.start();     
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	/**
	 * message for reacting to the messages from the server
	 * @param message message from the server
	 */
	public void parseMessage(GameMessage message) {
		int type = message.getType();
				
		if (type == 0) { //PLAYER_LIST
								
			String[] names = (String[]) message.getData();
		
			for (int i = 0; i < names.length; i++) {
				if (names[i] == null) names[i] = "";							
				game.getPlayerList().get(i).setName(names[i]);
			}			
				
			playerID = message.getPlayerID();
			
			if (playerName.isBlank() || playerName.isEmpty()) System.exit(0);
			
			game.getPlayerList().get(playerID).setName(playerName);
			sendMessage(new CardGameMessage(1, -1, playerName));			
		}
		
		if (type == 1) { // JOIN
			if (playerID == message.getPlayerID()) {
				sendMessage(new CardGameMessage(4, -1, null));
			}
			else {
				game.getPlayerList().get(message.getPlayerID()).setName((String) message.getData());
			}

			gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName() + " JOINS");
		}
		
		if (type == 2) { // FULL
			gui.printMsg("THE SERVER IS FULL");
		}		
		
		if (type == 3) { // QUIT
			game.getPlayerList().get(message.getPlayerID()).setName("");
			if (game.inProgress == true) {
				game.inProgress = false;
				
				sendMessage(new CardGameMessage(4, -1, null));
			}
		}
		
		if (type == 4) { // READY
			gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName() + " IS READY");
		}
		
		if (type == 5) { // START
			gui.printMsg("GAME STARTS");
			game.start((BigTwoDeck) message.getData());
		}
		
		if (type == 6) { // MOVE
			int[] cardIdx = (int[]) message.getData();
				
			game.makeMove(message.getPlayerID(), cardIdx);

			if (game.endOfGame()) {
				game.inProgress = false;
				
				String results = "";
				
				for (int i = 0; i < 4; i++) {
					String cur;
					if (game.getPlayerList().get(i).getNumOfCards() > 0) cur = game.getPlayerList().get(i).getName() + "has " + Integer.toString(game.getPlayerList().get(i).getNumOfCards());
					else cur = game.getPlayerList().get(i).getName() + "is the winner";
					results += cur;
					results += "\n";
				}
				
				int ok = JOptionPane.showConfirmDialog(null, results, "Results", JOptionPane.OK_OPTION);
				if (ok == JOptionPane.OK_OPTION) sendMessage(new CardGameMessage(4, -1, null));
				else {
					System.exit(0);
				}
				
			}
			
			gui.promptActivePlayer();
		}
		
		if (type == 7) { // MSG
			gui.updateChatArea((String) message.getData());
		}
		
		
		gui.repaint();
	}

	@Override
	/**
	 * function to send messages to the server
	 * @param message the message to be sent to the server
	 */
	public void sendMessage(GameMessage message) {
		// TODO Auto-generated method stub
	
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }		
	}

	/**
	 * class for handling the server
	 * @author Yerdaulet
	 */
    private class ServerHandler implements Runnable {
        @Override
        public void run() {
            try {
                ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                while (true) {
                    GameMessage message = (GameMessage) ois.readObject();
                    parseMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                // Handle connection or message receiving failure
            }
        }
    }
	
}
