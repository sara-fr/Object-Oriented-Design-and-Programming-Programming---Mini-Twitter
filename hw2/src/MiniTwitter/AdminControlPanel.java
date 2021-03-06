package MiniTwitter;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Observable;

public class AdminControlPanel extends JFrame implements Visitor {
    private JPanel mainPanel;
    private JTree treeView;
    private JTextArea userIdTextArea;
    private JTextArea groupIdTextArea;
    private JButton addUserButton;
    private JButton addGroupButton;
    private JButton openUserViewButton;
    private JButton totalUserButton;
    private JButton totalGroupButton;
    private JButton totalMessagesButton;
    private JButton positivePercentageButton;
    private JButton IDVerificationButton;
    private JButton lastUpdatedUserButton;
    private String userName;
    private String groupName;
    private DefaultTreeModel treeModel = (DefaultTreeModel) treeView.getModel();
    private DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
    private DefaultMutableTreeNode currentNode;
    private static AdminControlPanel getAdminInstance;
    public static HashMap<String, UserView> users = new HashMap();
    public static HashMap<String, Group> userGroups = new HashMap();
    private boolean duplicatedUserID = false;
    private boolean duplicatedGroupID = false;
    private boolean whiteSpace = false;

    public static AdminControlPanel getAdminInstance() {
        if (getAdminInstance == null) {
            getAdminInstance = new AdminControlPanel();
        }
        return getAdminInstance;
    }
    private AdminControlPanel() {
        //initialComponents();
        userGroups.get(root);
        this.setTitle("Admin Control Panel - Mini Twitter");
        setBounds(100, 100, 450, 450);
        treeView.removeAll();
        root.removeAllChildren();
        treeModel.setRoot(root);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        positivePercentageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int countMessages = 0;
                int positive = 0;
                float percentage;
                for (UserView userId : users.values()) {
                    countMessages += userId.TweetList.size();
                    for (String tweet : userId.TweetList) {
                        if (tweet.toUpperCase().contains("GOOD") || tweet.contains("GREAT") || tweet.contains("EXCELLENT")) {
                            positive++;
                        }
                    }
                }
                percentage = (float) ((positive * 100.0) / countMessages);
                JOptionPane.showMessageDialog(rootPane, "Percentage of the positive Tweets = " + percentage + " %");
            }
        });
        totalMessagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Counter messageCount = new Counter();
                JOptionPane.showMessageDialog((Component)null, "Total tweets: " + messageCount.getMessageCount());
            }
        });
        totalGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Counter groupCount = new Counter();
                JOptionPane.showMessageDialog((Component)null, "Total groups: " + groupCount.getGroupCount());
            }
        });
        totalUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Counter userCount = new Counter();
                JOptionPane.showMessageDialog((Component)null, "Total users: " + userCount.getUserCount());
            }
        });
        openUserViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //instance.getSelectionPath().getLastPathComponent();
                currentNode = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();
                if (currentNode != null && !userGroups.containsKey(currentNode.toString())) {
                    UserView user = users.get(currentNode.toString());
                    user.updateUser(user,"");
                    user.getAllUserList().removeItem(user);
                    user.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog((Component)null, "Select a user");
                }
                treeModel.reload(currentNode);
            }
        });
        addGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                groupName = groupIdTextArea.getText();
                if (groupName.contains(" ")){
                    whiteSpace = true;
                }
                long creationTime = System.currentTimeMillis();
                currentNode = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();
               if (userGroups.containsKey(groupName)) {
                    duplicatedGroupID = true;
                }
               if (currentNode == null) {
                    addGroup(groupName, root, creationTime);
                } else if (users.containsKey(currentNode.toString())) {
                    JOptionPane.showMessageDialog((Component)null, "Choose a directory");
                } else {
                    addGroup(groupName, currentNode, creationTime);
                }
                groupIdTextArea.setText("");
            }
        });
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName = userIdTextArea.getText();
                if (userName.contains(" ")){
                    whiteSpace = true;
                }
                long creationTime = System.currentTimeMillis();
                currentNode = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();
                if (users.containsKey(userName)) {
                    duplicatedUserID = true;
                }
                if (currentNode == null) {
                    addUser(userName, root, creationTime);
                } else if (users.containsKey(currentNode.toString())) {
                    JOptionPane.showMessageDialog((Component)null, "Choose a group");
                } else {
                    addUser(userName, currentNode, creationTime);
                }
                userIdTextArea.setText("");
            }
        });
        IDVerificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (users.containsKey(" ") || userGroups.containsKey(" ") || whiteSpace == true) {
                    JOptionPane.showMessageDialog((Component)null, "Invalid ID Exists Due to Formatting.");
                }
                if (duplicatedUserID == true || duplicatedGroupID == true) {
                    JOptionPane.showMessageDialog((Component)null, "Duplicated ID. \n Invalid ID Exists Due to Uniqueness.");
                }
                else {
                    JOptionPane.showMessageDialog((Component)null, "All The IDs Are Validated.");
                }
            }
        });
        lastUpdatedUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long latestUpdated = Collections.max(UserView.updatedUser.values());
                String updatedUser = null;
                for(Map.Entry <String, Long> entry : UserView.updatedUser.entrySet()) {
                    if(entry.getValue() >= latestUpdated) {
                        updatedUser = entry.getKey();
                    }
                }
                JOptionPane.showMessageDialog((Component)null, "Last Updated User is: " + updatedUser);
            }
        });
    }
    public void addUser(String name, DefaultMutableTreeNode node, long creationTime) {
        UserView newUser = new UserView(name, creationTime);
        users.put(name, newUser);
        Counter userPlus = new Counter();
        this.visit_userCount(userPlus);
        node.add(new DefaultMutableTreeNode(name));
        JOptionPane.showConfirmDialog((Component)null, "User Added");
        treeModel.reload(currentNode);
    }
    public void addGroup(String name, DefaultMutableTreeNode node, long creationTime) {
        Group newGroup = new Group(name, creationTime) {
            @Override
            public void update(Observable follower, Object message) {
            }
        };
        userGroups.put(name, newGroup);
        Counter groupPlus = new Counter();
        this.visit_groupCount(groupPlus);
        node.add(new DefaultMutableTreeNode(name));
        JOptionPane.showMessageDialog((Component)null, "Group created");
        treeModel.reload(currentNode);
    }
    public void visit_userCount(Counter users) {
        users.accept_userCount();
    }
    public void visit_messageCount(Counter messages) {
    }
    public void visit_groupCount(Counter groups) {
        groups.accept_groupCount();
    }
    private void initialComponents() {
        setBounds(100, 100, 450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel();
        setContentPane(mainPanel);
        mainPanel.setLayout(null);
        treeView = new JTree(root);
        treeView.setBounds(10,10,150,300);
        mainPanel.add(treeView);
        userIdTextArea = new JTextArea();
        userIdTextArea.setBounds(190,10,150,50);
        mainPanel.add(userIdTextArea);
        groupIdTextArea = new JTextArea();
        groupIdTextArea.setBounds(480,10,160,20);
        mainPanel.add(groupIdTextArea);
        addUserButton = new JButton("Add User");
        addUserButton.setBounds(480,10,150,50);
        mainPanel.add(addUserButton);
        addGroupButton = new JButton("Add Group");
        addGroupButton.setBounds(480,50,150,50);
        mainPanel.add(addGroupButton);
        openUserViewButton = new JButton("Open User View");
        openUserViewButton.setBounds(190,80,500,30);
        mainPanel.add(openUserViewButton);
        totalUserButton = new JButton("Show Total Users");
        totalUserButton.setBounds(190,250,210,30);
        mainPanel.add(totalUserButton);
        totalGroupButton = new JButton("Show Total Groups");
        totalGroupButton.setBounds(400,250,220,30);
        mainPanel.add(totalGroupButton);
        totalMessagesButton = new JButton("Show Total Messages");
        totalMessagesButton.setBounds(190,300,220,30);
        mainPanel.add(totalMessagesButton);
        positivePercentageButton = new JButton("Show Positive Percentage");
        positivePercentageButton.setBounds(400,300,220,30);
        mainPanel.add(positivePercentageButton);
        lastUpdatedUserButton = new JButton("The Last Updated User");
        lastUpdatedUserButton.setBounds(190,250,220,30);
        mainPanel.add(lastUpdatedUserButton);
        IDVerificationButton = new JButton("User/Group ID Verification");
        IDVerificationButton.setBounds(400,300,220,30);
        mainPanel.add(IDVerificationButton);
        treeModel = (DefaultTreeModel) treeView.getModel();
    }
}
