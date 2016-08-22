package view.token;

import javax.swing.ImageIcon;

import card.Weapon;
import tile.RoomTile;

/**
 * This class represents a weapon token on board. In Cluedo game, six tokens of weapon are
 * moved from room to room to represents a weapon used in murder. It remembers where to
 * draw tokens. Also, for GUI mode, each weeapon token has a custom tooltip to show a
 * better-looking tooltip in no-brainer mode.
 * 
 * @author Hector
 *
 */
@SuppressWarnings("serial")
public class WeaponToken extends AbstractToken {

    /**
     * weapon
     */
    private final Weapon weapon;

    /**
     * Construct a weapon token on board.
     * 
     * @param img
     *            --- the image used to draw this weapon token on board
     * @param token
     *            --- the weapon
     * @param roomTile
     *            --- which room tile is this token currently placed
     */
    public WeaponToken(ImageIcon img, Weapon token, RoomTile roomTile) {
        super(img, roomTile);
        this.weapon = token;
        this.setToolTipText(token.toString());
    }

    /**
     * Which weapon is it?
     * 
     * @return --- Which weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

}