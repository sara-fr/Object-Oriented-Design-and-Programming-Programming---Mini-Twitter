package MiniTwitter;
import java.util.Observable;

public interface Entry {
    String getName();
    void update(Observable follower, Object tweet);
}
