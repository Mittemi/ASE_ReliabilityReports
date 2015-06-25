package ase.analysis.analysis.prioritizedMessaging;

/**
 * Created by Michael on 21.06.2015.
 */
public enum MessagePriority {
    //priority values from: http://activemq.apache.org/how-can-i-support-priority-queues.html
    Low(1), //0..3
    Medium(4),  //4
    High(9);    //5..9

    private final int priority;

    MessagePriority(int priority) {
        this.priority = priority;
    }

    public int getValue() {
        return priority;
    }

}
