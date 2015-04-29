package poker;

import java.util.Comparator;


public class HandComparator implements Comparator<PokerHand>
{
	private static final int HANDSIZE = 5;

	public int compare(PokerHand hand1, PokerHand hand2) {
		if (hand1.getHandStrength() > hand2.getHandStrength()) {
			return -1;
		} else if (hand1.getHandStrength() < hand2.getHandStrength()) {
			return 1;
		} else {
			for (int i = 0; i < HANDSIZE; i++) {
				if (hand1.getCards().get(i).getValue() > hand2.getCards().get(i).getValue()) {
					return -1;
				} else if (hand1.getCards().get(i).getValue() < hand2.getCards().get(i).getValue()) {
					return 1;
				}
			}
			return 0;
		}
	}
}

