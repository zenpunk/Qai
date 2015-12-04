package qube.qai.procedure;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Message;
import qube.qai.data.Arguments;
import qube.qai.message.MessageListener;
import qube.qai.message.MessageQueue;
import qube.qai.user.User;

import javax.inject.Inject;

/**
 * Created by rainbird on 11/27/15.
 */
public abstract class BaseProcedure extends MessageListener implements Procedure {

    public static String END_OF_PROCESS = "Process has been ended";

    public static String PROCESS_INTERRUPTED = "Process has been interrupted";

    public static String PROCESS_ERROR = "Process has been terminated with runtime errors";

    @Inject
    protected MessageQueue messageQueue;

    protected HazelcastInstance hazelcastInstance;

    protected User user;

    protected String uuid;

    protected String name;

    protected String description;

    protected double progressPercentage;

    protected boolean hasExecuted = false;

    protected Arguments arguments;

    /**
     * Visitor-pattern
     * @param visitor
     * @param data
     * @return
     */
    public Object accept(ProcedureVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    /**
     * Visitor-pattern
     * @param visitor
     * @param data
     * @return
     */
    public Object childrenAccept(ProcedureVisitor visitor, Object data) {
        // nothing to do, simply return the data
        return data;
    }

    @Override
    public void onMessage(Message message) {
        this.arguments = (Arguments) message.getMessageObject();
        try {
            run();

            // after running mark as executed
            hasExecuted = true;
        } catch (Exception e) {
            messageQueue.sendMessage(uuid, PROCESS_ERROR);
        }
        messageQueue.sendMessage(uuid, END_OF_PROCESS);
    }

    public abstract void run();

    public boolean haveChildren() {
        return false;
    }

    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Arguments getArguments() {
        return arguments;
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public boolean hasExecuted() {
        return hasExecuted;
    }

    public void hasExecuted(boolean hasExecuted) {
        this.hasExecuted = hasExecuted;
    }
}