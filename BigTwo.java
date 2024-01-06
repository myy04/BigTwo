import java.io.*;
import java.util.*;

/**
 * The BigTwo class implements the CardGame interface and is used to model a Big Two card game. 
 * @author Yerdaulet
 */
public class BigTwo implements CardGame {
	/**
	 * 
	 */
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>();
	private ArrayList<Hand> handsOnTable = new ArrayList<Hand>();
	private int currentPlayerIdx;
	private BigTwoGUI gui;
	private BigTwoClient client;
	
	/**
	 * true if the game is in progress and false otherwise
	 */
	public boolean inProgress;
	
	/**
	 * getter function for the client
	 * @return client
	 */
	public BigTwoClient getClient() {
		return client;
	}
	
	/**
	 * a no argument constructor for creating a Big Two card game with 4 players. 
	 */
	public BigTwo() {
		numOfPlayers = 4;
		for (int i = 0; i < 4; i++) {
			playerList.add(new CardGamePlayer());
			playerList.get(i).setName("");
		}
		
		inProgress = false;
		gui = new BigTwoGUI(this);
		client = new BigTwoClient(this, gui);
	}
	
	
	/**
	 * a method for getting the number of players.
	 * @return number of players
	 */
	public int getNumOfPlayers() {
		return numOfPlayers;
	}
	
	/**
	 * a method for retrieving the deck of cards being used
	 * @return deck of cards
	 */
	public Deck getDeck() {
		return deck;
	}
	
	/**
	 * a method for retrieving the list of players.
	 * @return list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}
	
	/**
	 * a method for retrieving the list of hands played on the table.
	 * @return list of hands played
	 */
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}
	
	/**
	 * a method for retrieving the index of the current player.
	 * @return index of current player.
	 */
	public int getCurrentPlayerIdx() {
		return currentPlayerIdx;
	}
	
	/**
	 * setter function for currentPlayerIdx
	 * @param playerId current player
	 */
	public void setCurrentPlayerIdx(int playerId) {
		currentPlayerIdx = playerId;
	}
	
	/**
	 * a method for starting/restarting the game with a given shuffled deck of cards
	 */
	public void start(Deck deck) {
		handsOnTable.clear();
		
		for (int i = 0; i < numOfPlayers; i++) {
			playerList.get(i).removeAllCards();
			
			while (deck.size() > 0 && playerList.get(i).getNumOfCards() < 13) {
				Card card = deck.removeCard(deck.size() - 1);
				
				playerList.get(i).addCard(card);
			}
			
			if (playerList.get(i).getCardsInHand().contains(new Card(0, 2))) {
				currentPlayerIdx = i;
			}
			
			playerList.get(i).sortCardsInHand();
		}
		
		inProgress = true;
		
		
		gui.setActivePlayer(currentPlayerIdx);
		gui.promptActivePlayer();
		gui.repaint();
	}
	
	/**
	 * a method for making a move by a player with the specified index using the cards specified by the list of indices
	 */
	public void makeMove(int playerIdx, int[] cardIdx) {
		if (inProgress == false) return;
		
		if (cardIdx != null) {
			
			Hand currentHand = composeHand(this.getPlayerList().get(playerIdx), this.getPlayerList().get(playerIdx).play(cardIdx));
			gui.printMsg(getPlayerList().get(playerIdx).getName() + "'s move: " + currentHand.getType());
	
			handsOnTable.add(currentHand);
			playerList.get(playerIdx).removeCards(currentHand);
		}
		else {
			gui.printMsg(getPlayerList().get(playerIdx).getName() + "'s move: " + "PASS");
		}
		
		currentPlayerIdx++;
		currentPlayerIdx %= 4;
		
		gui.setActivePlayer(currentPlayerIdx);
		gui.repaint();
	}
	
	/**
	 * a method for checking a move made by a player.
	 */
	public void checkMove(int playerIdx, int[] cardIdx) {
		if (inProgress == false) return;
		
		if (cardIdx == null) {
			if (handsOnTable.isEmpty()) {

				gui.printMsg("Not a legal move!!!");
				gui.promptActivePlayer();

				return;
			}
			
			if (playerList.get(playerIdx) == handsOnTable.get(handsOnTable.size() - 1).getPlayer()) {

				gui.printMsg("Not a legal move!!!");
				gui.promptActivePlayer();

				return;
			}
			
			client.sendMessage(new CardGameMessage(6, -1, cardIdx));			
			return;
		}
		
		Hand currentHand = composeHand(this.getPlayerList().get(playerIdx), this.getPlayerList().get(playerIdx).play(cardIdx));

		if (currentHand == null) {

			gui.printMsg("Not a legal move!!!");
			gui.promptActivePlayer();

			return;
		}
		
		if (this.handsOnTable.isEmpty()) {
			if (currentHand.contains(new Card(0, 2)) == false) {
				
				gui.printMsg("Not a legal move!!!");
				gui.promptActivePlayer();

				return;
			}

			client.sendMessage(new CardGameMessage(6, -1, cardIdx));
			
			return;				
		}
		
		Hand lastHand = handsOnTable.get(handsOnTable.size() - 1);
		
		if (lastHand.getPlayer() == playerList.get(playerIdx)) {
			client.sendMessage(new CardGameMessage(6, -1, cardIdx));

			return;
		}
		
		if (lastHand.size() != currentHand.size()) {

			gui.printMsg("Not a legal move!!!");
			gui.promptActivePlayer();

			return;
		}
		
		if (currentHand.beats(lastHand) == false) {

			gui.printMsg("Not a legal move!!!");
			gui.promptActivePlayer();

			return;			
		}
		
		client.sendMessage(new CardGameMessage(6, -1, cardIdx));
	}
	
	/**
	 * a method for checking if the game ends.
	 * @return true if the game has ended, false otherwise
	 */
	public boolean endOfGame() {
		for (int i = 0; i < this.getNumOfPlayers(); i++) {
			if (this.getPlayerList().get(i).getCardsInHand().isEmpty() == true) return true;
		}
		
		return false;
	}
	
	/**
	 * Creates and runs big two game
	 * @param args args are not used
	 */
	public static void main(String args[]) {		
		BigTwo game = new BigTwo();
		if (game.getClient().getPlayerName() == "") System.exit(0);
	}
	
	/**
	 * a method for returning a valid hand from the specified list of cards of the player
	 * @param player player who has selected the cards
	 * @param cards selected cards
	 * @return a valid hand from the specified list of cards of the player. Returns null if no valid hand can be composed from the specified list of cards
	 */
	public Hand composeHand(CardGamePlayer player, CardList cards) {
		int n = cards.size();
		
		if (n == 1) {
			Hand single = new Single(player, cards);
			if (single.isValid()) return single;
			else return null;
		}
		
		if (n == 2) {
			Hand pair = new Pair(player, cards);
			if (pair.isValid()) return pair;
			else return null;
		}
		
		if (n == 3) {
			Hand triple = new Triple(player, cards);
			if (triple.isValid()) return triple;
			else return null;
		}
		
		if (n == 5) {
					
			if ((new StraightFlush(player, cards)).isValid()) return new StraightFlush(player, cards);
			if ((new Quad(player, cards)).isValid()) return new Quad(player, cards);
			if ((new FullHouse(player, cards)).isValid()) return new FullHouse(player, cards);
			if ((new Flush(player, cards)).isValid()) return new Flush(player, cards);
			if ((new Straight(player, cards)).isValid()) return new Straight(player, cards);
			
			return null;
		}
		
		return null;
	}
}