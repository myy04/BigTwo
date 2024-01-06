/**
 * a subclass of Hand used to model a hand of Straight
 * @author Yerdaulet
 */
public class Straight extends Hand {
	
	private static final long serialVersionUID = 1L;
	
	// sorted array of card ranks (from highest to lowest).
	private static int[] rankPower = {1, 0, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
	// 2, A, K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3
	
	/**
	 * constructor for building a hand of Straight with the specified player and list of cards.
	 * @param player - player that this hand belongs to
	 * @param cards - cards in this hand
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return name of hand type
	 */
	public String getType() {
		return "Straight";
	}
	
	/**
	 *  a method for checking if this is a valid hand of straight.
	 *  @return true if it is a valid, false otherwise.
	 */
	public boolean isValid() {
		int n = this.size();
		
		
		if (n != 5) return false;
		this.sort();
		
		int mnPos = 99999999;
		int mxPos = -9999999;
		
		for (int i = 0; i < rankPower.length; i++) {
			for (int j = 0; j < n; j++) {
				if (rankPower[i] == getCard(j).getRank()) {
					mnPos = Math.min(mnPos, i);
					mxPos = Math.max(mxPos, i);
				}
			}
		}
		
		if (mxPos - mnPos == 4) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (i == j) continue;
					
					if (getCard(i).getRank() == getCard(j).getRank()) return false;
				}
			}
			
			return true;
		}
		
		return false;
		
		
	}
	
	/**
	 * function to get a power of this hand (compared to other hands with 5 cards). 
	 * @return power of this hand (1)
	 */
	public int getCombinationPower() {
		return 1;
	}
}
