package canfield;

import ucb.gui.TopLevel;
import ucb.gui.LayoutSpec;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/** A top-level GUI for Canfield solitaire.
 *  @author P. N. Hilfinger */
class CanfieldGUI extends TopLevel {

    /** A new window with given TITLE and displaying GAME. */
    CanfieldGUI(String title, Game game) {
        super(title, true);
        _game = game;

        _display = new GameDisplay(game);
        addLabel("Canfield Solitaire 1.0",
            new LayoutSpec("y", 0, "x", 0, "itop", 10, "ibottom", 10));

        addMenuButton("Menu->New Game", "clean");
        addMenuButton("Menu->Undo Move", "undo");
        addMenuButton("Menu->Quit", "quit");

        add(_display, new LayoutSpec("y", 1, "width", 10));

        _display.setMouseHandler("click", this, "mouseClicked");
        _display.setMouseHandler("release", this, "mouseReleased");
        _display.setMouseHandler("drag", this, "mouseDragged");

        display(true);
    }

    /** Respond to "Quit" button. */
    public void quit(String dummy) {
        if (showOptions("Do you want to quit the game?", "Quit?", "question",
                        "Yes", "Yes", "No") == 0) {
            System.exit(1);
        }
    }

    /** Respond to "Quit" button. */
    public void clean(String dummy) {
        _game.deal();
        _display.repaint();
    }

    /** Undo to the previous move. */
    public void undo(String dummy) {
        _game.undo();
        _display.repaint();
    }

    /** Action in response to mouse-clicking event EVENT. */
    public synchronized void mouseClicked(MouseEvent event) {
        int x = event.getX(), y = event.getY();
        _clickedPoint = new Point(x, y);
        if (_game.STOCK_PILE.contains(_clickedPoint)) {
            _game.stockToWaste();
        } else {
            _clickedPoint = null;
        }
        _display.repaint();
    }

    /** Helper function for released Foundation. */
    public void releasedFoundationAction() {
        if (fromTableau) {
            _game.tableauToFoundation(tableauPile);
        } else if (fromWaste) {
            _game.wasteToFoundation();
        } else {
            _game.reserveToFoundation();
        }
    }

    /** Helper function for released Tableau.
     * @param index "index" of tableau pile"
     */
    public void releasedTableauAction(int index) {
        if (fromFoundation) {
            _game.foundationToTableau(foundationPile, index);
        } else if (fromTableau) {
            _game.tableauToTableau(tableauPile, index);
        } else if (fromWaste) {
            _game.wasteToTableau(index);
        } else {
            _game.reserveToTableau(index);
        }
    }

    /** Action in response to mouse-released event EVENT. */
    public synchronized void mouseReleased(MouseEvent event) {
        if (_clickedPoint != null && _draggedPoint != null) {
            try {
                if (_game.FOUNDATION_PILE1.contains(_draggedPoint)) {
                    releasedFoundationAction();
                }
                if (_game.FOUNDATION_PILE2.contains(_draggedPoint)) {
                    releasedFoundationAction();
                }
                if (_game.FOUNDATION_PILE3.contains(_draggedPoint)) {
                    releasedFoundationAction();
                }
                if (_game.FOUNDATION_PILE4.contains(_draggedPoint)) {
                    releasedFoundationAction();
                }
                if (_game.TABLEAU_PILE1.contains(_draggedPoint)) {
                    releasedTableauAction(1);
                }
                if (_game.TABLEAU_PILE2.contains(_draggedPoint)) {
                    releasedTableauAction(2);
                }
                if (_game.TABLEAU_PILE3.contains(_draggedPoint)) {
                    releasedTableauAction(3);
                }
                if (_game.TABLEAU_PILE4.contains(_draggedPoint)) {
                    releasedTableauAction(4);
                }
                _clickedPoint = null;
            } catch (IllegalArgumentException e) {
            }

            tableauPile = 0;
            foundationPile = 0;
            fromFoundation = false;
            fromTableau = false;
            fromWaste = false;

            _display.setCardLocation(null);
            _game.setClickedCard(null);

            _draggedPoint = null;
            _display.repaint();
        }
    }

    /** Helper function for dragged Foundation.
     * @param r "Rectangle Box"
     * @param index "index" of foundation pile
     */
    public void draggedFoundation(Rectangle r, int index) {
        foundationPile = index;
        fromFoundation = true;
        _upperLeftPoint = new Point(r.x, r.y);
    }

