package MiniTwitter;

import java.awt.event.ActionListener;

public interface Observer {
    void update (Observable follower, String message);
    void updateUser (UserView follower, String message);
}
