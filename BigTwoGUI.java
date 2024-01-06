import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

/**
 * Implements CardGameUI interface. It is used to build a GUI for the BigTwo Card game.
 * @author Yerdaulet
 */
public class BigTwoGUI implements CardGameUI {
	

	private BigTwo game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;	
	private JPanel chatPanel;
	private JTextArea msgArea;
	private JTextArea chatArea;
	private JPanel btnPanel;
	private JButton play;
	private JButton pass;
	private JTextField chatInput;
	private JLabel lastPlayer;
	private int lastPlayerId;
	
	private String playerName;
	
	private HandComponent[] handComponents = new HandComponent[5];
	
	/**
	 * puts a message to the chat
	 * @param msg message from a player
	 */
	public void updateChatArea(String msg) {
		chatArea.append(msg);
	}
		
	/**
	 * a constructor for creating Big Two GUI.
	 * @param game BigTwo game
	 */
	BigTwoGUI(BigTwo game) {
		this.game = game;
		
		playerName = JOptionPane.showInputDialog("Enter your name");
		
		selected = new boolean[15];
		for (int i = 0; i < selected.length; i++) {
			selected[i] = false;
		}
		
		ClassLoader cldr = this.getClass().getClassLoader();	
		if (backImage == null) backImage = new ImageIcon(cldr.getResource("./cards/b.gif"));
		
		frame = new JFrame();
		frame.setTitle("Big Two" + "(" + playerName + ")");
		frame.setLayout(new BorderLayout());
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu gameMenu = new JMenu("Game");
		JMenuItem connect = new JMenuItem("Connect");
		JMenuItem quit = new JMenuItem("Quit");
		
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.getClient().setPlayerName(playerName);
				game.getClient().connect();
				frame.repaint();
			}
		});
		
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		gameMenu.add(connect);
		gameMenu.add(quit);
		
		menuBar.add(gameMenu);
		
		
		frame.setJMenuBar(menuBar);
		
		bigTwoPanel = new JPanel();
		bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.Y_AXIS));
		bigTwoPanel.setBackground(new Color(1, 50, 32));
				
		for (int i = 0; i < 4; i++) {
			String name = game.getPlayerList().get(i).getName();
			if (name == "") name = "NOT CONNECTED";
			
			handComponents[i] = new HandComponent();
			handComponents[i].setPlayerId(i);
			handComponents[i].setBorder(BorderFactory.createTitledBorder(null, name, 0, 0, null, Color.WHITE));
			handComponents[i].setForeground(Color.WHITE);
			
			handComponents[i].addMouseListener(handComponents[i]);	
			handComponents[i].repaint();
			
			bigTwoPanel.add(handComponents[i]);
		}
		
		handComponents[4] = new HandComponent();
		
		handComponents[4].setBorder(BorderFactory.createTitledBorder(null, "Previously Played Hand", 0, 0, null, Color.WHITE));
		
		handComponents[4].repaint();
		
		bigTwoPanel.add(handComponents[4]);
		
		frame.add(bigTwoPanel);
		
		chatPanel = new JPanel();
		frame.add(chatPanel, BorderLayout.EAST);
		
		msgArea = new JTextArea(10, 30);
		msgArea.setLineWrap(true);
		msgArea.setEditable(false);
		
		chatArea = new JTextArea(10, 30);
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		
		
		JScrollPane scrollerMsgArea = new JScrollPane(msgArea);
		scrollerMsgArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollerMsgArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JScrollPane scrollerChatArea = new JScrollPane(chatArea);
		scrollerChatArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollerChatArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

		chatPanel.add(scrollerMsgArea);
		chatPanel.add(scrollerChatArea);
		
		
		play = new JButton("Play");
		pass = new JButton("Pass");
		chatInput = new JTextField(25);
		
		play.addActionListener(new btnListener());
		pass.addActionListener(new btnListener());
		chatInput.addActionListener(new btnListener());
		
		lastPlayer = new JLabel("");
		
		if (game.getHandsOnTable().isEmpty() == false) {
			String name = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getPlayer().getName();
			
			lastPlayer.setText("Last move: " + name);
		}
		
		btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		btnPanel.add(play);
		btnPanel.add(pass);
		btnPanel.add(new JLabel("Chat:"));
		btnPanel.add(chatInput);
		btnPanel.add(lastPlayer);
		
		
		frame.add(btnPanel, BorderLayout.SOUTH);	
		
		frame.pack();
		frame.setSize(1280, 720);
		frame.setVisible(true);	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	/**
	 * a class for handling events of play/pass buttons and chat input.
	 * @author Yerdaulet
	 */
	public class btnListener implements ActionListener {

		
		@Override
		/**
		 * Function to handle play/pass buttons press and chat input.
		 * @param e ActionEvent to handle
		 */
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == play) {
				if (game.getCurrentPlayerIdx() != game.getClient().getPlayerID()) return;
								
				activePlayer = game.getCurrentPlayerIdx();
			
				int cnt = 0;
				for (int i = 0; i < selected.length; i++) {
					if (selected[i]) cnt++;
				}
				
				if (cnt == 0) return;
				
				int[] cardIdx = new int[cnt];
				for (int i = 0, j = 0; i < selected.length; i++) {
					if (selected[i]) {
						cardIdx[j] = i;
						j++;
					}
				}
			
				
				for (int i = 0; i < selected.length; i++) {
					selected[i] = false;
				}

				game.checkMove(activePlayer, cardIdx);
				repaint();
				
				if (game.endOfGame()) {
					
					printMsg("Game ends");		
					for (int i = 0; i < game.getNumOfPlayers(); i++) {
						CardGamePlayer player = game.getPlayerList().get(i);
						if (player.getNumOfCards() == 0) printMsg(player.getName() + " wins the game.");
						else printMsg(player.getName() + " has " + player.getNumOfCards() + " cards.");
					}
					
					repaint();
					
					return;
				}
				
			}
			
			else if (e.getSource() == pass) {
				if (game.getCurrentPlayerIdx() != game.getClient().getPlayerID()) return;
				
				for (int i = 0; i < selected.length; i++) {
					selected[i] = false;
				}

				game.checkMove(game.getClient().getPlayerID(), null);
				
				repaint();
				
			}
			
			else if (e.getSource() == chatInput) {
				String val = chatInput.getText();
				
				game.getClient().sendMessage(new CardGameMessage(7, game.getClient().getPlayerID(), game.getPlayerList().get(game.getClient().getPlayerID()).getName() + ": " + val));
				
				chatArea.append("Player " + Integer.toString(game.getCurrentPlayerIdx()) + ": " + val + "\n");
				
				chatInput.setText("");
			}
		}
		
	}
	
	/**
	 * a setter function to set the current player.
	 */
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}
	
	/**
	 * a function to repaing GUI.
	 */
	public void repaint() {
		
		for (int i = 0; i < 4; i++) {
			String name = game.getPlayerList().get(i).getName();
			if (name == "") name = "NOT CONNECTED";

			handComponents[i].setPlayerId(i);
			if (game.getClient().getPlayerID() == i) handComponents[i].setBorder(BorderFactory.createTitledBorder(null, name, 0, 0, null, Color.BLUE));
			else handComponents[i].setBorder(BorderFactory.createTitledBorder(null, name, 0, 0, null, Color.WHITE));

			handComponents[i].setForeground(Color.WHITE);
			
			handComponents[i].repaint();
			
//			bigTwoPanel.add(handComponents[i]);
			
		}
				
		handComponents[4].setBorder(BorderFactory.createTitledBorder(null, "Previously Played Hand", 0, 0, null, Color.WHITE));
		
		handComponents[4].repaint();

//		bigTwoPanel.add(handComponents[4]);

				
		frame.repaint();
	}
	
	/**
	 * a function to send message to the players.
	 */
	public void printMsg(String msg) {
		msgArea.append(msg + "\n");
		msgArea.update(msgArea.getGraphics());
	}
		
	/**
	 * a functino to clear message area.
	 */
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	/**
	 * a function to ask a player to make a move.
	 */
	public void promptActivePlayer() {
		printMsg(game.getPlayerList().get(game.getCurrentPlayerIdx()).getName() + "'s turn");	
		msgArea.update(msgArea.getGraphics());
	}
	
	private static HashMap<Card, ImageIcon> image = new HashMap<Card, ImageIcon>();
	private static ImageIcon backImage = null;
	
	/**
	 * a function to load an card image from a file.
	 * @param card card for which image is being loaded
	 */
	public void loadImage(Card card) {
		
		ClassLoader cldr = this.getClass().getClassLoader();
		
		String suits = "dchs";
		String faces = "a23456789tjqk";		
		
		String imagePath = "./cards/" + faces.charAt(card.rank) + suits.charAt(card.suit) + ".gif";
					
		java.net.URL imageURL = cldr.getResource(imagePath);
		image.put(card, new ImageIcon(imageURL));

	}
	
	/**
	 * a class for creating components that draw hands as well as handling events.
	 * @author Yerdaulet
	 */
	public class HandComponent extends JComponent implements MouseListener {
		
		private int playerId = 4;
		
		/**
		 * a setter function for player index.
		 * @param playerId index of the player that this hand belongs to.
		 */
		public void setPlayerId(int playerId) {
			this.playerId = playerId;
		}
		
		@Override
		/**
		 * a function that draws hands.
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if (playerId == 4) {
				

				if (game.getHandsOnTable().isEmpty() == false) {
					String name = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getPlayer().getName();
					lastPlayer.setText("Last move: " + name);
				}
					
			}
			
			CardList cards = new CardList();
			if (playerId != 4) cards = game.getPlayerList().get(playerId).getCardsInHand();
			else {
				if (game.getHandsOnTable().isEmpty() == false) {
					Hand hand = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
					
					for (int i = 0; i < hand.size(); i++) {
						cards.addCard(hand.getCard(i));
					}
				}
			}
			
			int x = 10;
			int y = getHeight() / 4;
			
			
			for (int j = 0; j < cards.size(); j++) {
				Card card = cards.getCard(j);
					
				if (image.containsKey(card) == false) loadImage(card);
				
				if (game.getClient().getPlayerID() == playerId || playerId == 4 || game.endOfGame()) {
					if (playerId == 4 || game.inProgress == false) image.get(card).paintIcon(bigTwoPanel, g, x, y);
					else {
						if (selected[j]) image.get(card).paintIcon(bigTwoPanel, g, x, y - 20);
						else image.get(card).paintIcon(bigTwoPanel, g, x, y);
					}
				}
				else backImage.paintIcon(bigTwoPanel, g, x, y);
				
				
				x += image.get(card).getIconWidth() / 2;
			}
		}

		@Override
		/**
		 * a function that handles an event of mouse clicking
		 */
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
			if (game.endOfGame()) return;
			
			int x = e.getX();
			int y = e.getY();
			
			CardList cards = game.getPlayerList().get(playerId).getCardsInHand();
			
			int curX = 10;
			int curY = getHeight() / 4;
			for (int i = 0; i < cards.size(); i++) {
				
				Card card = cards.getCard(i);
				
				int width = image.get(card).getIconWidth();
				int height = image.get(card).getIconHeight();
				
				if (i + 1 < cards.size()) {
					if (x >= curX && x < curX + width / 2 && y >= curY && y <= curY + height) {
						selected[i] ^= true;
						System.out.println(i);
						repaint();
						return;
					}
				}
				
				else {
					if (x >= curX & x <= curX + width && y >= curY && y <= curY + height) {
						selected[i] ^= true;
						System.out.println(i);
						repaint();
						return;
					}
				}
				
				curX += (width / 2);
			}
			
		}

		@Override
		/**
		 * not being used
		 */
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		/**
		 * not being used
		 */
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		/**
		 * not being used
		 */
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		/**
		 * not being used
		 */
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
			
	}

	@Override
	/**
	 * not being used
	 */
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * not being used
	 */
	public void enable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * not being used
	 */
	public void disable() {
		// TODO Auto-generated method stub
		
	}
	
}
	