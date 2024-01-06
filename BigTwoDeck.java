import javax.swing.JPanel;

/**
 * The BigTwoDeck class is a subclass of the Deck class and is used to model a deck of cards used in a Big Two card game.
 * @author Yerdaulet
 */
public class BigTwoDeck extends Deck {
	private static final long serialVersionUID = 1L;
		
	/**
	 * a method for initializing a deck of Big Two cards.
	 */
	public void initialize() {
		this.removeAllCards();
			
		for (int suit = 0; suit < 4; suit++) {
			for (int rank = 0; rank < 13; rank++) {
				this.addCard(new BigTwoCard(suit, rank));
			}
		}
	}
}
