/**
 * a subclass of Hand used to model a hand of Full House
 * @author Yerdaulet
 */
public class FullHouse extends Hand {
	
	private static final long serialVersionUID = 1L;

	/**
	 * constructor for building a hand of Full House with the specified player and list of cards.
	 * @param player - player that this hand belongs to
	 * @param cards - cards in this hand
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return name of hand type
	 */
	public String getType() {
		return "FullHouse";
	}
	
	/**
	 *  a method for checking if this is a valid hand.
	 *  @return true if it is a valid Full House, false otherwise.
	 */
	public boolean isValid() {
		CardList cards = this;
		int n = cards.size();
		
		if (n != 5) return false;
		
		cards.sort();
		
		int rankCount[] = new int[15];
		
		for (int i = 0; i < n; i++) {
			rankCount[cards.getCard(i).getRank()]++;
		}
		
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (i != j && rankCount[i] == 2 && rankCount[j] == 3) return true;
			}
		}
		
		return false;
	}

	/**
	 * function to get a power of this hand (compared to other hands with 5 cards). 
	 * @return power of this hand (3)
	 */
	public int getCombinationPower() {
		return 3;
	}
}
