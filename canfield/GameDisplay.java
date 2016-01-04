package canfield;

import ucb.gui.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import javax.imageio.ImageIO;

import java.io.InputStream;
import java.io.IOException;

/** A widget that displays a Pinball playfield.
 *  @author Truong Pham
 */
class GameDisplay extends Pad {

    /** Color of display field. */
    private static final Color BACKGROUND_COLOR = Color.white;

    /** Preferred dimensions of the playing surface. */
    private static final int BOARD_WIDTH = 1010, BOARD_HEIGHT = 800;

    /** Displayed dimensions of a card image. */
    private static final int CARD_HEIGHT = 125, CARD_WIDTH = 90;

    /** Text size. */
    private static final int FONT_SIZE = 18;

    /** Position of TextLabel for Foundation Piles. */
    private static final Point TEXT_LABEL1 = new Point(475, 135);

    /** Position of TextLabel for Foundation Piles. */
    private static final Point TEXT_LABEL2 = new Point(475, 335);

    /** Position of TextLabel for Foundation Piles. */
    private static final Point TEXT_LABEL3 = new Point(100, 335);

    /** Position of TextLabel for Foundation Piles. */
    private static final Point TEXT_LABEL4 = new Point(100, 535);

    /** Position of TextLabel for Foundation Piles. */
    private static final Point TEXT_LABEL5 = new Point(210, 535);

    /** Necessary offset for locating Tableau and Foundation Piles. */
    private static final int OFFSET1 = 20;

    /** Necessary offset for locating Tableau and Foundation Piles. */
    private static final int OFFSET2 = 25;

    /** Backround for the game display. */
    private Image img =
        Toolkit.getDefaultToolkit().createImage("canfield/background.png");

    /** A graphical representation of GAME. */
    public GameDisplay(Game game) {
        _game = game;
        setPreferredSize(BOARD_WIDTH, BOARD_HEIGHT);
    }

    /** Return an Image read from the resource named NAME. */
    private Image getImage(String name) {
        InputStream in =
            getClass().getResourceAsStream("/canfield/resources/" + name);
        try {
            return ImageIO.read(in);
        } catch (IOException excp) {
            return null;
        }
    }
    /** Return an Image of CARD. */
    private Image getCardImage(Card card) {
        return getImage("playing-cards/" + card + ".png");
    }
    /** Return an Blank Image for the blank space. */
    private Image getBlankImage() {
        return getImage("playing-cards/blank-space.png");
    }
    /** Return an Image of the back of a card. */
    private Image getBackImage() {
        return getImage("playing-cards/red-back.png");
    }
    /** Draw CARD at X, Y on G. */
    private void paintCard(Graphics2D g, Card card, int x, int y) {
        if (card != null) {
            g.drawImage(getCardImage(card), x, y,
                        CARD_WIDTH, CARD_HEIGHT, null);
        } else {
            g.drawImage(getBlankImage(), x, y,
                        CARD_WIDTH, CARD_HEIGHT, null);
        }
    }
    /** Draw card back at X, Y on G. */
    private void paintBack(Graphics2D g, int x, int y) {
        g.drawImage(getBackImage(), x, y, CARD_WIDTH, CARD_HEIGHT, null);
    }
    /** Draw Waste Pile on G. */
    private void paintWaste(Graphics2D g) {

        if (_game.topWaste() == _game.getClickedCard()) {
            paintCard(g, _game.secondTopWaste(),
                _game.WASTE_PILE.x, _game.WASTE_PILE.y);
        } else {
            paintCard(g, _game.topWaste(),
                _game.WASTE_PILE.x, _game.WASTE_PILE.y);
        }
    }
    /** Draw Reserve Pile on G. */
    private void paintReserve(Graphics2D g) {

        if (_game.topReserve() == _game.getClickedCard()) {
            paintCard(g, _game.secondTopWaste(),
                _game.RESERVE_PILE.x, _game.RESERVE_PILE.y);
        } else {
            paintCard(g, _game.topReserve(),
                _game.RESERVE_PILE.x, _game.RESERVE_PILE.y);
        }
    }
    /** Draw Tableau Piles on G. */
    private void paintTableau(Graphics2D g) {
        for (int i = 1; i <= Game.TABLEAU_SIZE; i++) {
            for (int j = _game.tableauSize(i) - 1; j >= 0; j--) {
                if (_game.getTableau(i, j) != _game.getClickedCard()) {
                    paintCard(g, _game.getTableau(i, j),
                        CARD_WIDTH * 4 + CARD_WIDTH * i + i * OFFSET2,
                            _game.TABLEAU_PILE1.y
                            + (_game.tableauSize(i) - j - 1) * OFFSET1);
                }
            }
        }
    }
    /** Draw Texts on G. */
    private void drawText(Graphics2D g) {
        g.setFont(new Font("Helvetica", Font.PLAIN, FONT_SIZE));
        g.setColor(Color.white);
        g.drawString("Foundation Piles", TEXT_LABEL1.x, TEXT_LABEL1.y);
        g.drawString("Tableau", TEXT_LABEL2.x, TEXT_LABEL2.y);
        g.drawString("Reserve", TEXT_LABEL3.x, TEXT_LABEL3.y);
        g.drawString("Stock", TEXT_LABEL4.x, TEXT_LABEL4.y);
        g.drawString("Waste", TEXT_LABEL5.x, TEXT_LABEL5.y);
    }

    /** Getter of _waste.
    * @return Point.
    */
    public Point getCardLocation() {
        return _cardLocation;
    }

    /** Getter of _reserve.
    * @param p "set location to p.
    */
    public void setCardLocation(Point p) {
        _cardLocation = p;
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        Rectangle b = g.getClipBounds();
        g.fillRect(0, 0, b.width, b.height);
        g.drawImage(img, 0, 0, BOARD_WIDTH, BOARD_HEIGHT, null);
        drawText(g);

        for (int i = 1; i <= Card.NUM_SUITS; i++) {
            paintCard(g, _game.topFoundation(i),
                CARD_WIDTH * 4 + CARD_WIDTH * i + i * OFFSET2,
                    _game.FOUNDATION_PILE1.y);
        }

        paintTableau(g);

        paintReserve(g);

        paintWaste(g);

        paintBack(g, _game.STOCK_PILE.x, _game.STOCK_PILE.y);

        if (_cardLocation != null) {
            paintCard(g, _game.getClickedCard(),
                _cardLocation.x, _cardLocation.y);
        }
    }

    /** Game I am displaying. */
    private final Game _game;

    /** Location of "draw" card. */
    private Point _cardLocation;
}
