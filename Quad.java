
/**
 * a subclass of Hand used to model a hand of Quad
 * @author Yerdaulet
 */
public class Quad extends Hand {
	
	
	private static final long serialVersionUID = 1L;

	/**
	 * constructor for building a hand of Quad with the specified player and list of cards.
	 * @param player - player that this hand belongs to
	 * @param cards - cards in this hand
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return name of hand type
	 */
	public String getType() {
		return "Quad";
	}
	
	
	/**
	 *  a method for checking if this is a valid hand of quad.
	 *  @return true if it is a valid hand of quad, false otherwise.
	 */
	public boolean isValid() {
		CardList cards = this;
		int n = cards.size();
		
		if (n != 5) return false;
	
		int rankCount[] = new int[15];
		
		for (int i = 0; i < n; i++) {
			rankCount[cards.getCard(i).getRank()]++;
		}
		
		for (int i = 0; i < 15; i++) {
			if (rankCount[i] == 4) return true;
		}
		
		return false;
	}
	
	/**
	 * function to get a power of this hand (compared to other hands with 5 cards). 
	 * @return power of this hand (4)
	 */
	public int getCombinationPower() {
		return 4;
	}
}
