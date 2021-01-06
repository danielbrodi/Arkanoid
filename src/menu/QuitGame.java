package menu;

import biuoop.GUI;

/**
 * The type Quit game.
 *
 * @author Daniel Brodsky
 */
public class QuitGame implements Task<Void> {
    private GUI gui;

    /**
     * Instantiates a new Quit game task.
     *
     * @param gui the gui
     */
    public QuitGame(GUI gui) {
        this.gui = gui;
    }

    @Override
    public Void run() throws Exception {
        gui.close();
        System.exit(0);
        return null;
    }
}
