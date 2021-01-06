package menu;

import game.GameFlow;

import levels.LevelInformation;
import levels.LevelSpecificationReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The task Play game.
 *
 * @author Daniel Brodsky
 */
public class PlayGame implements Task<Void> {
    private String[] args;
    private GameFlow gameFlow;

    /**
     * Instantiates a new Play game.
     *
     * @param args     the args
     * @param gameFlow the game flow
     */
    public PlayGame(String[] args, GameFlow gameFlow) {
        this.gameFlow = gameFlow;
        this.args = args;
    }

    @Override
    public Void run() {
        LevelSpecificationReader levelReader = new LevelSpecificationReader();
        List<LevelInformation> defaultLevels;
        String defaultLevelsFile = "./resources/Default_Levels.txt";
        List<LevelInformation> chosenLevels = new ArrayList<>();
        if (args.length >= 1) {
            File f = new File(args[0]);
            if (f.exists()) {
                chosenLevels = levelReader.makeLevel(args[0]);
            }
        }
        try {
            if (chosenLevels.size() == 0) {
                defaultLevels = levelReader.makeLevel(defaultLevelsFile);
                gameFlow.runLevels(defaultLevels);
            } else {
                gameFlow.runLevels(chosenLevels);
            }
        } catch (IOException e) {
            System.out.println("Can't read level's file");
        }
        return null;
    }

}
