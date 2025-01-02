package Enums;

public enum StateTransition {
    START_GAME,
    BACK_TO_MENU,
    RESUME_GAME,
    EXIT_GAME,
    PAUSE_GAME,
    MENU_TO_SETTINGS,
    SETTINGS_TO_MENU,
    DEATH,
    EXIT_DEATH,
    RESTART;

    public static StateTransition getTransition(GameState previous, GameState next) {
        if (previous == GameState.MENU && next == GameState.GAME)
            return START_GAME;
        if (previous == GameState.MENU && next == GameState.EXIT)
            return EXIT_GAME;
        if (previous == GameState.GAME && next == GameState.PAUSE)
            return PAUSE_GAME;
        if (previous == GameState.PAUSE && next == GameState.GAME)
            return RESUME_GAME;
        if (previous == GameState.PAUSE && next == GameState.MENU)
            return BACK_TO_MENU;
        if (previous == GameState.MENU && next == GameState.SETTINGS)
            return MENU_TO_SETTINGS;
        if (previous == GameState.SETTINGS && next == GameState.MENU)
            return SETTINGS_TO_MENU;
        if (previous == GameState.GAME && next == GameState.DEATH)
            return DEATH;
        if (previous == GameState.DEATH && next == GameState.MENU)
            return EXIT_DEATH;
        if (previous == GameState.DEATH && next == GameState.GAME)
            return RESTART;
        return null;
    }
}
