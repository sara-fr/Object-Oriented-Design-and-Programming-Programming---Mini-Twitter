package MiniTwitter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class UserView extends JFrame implements Entry, Observer {
    private JPanel UserPanel;
    private JComboBox userIdsList;
    private JButton followUserButton;
    private JList currentFollowingList;
    private JTextArea tweetMessageTextArea;
    private JButton postTweetButton;
    private JList newsList;
    private JLabel currentFollowingLabel;
    private JLabel newsFeedLabel;
    private ArrayList<UserView> followingNameList = new ArrayList<>();
    private ArrayList<UserView> followerNameList = new ArrayList<>();
    protected ArrayList<String> TweetList = new ArrayList<>();
    String id;
    Group group = new Group("root") {
        @Override
        public void update(Observable user, Object text) {
        }
    };

    public UserView(String id) {
        //initialComponents();
        setBounds(100, 100, 700, 500);
        this.id = id;
        this.setTitle("User: " + id);
        this.setContentPane(UserPanel);
        this.pack();
        updateTweets();

        postTweetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tweet = tweetMessageTextArea.getText();
                tweet(tweet);
                Counter tweetTotal = new Counter();
                tweetTotal.accept_messageCount();
                for (UserView userId : followingNameList) {
                    userId.updateTweets();
                }
                tweetMessageTextArea.setText("");
            }
        });
        followUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserView follower = (UserView) userIdsList.getSelectedItem();
                followingNameList.add(follower);
                follower.followerNameList.add(UserView.this);
                userIdsList.removeItem(follower);
                updateFollowingList();
            }
        });
    }
    public void updateTweets() {
        DefaultListModel newsFeedModel = new DefaultListModel();
        for (UserView userId : followingNameList) {
            for (String message : userId.TweetList) {
                newsFeedModel.addElement(userId + ": " + message);
            }
        }
        for (String tweet : TweetList) {
            newsFeedModel.addElement(tweet);
        }
        newsList.setModel(newsFeedModel);
    }
    public void updateFollowingList() {
        DefaultListModel defaultListModel = new DefaultListModel();
        for (UserView userId : followingNameList) {
            defaultListModel.addElement(userId);
        }
        currentFollowingList.setModel(defaultListModel);
    }
    public void updateUserIdList() {
        userIdsList.removeAllItems();
        HashMap<String, UserView> s = new HashMap<String, UserView>();
        s.putAll((AdminControlPanel.users));
        for (UserView userId : s.values()) {
            userIdsList.addItem(userId);
        }
        for (UserView userId : followingNameList) {
            userIdsList.removeItem(userId);
        }
    }
    @Override
    public String toString() {
        return id;
    }
    public void tweet(String tweet) {
        TweetList.add(tweet);
        updateTweets();
    }
    public JComboBox getAllUserList() {
        return userIdsList;
    }
    public void setUserIdsList(JComboBox userIdsList) {
        this.userIdsList = userIdsList;
    }
    public JList getCurrentFollowingList() {
        return currentFollowingList;
    }
    public void setCurrentFollowingList(JList currentFollowingList) {
        this.currentFollowingList = currentFollowingList;
    }
    public JTextArea getTweetMessageTextArea() {
        return tweetMessageTextArea;
    }
    public void setTweetMessageTextArea(JTextArea tweetMessageTextArea) {
        this.tweetMessageTextArea = tweetMessageTextArea;
    }
    public JList getNewsList() {
        return newsList;
    }
    public void setNewsList(JList newsList) {
        this.newsList = newsList;
    }
    @Override
    public void update (Observable follower, Object message) {
        updateUserIdList();
        updateTweets();
        updateFollowingList();
    }
    public void updateUser (UserView follower, String message) {
        updateUserIdList();
        updateTweets();
        updateFollowingList();
        setVisible(this.isShowing());
    }
     private void initialComponents() {
         setBounds(100, 100, 450, 450);
         UserPanel = new JPanel();
         setContentPane(UserPanel);
         UserPanel.setLayout(null);
         userIdsList = new JComboBox();
         userIdsList.setBounds(10,10,50,50);
         UserPanel.add(userIdsList);
         followUserButton = new JButton("Follow User");
         followUserButton.setBounds(300,10,120,30);
         UserPanel.add(followUserButton);
         currentFollowingList = new JList();
         currentFollowingList.setBounds(10,70,420,150);
         UserPanel.add(currentFollowingList);
         currentFollowingLabel = new JLabel("Current Following");
         currentFollowingLabel.setBounds(10,50,180,20);
         UserPanel.add(currentFollowingLabel);
         tweetMessageTextArea = new JTextArea();
         tweetMessageTextArea.setBounds(10,220,280,30);
         UserPanel.add(tweetMessageTextArea);
         postTweetButton = new JButton("Post Tweet");
         postTweetButton.setBounds(300,250,120,30);
         UserPanel.add(postTweetButton);
         newsFeedLabel = new JLabel("News Feed");
         newsFeedLabel.setBounds(10,300,400,120);
         UserPanel.add(newsFeedLabel);
         newsList = new JList();
         newsList.setBounds(10,300,400,120);
         UserPanel.add(newsList);
     }
}
