/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGames.Objects;

public class Card {

    public enum RANK {

        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
        EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);
        private final int value;

        RANK(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum SUIT {

        CLUB('C'), DIAMOND('D'), SPADE('S'), HEART('H');
        private final char value;
        
        SUIT(char value) {
            this.value = value;
        }

        public char getValue() {
            return value;
        }
    }
    public SUIT suit;
    public RANK rank;

    public Card(SUIT suit, RANK rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String toString() {
        return this.suit.getValue() + "" + this.rank.getValue();
    }
}