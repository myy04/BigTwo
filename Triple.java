
/**
 * a subclass of Hand used to model a hand of Triple
 * @author Yerdaulet
 */
public class Triple extends Hand {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor for building a hand of triple with the specified player and list of cards.
	 * @param player - player that this hand belongs to
	 * @param cards - cards in this hand
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return name of hand type
	 */
	public String getType() {
		return "Triple";
	}
	
	/**
	 *  a method for checking if this is a valid hand of triple.
	 *  @return true if it is a valid, false otherwise.
	 */
	public boolean isValid() {
		CardList cards = this;
		int n = cards.size();
		
		if (n != 3) return false;
		
		for (int i = 1; i < 3; i++) {
			if (cards.getCard(0).getRank() != cards.getCard(i).getRank()) return false;
		}
		
		return true;
	}

}
