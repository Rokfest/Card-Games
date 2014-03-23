/*
 * No support for multiple decks yet
 */
package cardGames.Games;

import cardGames.Objects.*;
import java.util.ArrayList;
//Used for Console Input
import java.util.Scanner;

public class BlackJack implements CardGame {

    private static final int BLACKJACK = 21;
    private Deck deck;
    private boolean gameOver;
    private boolean playerWon;
    private Hand hands[];
    //Variables to make code easier to read
    private int dealerIndex;
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
            // First hand is Player
            if (i == 0) {
                this.hands[i] = new Hand(false);
            } else {
                this.hands[i] = new Hand(true);
            }
        }
        this.dealerIndex = this.hands.length - 1;
        initHands();
    }

    // Single Player Game (Player Vs. Dealer)
    public BlackJack() {
        this(1);
    }

    private void initHands() {
        for (int i = 0; i < this.hands.length; i++) {
            // 2 Cards at first draw
            for (int j = 0; j < 2; j++) {
                hit(i);
            }
            if (this.hands[i].getValue() == BLACKJACK) {
                this.hands[i].state = STATE.BLACKJACK;
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
            // Keep Player Turn while state is normal
            while (hands[i].state == STATE.NORMAL) {
                if (hands[i].auto) {
                    // Automated Player Code
                    if (i == dealerIndex) {
                        // Last Hand = Dealer Code
                        System.out.println("Dealer Hand: " + this.hands[i]);
                        dealerPlay(i);
                    } else {
                        //Autoplayer Code
                        System.out.println("Computer " + i + " Hand: " + this.hands[i]);
                        autoPlay(i);
                    }
                } else {
                    // Player Code
                    System.out.println("Dealer Face Up: " + this.hands[dealerIndex].hand.get(0)
                            + "\nPlayer Hand: " + this.hands[i]);
                    humanPlay(i);
                }
            }
        }

        //Print out who won Vs. Dealer
        int dealer = this.hands[dealerIndex].getValue();
        System.out.println("\n" + this.hands[dealerIndex]);
        for (int i = 0; i < dealerIndex; i++) {
            if (this.hands[i].state == STATE.BUST) {
                if (i == 0) {
                    System.out.println("Player bust... " + this.hands[i]);
                } else {
                    System.out.println("Computer " + i + " bust... " + this.hands[i]);
                }
            } else if (this.hands[i].state == STATE.FOLD) {
                if (i == 0) {
                    System.out.println("Player folded... " + this.hands[i]);
                } else {
                    System.out.println("Computer " + i + " folded... " + this.hands[i]);
                }
            } else if ((this.hands[dealerIndex].state == STATE.BUST
                    || this.hands[i].getValue() > dealer)
                    || this.hands[i].state == STATE.BLACKJACK) {
                if (i == 0) {
                    System.out.println("Player won! " + this.hands[i]);
                } else {
                    System.out.println("Computer " + i + " won! " + this.hands[i]);
                }
            } else if (this.hands[i].getValue() == dealer) {
                if (i == 0) {
                    System.out.println("Player tied. " + this.hands[i]);
                } else {
                    System.out.println("Computer " + i + " tied. " + this.hands[i]);
                }
            } else {
                if (i == 0) {
                    System.out.println("Player lost... " + this.hands[i]);
                } else {
                    System.out.println("Computer " + i + " lost... " + this.hands[i]);
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
        Hand dealer = this.hands[dealerIndex];

        // When dealer shown card is high
        if (dealer.getValue(true) >= HIGH) {
            if (current.getValue() >= (HIGH * 2)) {
                current.state = STATE.STAND;
            } else {
                current.state = hit(index);
            }
        } // When dealer card is mid
        else if (dealer.getValue(true) >= MID) {
            if (current.getValue() >= MID * 3) {
                current.state = STATE.STAND;
            } else {
                current.state = hit(index);
            }
        } // When dealer card is low
        else {
            if (current.getValue() > HIGH + MID) {
                current.state = STATE.STAND;
            } else {
                current.state = hit(index);
            }
        }
    }

    private void dealerPlay(int index) {
        Hand dealer = this.hands[index];

        int max = 0;

        for (int i = 0; i < dealerIndex; i++) {
            if (this.hands[i].getValue() > max) {
                max = this.hands[i].getValue();
            }
        }

        while ((dealer.getValue() < max && dealer.getValue() <= 17)
                && dealer.state != STATE.BUST) {
            dealer.state = hit(index);
        }

        if (dealer.state == STATE.NORMAL) {
            dealer.state = STATE.STAND;
        }
    }

    /* Currently uses Console input for player logic, but can be adapted
     * for other types on input later when GUI is implemented.
     */
    private void humanPlay(int index) {
        Hand current = this.hands[index];
        
        Scanner scan = new Scanner(System.in);
        char input = 0;
        while(input == 0) {
            System.out.print("'H' for Hit, 'S' for Stand, 'F' for Fold: ");
            input = scan.next().toUpperCase().charAt(0);
            
            switch(input) {
                case 'H':
                    current.state = hit(index);
                    break;
                case 'S':
                    current.state = STATE.STAND;
                    break;
                case 'F':
                    current.state = STATE.FOLD;
                    break;
                default:
                    input = 0;
            }
        }
    }
    private STATE hit(int index) {
        Hand current = this.hands[index];
        current.hand.add(this.deck.drawCard());

        if (current.getValue() > BLACKJACK) {
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
            this.hand = new ArrayList<>();
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
                } else if (card.rank.getValue() >= 10) {

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

        @Override
        public String toString() {
            return this.hand.toString() + " Value: " + getValue() + " State: "
                    + this.state;
        }
    }
}
