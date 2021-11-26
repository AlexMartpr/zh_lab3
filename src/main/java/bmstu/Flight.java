package bmstu;

import java.io.Serializable;

public class Flight implements Serializable {
    private String destID;
    private String originID;
    private double delay;
    private boolean cancelled;

    public Flight(String dID, String oID,  Double del, Boolean cancl) {
        this.destID = dID;
        this.originID = oID;
        this.delay = del;
        this.cancelled = cancl;
    }

    public Flight() {};

    public String getDestinationID() {
        return this.destID;
    }

    public String getOriginID() {
        return this.originID;
    }

    public double getDelay() {
        return this.delay;
    }

    public boolean getIsCannceled() {
        return this.cancelled;
    }

}
