/*
 * No support for multiple decks yet
 */
package cardGames.Games;

import cardGames.Objects.*;
import java.util.ArrayList;

public class BlackJack implements CardGame {

	private static final int BLACKJACK = 21;
	private Deck deck;
	private boolean gameOver;
	private boolean playerWon;
	private Hand hands[];

	/*
	 * Multiplayer Game (Multiple Players Vs. Dealer) Only end game when all
	 * players are done.
	 */
	public BlackJack(int players) {
		this.deck = new Deck(true);
		this.gameOver = false;
		this.playerWon = false;

		// +1 for dealer hand
		this.hands = new Hand[players + 1];
		for (int i = 0; i < this.hands.length; i++) {
			// Last hand is always dealer
			if (i != this.hands.length - 1) {
				this.hands[i] = new Hand(false);
			} else {
				this.hands[i] = new Hand(true);
			}
		}
		initHands();
	}

	// Single Player Game (Player Vs. Dealer)
	public BlackJack() {
		this(1);
	}

	private void initHands() {
		for (Hand player : this.hands) {
			// 2 Cards at first draw
			for (int i = 0; i < 2; i++) {
				player.hand.add(this.deck.drawCard());
				if (player.getValue() == BLACKJACK) {
					player.state = STATE.BLACKJACK;
				}
			}
		}
	}

	// Interface Methods
	@Override
	public boolean isGameOver() {
		return this.gameOver;
	}

	@Override
	public boolean didPlayerWin() {
		return this.playerWon;
	}

	@Override
	public void run() {
		// Player Turns are defined here
		for (int i = 0; i < this.hands.length; i++) {
			if (hands[i].auto) {
				System.out.println("Computer Hand: " + hands[i].toString());
			} else {
				System.out.println("Player Hand: " + hands[i].toString());
			}
			// Keep Player Turn while state is normal
			while (hands[i].state == STATE.NORMAL) {
				if (hands[i].auto) {
					// Automated Player Code
					if (i == this.hands.length - 1) {
						// Last Hand = Dealer Code
						hands[i].state = STATE.STAND;
					} else {
						autoPlay(i);
						// Auto Player Code
						hands[i].state = STATE.STAND;
					}
				} else {
					// Player Code
					hands[i].state = STATE.STAND;
				}
			}
		}
		System.out.println("Game is not ready");
		this.playerWon = true;
		this.gameOver = true;
	}

	private static final int HIGH = 9;
	private static final int MID = 5;

	private void autoPlay(int index) {
		Hand current = this.hands[index];
		Hand dealer = this.hands[this.hands.length - 1];

		// When dealer shown card is high
		if (dealer.getValue(true) >= HIGH) {
			if (current.getValue() >= (HIGH * 2)) {
				current.state = STATE.STAND;
			} else {
				current.state = hit(index);
			}
		}
		// When dealer card is mid
		else if (dealer.getValue(true) >= MID) {
			if(current.getValue() >= MID * 3) {
				current.state = STATE.STAND;
			} else {
				current.state = hit(index);
			}
		}
		// When dealer card is low
		else {
			if(current.getValue() > HIGH + MID) {
				current.state = STATE.STAND;
			}
			else {
				current.state = hit(index);
			}
		}
	}
	
	private STATE hit(int index) {
		Hand current = this.hands[index];
		current.hand.add(this.deck.drawCard());
		
		if(current.getValue() > BLACKJACK) {
			return STATE.BUST;
		}
		
		return STATE.NORMAL;
	}

	// Private Class
	private enum STATE {

		NORMAL, BLACKJACK, FOLD, STAND, BUST
	}

	private class Hand {

		public ArrayList<Card> hand;
		public STATE state;
		// This gives future room for automated players
		public boolean auto;

		public Hand(boolean auto) {
			this.hand = new ArrayList<Card>();
			this.state = STATE.NORMAL;

			this.auto = auto;
		}

		public int getValue(boolean dealer) {
			if (!dealer) {
				return getValue();
			}

			Hand shownCard = new Hand(true);
			shownCard.hand.add(this.hand.get(0));
			return shownCard.getValue();
		}

		public int getValue() {
			int value = 0;
			int aceFlag = 0;
			for (Card card : this.hand) {
				if (card.rank.compareTo(Card.RANK.ACE) == 0) {
					aceFlag++;
					value += 11;
				}
				if (card.rank.getValue() >= 10) {

					value += 10;
				} else {
					value += card.rank.getValue();
				}
			}

			// Handle Ace && Value over 21 Overflow
			if (value > BLACKJACK && aceFlag > 0) {
				for (int i = 0; i < aceFlag; i++) {
					value -= 10;
					if (value <= BLACKJACK) {
						break;
					}
				}
			}

			return value;
		}

		public String toString() {
			return this.hand.toString() + " Value: " + getValue() + " State: "
					+ this.state;
		}
	}
}
