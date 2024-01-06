import java.awt.Component;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * The BigTwoCard class is a subclass of the Card class and is used to model a card used in a Big Two card game.
 * @author Yerdaulet
 */
public class BigTwoCard extends Card {
	
	private static final long serialVersionUID = 1L;

	// sorted array of ranks (from highest to lowest).
	private static int[] rankPower = {1, 0, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
	// 2, A, K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3
		
	/**
	 * a constructor for building a card with the specified suit and rank
	 * @param suit suit of the card (integer between 0 - 3)
	 * @param rank rank of the card (integer between 0 - 12)
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * a constructor for building a BigTwoCard based on the data of a regular card
	 * @param card - card
	 */
	public BigTwoCard(Card card) {
		super(card.getSuit(), card.getRank());
	}
	
	
	/**
	 * a method for comparing the order of this card with the specified card
	 * @param card card that is being compared to
	 * @return Returns a negative integer, zero, or a positive integer when this card is less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card) { 
		int aPower = -1, bPower = -1;
		
		for (int i = 0; i < rankPower.length; i++) {
			if (this.getRank() == rankPower[i]) aPower = i;
			if (card.getRank() == rankPower[i]) bPower = i;
		}
		
		if (aPower != bPower) {
			if (aPower < bPower) return 1;
			else return -1;
		}
		else {
			if (this.getSuit() < card.getSuit()) return -1;
			else if (this.getSuit() > card.getSuit()) return 1;
			else return 0;
		}
	}


}
