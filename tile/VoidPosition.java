package tile;

import game.GameError;

public class VoidPosition extends Position {

    @Override
    public String optionString(Position destination) {
        throw new GameError("This should not be called on a void position");
    }

}
