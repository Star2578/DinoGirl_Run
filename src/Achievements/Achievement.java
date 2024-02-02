package Achievements;

public class Achievement {
    private String name;
    private String description;
    private boolean unlocked;
    private String imagePath;

    public Achievement(String name, String description, boolean unlocked) {
        this.name = name;
        this.description = description;
        this.unlocked = unlocked;
    }

    public void condition(boolean condition) {
        setUnlocked(condition);
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public boolean getUnlocked() {
        return unlocked;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
    public String getImagePath() {
        return imagePath;
    }
}
