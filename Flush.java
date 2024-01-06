/**
 * a subclass of Hand used to model a hand of Flush
 * @author Yerdaulet
 */
public class Flush extends Hand {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * constructor for building a hand of flush with the specified player and list of cards.
	 * @param player - player that this hand belongs to
	 * @param cards - cards in this hand
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return name of hand type
	 */
	public String getType() {
		return "Flush";
	}
	
	/**
	 *  a method for checking if this is a valid hand of Flush.
	 *  @return true if it is a valid, false otherwise.
	 */
	public boolean isValid() {
		int n = this.size();
		
		if (n != 5) return false;
		
		for (int i = 0; i < n; i++) {
			
			if (this.getCard(0).getSuit() != this.getCard(i).getSuit()) return false;
		}
		
		return true;
	}
	
	/**
	 * function to get a power of this hand (compared to other hands with 5 cards). 
	 * @return power of this hand (2)
	 */
	public int getCombinationPower() {
		return 2;
	}

}
