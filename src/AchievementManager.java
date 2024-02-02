import Achievements.*;

import java.util.ArrayList;
import java.util.List;

public class AchievementManager {
    public static AchievementManager instance;
    private final GameManager gameManager = GameManager.getInstance();
    private List<Achievement> achievements;

    private AchievementManager() {
        gameManager.initialize(this);
        achievements = new ArrayList<>();
    }

    public static AchievementManager getInstance() {
        if (instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }
}
