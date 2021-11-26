package bmstu;

import java.io.Serializable;

public class Flight implements Serializable {
    private double delay;
    private boolean cancelled;

    public Flight(Double del, Boolean cancl) {
        this.delay = del;
        this.cancelled = cancl;
    }

    public Flight() {};

    public double getDelay() {
        return this.delay;
    }

    public boolean getIsCannceled() {
        return this.cancelled;
    }

}
