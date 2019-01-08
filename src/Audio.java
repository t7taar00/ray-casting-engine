import java.io.File;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Audio
{
    public void playSound(String audioTrack, double playRate)
    {
        String musicFile = "sound/" + audioTrack + ".wav";

        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer player = new MediaPlayer(sound);
        player.setRate(playRate);
        player.play();
    }
    
    public void playMusic(Scene gameScene, String audioTrack)
    {
        String musicFile = "music/" + audioTrack + ".wav";

        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer player = new MediaPlayer(sound);
        
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
        
        MediaView mediaView = new MediaView(player);
        ((Group) gameScene.getRoot()).getChildren().add(mediaView);
    }
}