package MiniTwitter;

public interface Visitor {
    void visit_userCount(Counter user);
    void visit_messageCount(Counter message);
    void visit_groupCount(Counter group);
}
