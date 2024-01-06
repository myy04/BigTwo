
/**
 * a subclass of Hand used to model a hand of Single
 * @author Yerdaulet
 */
public class Single extends Hand {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * constructor for building a hand of Single with the specified player and list of cards.
	 * @param player - player that this hand belongs to
	 * @param cards - cards in this hand
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return name of hand type
	 */
	public String getType() {
		return "Single";
	}
	
	/**
	 *  a method for checking if this is a valid hand of flush.
	 *  @return true if it is a valid flush hand, false otherwise.
	 */
	public boolean isValid() {
		return true;
	}

}

