package MiniTwitter;
import java.util.*;
import javax.swing.*;

public abstract class Group extends JFrame implements Entry {
    private String groupName;
    private ArrayList<Entry> userList;
    private ArrayList<Entry> groupList;
    long time;
    public Group(String name, long creationTime) {
        this.groupName = name;
        this.time = creationTime;
        this.userList = new ArrayList();
        this.groupList = new ArrayList();
    }
    public void newUser(UserView user) {
        this.userList.add(user);
    }
    public void newGroup(Group group) {
        this.groupList.add(group);
    }
    public String getName() {
        return this.groupName;
    }
}
