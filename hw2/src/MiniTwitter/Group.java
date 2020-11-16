package MiniTwitter;
import java.util.*;
import javax.swing.*;

public abstract class Group extends JFrame implements Entry {
    private String groupName;
    private ArrayList<Entry> userList;
    private ArrayList<Entry> groupList;
    public Group(String name) {
        this.groupName = name;
        this.userList = new ArrayList();
        this.groupList = new ArrayList();
    }

    /*@Override
    public void accept(Visitor visitor) {
        visitor.visit_groupCount(this);
    }*/

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
