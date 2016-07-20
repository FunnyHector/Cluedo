package game;

/**
 * This class represents a runtime exception in game.
 * 
 * @author Hector
 *
 */
public class GameError extends RuntimeException {

    // auto-generated serial version UID
    private static final long serialVersionUID = 3193391567599261380L;

    public GameError(String errMsg) {
        super(errMsg);
    }

}
