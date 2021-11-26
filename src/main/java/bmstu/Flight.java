package bmstu;

import java.io.Serializable;

public class Flight implements Serializable {
    private String destID;
    private String originName;
    private double delay;
    private boolean cancelled;

    public Flight(String dID, String oN,  Double del, Boolean cancl) {
        this.destID = dID;
        this.originName = oN;
        this.delay = del;
        this.cancelled = cancl;
    }

    public Flight() {};

    public String getDestinationID() {
        return this.destID;
    }

    public String getOriginName() {
        return this.originName;
    }

    public double getDelay() {
        return this.delay;
    }

    public boolean getIsCannceled() {
        return this.cancelled;
    }

}
