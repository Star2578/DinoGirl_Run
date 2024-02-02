import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.Random;

public class SoundManager {
    private static SoundManager instance;
    private final GameManager gameManager = GameManager.getInstance();
    private AudioInputStream audioInputStream;
    private Clip backgroundMusic;
    private Thread backgroundMusicThread;
    public final String[] gameOverSounds = {
            "src/Sounds/Hurt/hurt_001.wav", "src/Sounds/Hurt/hurt_002.wav", "src/Sounds/Hurt/hurt_003.wav",
            "src/Sounds/Hurt/hurt_004.wav", "src/Sounds/Hurt/hurt_005.wav"
    };

    public final String clickingSound = "src/Sounds/Clicking.wav";
    public final String jumpingSound = "src/Sounds/Jump.wav";
    public String backgroundMusicPath = "src/Sounds/Ludum Dare 30 - Track 4.wav";

    private SoundManager() {
        gameManager.initialize(this);
    }

    // Public method to get the singleton instance
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    public void playSoundEffect(String filePath) {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));

            // Open an audio input stream
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));

            // Get a clip resource
            Clip clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream
            clip.open(audioInputStream);

            adjustSoundEffectVolume(clip, gameManager.getSoundEffectVolume());

            // Play the sound in a separate thread
            new Thread(() -> {
                clip.start();
                try {
                    // Sleep for a while to allow the sound to play
                    Thread.sleep(10000); // Adjust the duration as needed
                } catch (InterruptedException e) {
                    System.out.println("Error with playing sound in new Thread: " + e.getMessage());
                } finally {
                    // Stop the sound
                    clip.stop();
                }
            }).start();
        } catch (Exception e) {
            System.out.println("Error with playing sound: " + e.getMessage());
        }
    }

    public void playBackgroundMusic(String filePath) {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));

            // Get a clip resource
            backgroundMusic = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream
            backgroundMusic.open(audioInputStream);

            // Set the clip to loop indefinitely
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

            adjustBackgroundMusicVolume(gameManager.getBackgroundMusicVolume());

            // Start playing the background music in a new thread
            backgroundMusicThread = new Thread(() -> backgroundMusic.start());
            backgroundMusicThread.start();
        } catch (Exception e) {
            System.out.println("Error with playing background music: " + e.getMessage());
        }
    }

    public void changeBackgroundMusic(String filePath) {
        // Stop the current background music
        stopBackgroundMusic();

        // Play the new background music
        playBackgroundMusic(filePath);
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }

        if (backgroundMusicThread != null && backgroundMusicThread.isAlive()) {
            try {
                backgroundMusicThread.join(); // Wait for the thread to finish
            } catch (InterruptedException e) {
                System.out.println("Error when stopping background music:" + e.getMessage());
            }
        }
    }

    public void adjustBackgroundMusicVolume(float volume) {
        if (backgroundMusic != null && backgroundMusic.isOpen()) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
        }
    }

    public void adjustSoundEffectVolume(Clip clip, float volume) {
        if (clip != null && clip.isOpen()) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
        }
    }

    public String randomSFX(String[] choices) {
        String sfx;
        Random random = new Random();
        int choose = random.nextInt(0, choices.length);
        sfx = choices[choose];

        return sfx;
    }
}
