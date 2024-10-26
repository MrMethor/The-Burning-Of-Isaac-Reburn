package Enums;

public enum StateTransition {
    START_GAME,
    BACK_TO_MENU,
    RESUME_GAME,
    EXIT_GAME,
    PAUSE_GAME,
    MENU_TO_SETTINGS,
    SETTINGS_TO_MENU;

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
        return null;
    }
}
