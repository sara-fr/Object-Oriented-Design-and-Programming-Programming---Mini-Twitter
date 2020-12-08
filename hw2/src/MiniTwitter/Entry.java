package MiniTwitter;
import java.util.Observable;

public interface Entry<S, L extends Number> {
    String getName();
    void update(Observable follower, Object tweet);
}
