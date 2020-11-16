package MiniTwitter;

public class Counter implements Visitable {
    private static int groupCount;
    private static int messageCount;
    private static int userCount;
    public Counter() {
    }
    public void accept_groupCount() {
        groupCount++;
    }
    public int getGroupCount() {
        return groupCount;
    }
    public void accept_messageCount() {
        messageCount++;
    }
    public int getMessageCount() {
        return messageCount;
    }
    public void accept_userCount() {
        userCount++;
    }
    public int getUserCount() {
        return userCount;
    }
}
