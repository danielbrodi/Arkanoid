package menu;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import game.GameLevel;

import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The type Menu animation.
 *
 * @param <T> the type parameter
 * @author Daniel Brodsky
 */
public class MenuAnimation<T> implements Menu<T> {
    private KeyboardSensor keyboard;
    private String title;
    private static final Color[] COLORARRAY = {
            Color.darkGray.brighter(),
            Color.red,
            Color.white,
            Color.yellow,
            Color.green,
            Color.orange,
            Color.pink,
            Color.magenta};

    private int colorIndex;
    private int charWidth = 36;
    private List<Selection<T>> selections = new ArrayList<>();
    private Selection<T> theSelection;
    private boolean stop;

    /**
     * Instantiates a new Menu animation.
     *
     * @param title    the title
     * @param keyboard the keyboard
     */
    public MenuAnimation(String title, KeyboardSensor keyboard) {
        this.title = title;
        this.keyboard = keyboard;
        this.stop = false;
    }


    @Override
    public void addSelection(String key, String message, T returnVal) {
        selections.add(new Selection<T>(key, message, returnVal));
    }

    @Override
    public T getStatus() {
        if (theSelection == null) {
            return null;
        }
        T value = theSelection.getValue();
        theSelection = null;
        this.stop = false;
        return value;
    }

    /**
     * Do one frame.
     *
     * @param d the chosen Draw surface.
     */
    @Override
    public void doOneFrame(DrawSurface d) {

        //background
        d.setColor(Color.cyan.darker());
        d.fillRectangle(0, 0, (int) GameLevel.getGuiWidth(), (int) GameLevel.getGuiHeight());

        //title
        char c = ' ';
        colorIndex = 0;
        Point loc = new Point(220, 100);
        for (int i = 0; i < this.title.length(); i++) {
            c = this.title.charAt(i);
            d.setColor(COLORARRAY[colorIndex]);
            d.drawText(loc.x, loc.y, "" + c, 66);
            loc.x += charWidth;
            colorIndex++;
            if (colorIndex >= COLORARRAY.length) {
                colorIndex = 0;
            }
        }

        //options
        d.setColor(Color.black);
        int yCoordinate = 200;
        for (Selection<T> selection : selections) {
            d.drawText(200, yCoordinate, selection.getMessage(), 30);
            yCoordinate = yCoordinate + 50;
        }

        //face
        d.setColor(Color.white);
        d.fillOval(200, 565, 100, 100);
        d.fillOval(196, 595, 6, 15);
        d.fillOval(298, 595, 6, 15);
        d.setColor(Color.red.darker());
        d.fillRectangle(214, 104, 292, 5);

        // eyes
        d.setColor(Color.black);
        d.drawOval(227, 583, 15, 10);
        d.drawOval(258, 583, 15, 10);
        d.setColor(Color.blue);
        d.fillOval(231, 585, 6, 6);
        d.fillOval(262, 585, 6, 6);

        //blocks
        Color[] randomColor = new Color[7];
        randomColor[0] = Color.darkGray.brighter();
        randomColor[1] = Color.red;
        randomColor[2] = Color.yellow;
        randomColor[3] = Color.green;
        randomColor[4] = Color.white;
        randomColor[5] = Color.pink;
        randomColor[6] = Color.magenta;
        Random rand = new Random();

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 15; i++) {

                d.setColor(randomColor[rand.nextInt(6 + 1)]);
                d.fillRectangle(52 * (i + 1) - 40, 350 + (j * 35), 51, 30);
                d.setColor(Color.black);
                d.drawRectangle(52 * (i + 1) - 40, 350 + (j * 35), 51, 30);
            }
        }

        //check if pressed
        for (Selection<T> selection : selections) {
            if (keyboard.isPressed(selection.getKey())) {
                this.theSelection = selection;
                this.stop = true;
            }
        }
    }

    /**
     * Should stop the animation depends on a given stat.
     *
     * @return true of false.
     */
    @Override
    public boolean shouldStop() {
        return this.stop;
    }
}
