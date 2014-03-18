package cardGames.Games;

public interface CardGame {
    public abstract boolean isGameOver();
    public abstract boolean didPlayerWin();
    public abstract void run();
}
