/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardGames;

import cardGames.Games.*;


/**
 *
 * @author Lab User
 */
public class CardGames {

    public static void main(String[] args) {
        //Choose game
        CardGame game = new BlackJack();
        //Run game
        while(!game.isGameOver())
        {
            game.run();
        }
    }
}
