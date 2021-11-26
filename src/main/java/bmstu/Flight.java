package bmstu;

import java.io.Serializable;

public class Flight implements Serializable {
    private String destID;
    private double delay;
    private boolean cancelled;

    public Flight(String dID, Double del, Boolean cancl) {
        this.destID = dID;
        this.delay = del;
        this.cancelled = cancl;
    }

    public Flight() {};
}
