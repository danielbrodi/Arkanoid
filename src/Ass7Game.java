
import animation.AnimationRunner;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import game.GameFlow;
import game.GameLevel;
import menu.MenuAnimation;
import menu.PlayGame;
import menu.ShowHiScoresTask;
import menu.Task;
import menu.Menu;
import menu.QuitGame;


/**
 * Creates a game gui window, initializes the levels and runs the game.
 *
 * @author Daniel Brodsky
 */
public class Ass7Game {
    /**
     * A main method that starts a game.
     * When run without arguments, the game will run default levels.
     * When run with additional arguments,
     * the arguments will be treated as a path to a file that sets up levels to run,
     * in the specified order. Any argument which is not a valid path to a valid file,
     * will be ignored.
     * Sets up selections for a start menu for different tasks.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        GUI gui = new GUI("Arkanoid", (int) GameLevel.getGuiWidth(), (int) GameLevel.getGuiHeight());
        KeyboardSensor ks = gui.getKeyboardSensor();
        AnimationRunner ar = new AnimationRunner(gui);
        GameFlow game = new GameFlow(ar, ks);
        Menu<Task<Void>> menu = new MenuAnimation<>("Arkanoid", ks);
        menu.addSelection("h", "Press <h> to see the highest score", new ShowHiScoresTask(ar, ks));
        menu.addSelection("s", "Press <s> to start a new game", new PlayGame(args, game));
        menu.addSelection("q", "Press <q> to quit", new QuitGame(gui));
        while (true) {
            ar.run(menu);
            Task<Void> result = menu.getStatus();
            result.run();
        }
    }
}
