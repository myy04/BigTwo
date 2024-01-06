
/**
 * (abstract) subclass of the CardList class and is used to model a hand of cards.
 * @author Yerdaulet
 */
abstract class Hand extends CardList {

	private static final long serialVersionUID = 1L;
	
	private CardGamePlayer player = new CardGamePlayer();
	
	/**
	 * a constructor for building a hand with the specified player and list of cards.
	 * @param player player that this hand belongs to
	 * @param cards cards in this hand
	 */
	public Hand(CardGamePlayer player, CardList	cards) {
		super();
		
		this.player = player;
				
		for (int i = 0; i < cards.size(); i++) {
			addCard(cards.getCard(i));
		}
		
	}
	
	/**
	 * getter function for the player variable
	 * @return the player who is playing this hand
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	/**
	 * a method for retrieving the top card of this hand
	 * @return top card of this hand
	 */
	public Card getTopCard() {
		CardList cards = this;
		int n = cards.size();
		
		for (int i = 0; i < n; i++) {
			boolean top = true;
			
			BigTwoCard x = new BigTwoCard(cards.getCard(i));
			
			for (int j = 0; j < n; j++) {
				if (i == j) continue;
				
				BigTwoCard y = new BigTwoCard(cards.getCard(j));
				
				if (x.compareTo(y) < 0) top = false;
			}
			
			if (top) return x;
		}
		
		return null;
	}
	
	/**
	 * compares this hand with the given hand
	 * @param hand hand that is being compared to
	 * @return true if the hand beats this hand, false otherwise
	 */
	public boolean beats(Hand hand) {	
		
		if (hand.getType() == "Single" || hand.getType() == "Pair" || hand.getType() == "Triple") {
			return ((BigTwoCard) this.getTopCard()).compareTo(hand.getTopCard()) >= 0;
		}

		if (this.getType() != hand.getType()) return this.getCombinationPower() > hand.getCombinationPower();
		else return ((BigTwoCard) this.getTopCard()).compareTo(hand.getTopCard()) > 0;
	}
	
	/**
	 *  a (abstract) method for checking if this is a valid hand.
	 *  @return true if it is a valid, false otherwise.
	 */
	abstract boolean isValid();
	
	/**
	 * a (abstract) method for returning a string specifying the type of this hand.
	 * @return name of hand type
	 */
	abstract String getType();

	/**
	 * function to get a power of this hand (compared to other hands with 5 cards). 
	 * @return power of this hand (-1 by default)
	 */
	public int getCombinationPower() {
		return -1;
	}
}
