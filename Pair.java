
/**
 * a subclass of Hand used to model a hand of Pair
 * @author Yerdaulet
 */
public class Pair extends Hand {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor for building a hand of pair with the specified player and list of cards.
	 * @param player - player that this hand belongs to
	 * @param cards - cards in this hand
	 */	
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return name of hand type
	 */
	public String getType() {
		return "Pair";
	}
	
	/**
	 *  a method for checking if this is a valid hand of pair.
	 *  @return true if it is a valid hand of pair, false otherwise.
	 */
	public boolean isValid() {
			
		CardList cards = this;
		int n = cards.size();
		
		if (n != 2) return false;
		
		if (cards.getCard(0).getRank() != cards.getCard(1).getRank()) return false;
		
		return true;
	}
}
