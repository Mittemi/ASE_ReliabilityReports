package ase.analysis.analysis.prioritizedMessaging;

/**
 * Created by Michael on 21.06.2015.
 */
public class PrioritizedMessage<T> {

    public PrioritizedMessage(T content, MessagePriority messagePriority) {
        this.content = content;
        this.messagePriority = messagePriority;
    }

    private T content;

    private MessagePriority messagePriority;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public MessagePriority getMessagePriority() {
        return messagePriority;
    }

    public void setMessagePriority(MessagePriority messagePriority) {
        this.messagePriority = messagePriority;
    }
}
