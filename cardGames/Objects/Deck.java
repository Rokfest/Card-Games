/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGames.Objects;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    //private static final int CARDS_PER_DECK = 52;
    private ArrayList<Card> deck;

    public Deck() {
        this.deck = new ArrayList<>();
        generateCards();
    }

    public Deck(boolean shuffle) {
        this();
        shuffle();
    }

    //PRIVATE METHODS
    private void generateCards() {
        for (Card.SUIT suit : Card.SUIT.values()) {
            for (Card.RANK rank : Card.RANK.values()) {
                this.deck.add(new Card(suit, rank));
            }
        }
    }
    //PUBLIC METHODS

    public void shuffle() {
        Random rand = new Random();
        ArrayList<Card> temp = new ArrayList<Card>();

        while (!this.deck.isEmpty()) {
            int select = rand.nextInt(this.deck.size());
            temp.add(this.deck.remove(select));
        }

        this.deck = temp;
    }

    @SuppressWarnings("unchecked")
	public ArrayList<Card> getDeck() {
    	return (ArrayList<Card>)this.deck.clone();
    }
    
    public Card drawCard()
    {
        if(!deck.isEmpty())
            return this.deck.remove(0);
        return null;
    }
    
    public boolean isEmpty()
    {
        return this.deck.isEmpty();
    }

    public String toString() {
        return this.deck.toString();
    }
}
