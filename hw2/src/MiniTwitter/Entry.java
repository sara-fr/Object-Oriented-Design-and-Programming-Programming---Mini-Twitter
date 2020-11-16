package MiniTwitter;
import java.util.Observable;

public interface Entry {
    String getName();
    //_________________Observer_____________________
    void update(Observable o, Object arg);
}