    /** Helper function for dragged Tableau.
    * @param r "Rectangle Box"
    * @param index "index" of Tableau Pile
    */
    public void draggedTableau(Rectangle r, int index) {
        tableauPile = index;
        fromTableau = true;
        _upperLeftPoint = new Point(r.x, r.y);
    }

    /*/** Action in response to mouse-dragging event EVENT. */
    public synchronized void mouseDragged(MouseEvent event) {
        int x = event.getX(), y = event.getY();
        int dX = 0, dY = 0;
        if (_draggedPoint == null) {
            _draggedPoint = new Point(x, y);
            _clickedPoint = new Point(x, y);
            _movePoint = new Point(x, y);
            if (_game.FOUNDATION_PILE1.contains(_clickedPoint)) {
                _game.setClickedCard(_game.topFoundation(1));
                draggedFoundation(_game.FOUNDATION_PILE1, 1);
            } else if (_game.FOUNDATION_PILE2.contains(_clickedPoint)) {
                _game.setClickedCard(_game.topFoundation(2));
                draggedFoundation(_game.FOUNDATION_PILE2, 2);
            } else if (_game.FOUNDATION_PILE3.contains(_clickedPoint)) {
                _game.setClickedCard(_game.topFoundation(3));
                draggedFoundation(_game.FOUNDATION_PILE3, 3);
            } else if (_game.FOUNDATION_PILE4.contains(_clickedPoint)) {
                _game.setClickedCard(_game.topFoundation(4));
                draggedFoundation(_game.FOUNDATION_PILE4, 4);
            } else if (_game.TABLEAU_PILE1.contains(_clickedPoint)) {
                _game.setClickedCard(_game.topTableau(1));
                draggedTableau(_game.TABLEAU_PILE1, 1);
            } else if (_game.TABLEAU_PILE2.contains(_clickedPoint)) {
                _game.setClickedCard(_game.topTableau(2));
                draggedTableau(_game.TABLEAU_PILE2, 2);
            } else if (_game.TABLEAU_PILE3.contains(_clickedPoint)) {
                _game.setClickedCard(_game.topTableau(3));
                draggedTableau(_game.TABLEAU_PILE3, 3);
            } else if (_game.TABLEAU_PILE4.contains(_clickedPoint)) {
                _game.setClickedCard(_game.topTableau(4));
                draggedTableau(_game.TABLEAU_PILE4, 4);
            } else if (_game.WASTE_PILE.contains(_clickedPoint)) {
                _game.setClickedCard(_game.topWaste());
                _upperLeftPoint = new Point(_game.WASTE_PILE.x,
                    _game.WASTE_PILE.y);
                fromWaste = true;
            } else if (_game.RESERVE_PILE.contains(_clickedPoint)) {
                _game.setClickedCard(_game.topReserve());
                _upperLeftPoint = new Point(_game.RESERVE_PILE.x,
                    _game.RESERVE_PILE.y);
            } else {
                _game.setClickedCard(null);
            }
        } else {
            _draggedPoint.move(event.getX(), event.getY());
        }
        if (_game.getClickedCard() != null) {
            dX = _clickedPoint.x - _upperLeftPoint.x;
            dY = _clickedPoint.y - _upperLeftPoint.y;
            _movePoint = new Point(_draggedPoint.x - dX,
                _draggedPoint.y - dY);
            _display.setCardLocation(new Point(_movePoint));
        }
        _display.repaint();
    }

    /** The upper left point of the rectangle pile. */
    private Point _upperLeftPoint;

    /** Actual starting point to draw card. */
    private Point _movePoint;

    /** Point being dragged. */
    private Point _draggedPoint;

    /** Last point clicked on (indicating start of new segment). */
    private Point _clickedPoint;

    /** Index of 4 Tableau Piles. */
    private int tableauPile;

    /** Index of 4 Foundation Piles. */
    private int foundationPile;

    /** Check if the clickPoint is from a Foundation Pile. */
    private boolean fromFoundation = false;

    /** Check if the clickPoint is from a Tableau Pile. */
    private boolean fromTableau = false;

    /** Check if the clickPoint is from Waste. */
    private boolean fromWaste = false;

    /** The board widget. */
    private final GameDisplay _display;

    /** The game I am consulting. */
    private final Game _game;
}
