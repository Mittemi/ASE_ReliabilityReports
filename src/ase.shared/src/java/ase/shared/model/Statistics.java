package ase.shared.model;

/**
 * Created by Michael on 20.06.2015.
 */
public class Statistics {

    private int totalLines;

    private int totalErrors;

    private int avgDelay;

    private int maxDelay;

    public int getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(int totalErrors) {
        this.totalErrors = totalErrors;
    }

    public int getAvgDelay() {
        return avgDelay;
    }

    public void setAvgDelay(int avgDelay) {
        this.avgDelay = avgDelay;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }
}
