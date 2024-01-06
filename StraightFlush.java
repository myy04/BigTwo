/**
 * a subclass of Hand used to model a hand of Flush
 * @author Yerdaulet
 */
public class StraightFlush extends Hand {
		
	private static final long serialVersionUID = 1L;

	/**
	 * constructor for building a hand of Straight Flush with the specified player and list of cards.
	 * @param player - player that this hand belongs to
	 * @param cards - cards in this hand
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return name of hand type
	 */
	public String getType() {
		return "StraightFlush";
	}
	
	/**
	 *  a method for checking if this is a valid hand of Straight Flush.
	 *  @return true if it is a valid, false otherwise.
	 */
	public boolean isValid() {
		Hand flush = new Flush(this.getPlayer(), this);
		Hand straight = new Straight(this.getPlayer(), this);
		
		return (flush.isValid() && straight.isValid());
	}

	/**
	 * function to get a power of this hand (compared to other hands with 5 cards). 
	 * @return power of this hand (5)
	 */
	public int getCombinationPower() {
		return 5;
	}
}
