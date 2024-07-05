package example.catering.businesslogic.shifts;

import java.util.ArrayList;
import java.util.Date;
import java.time.Duration;

public abstract class Shift {
    protected int id;
    protected Date startDateTime;
    protected Date endDateTime;
    protected Duration duration;
    protected Date lastAvailabilityDate;
    protected boolean locked;
    protected ArrayList<Shift> recurrentShifts;

    public Shift() {
        this.recurrentShifts = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
        updateDuration();
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
        updateDuration();
    }

    public Duration getDuration() {
        return duration;
    }

    private void updateDuration() {
        if (startDateTime != null && endDateTime != null) {
            this.duration = Duration.between(startDateTime.toInstant(), endDateTime.toInstant());
        } else {
            this.duration = null;
        }
    }

    public Date getLastAvailabilityDate() {
        return lastAvailabilityDate;
    }

    public void setLastAvailabilityDate(Date lastAvailabilityDate) {
        this.lastAvailabilityDate = lastAvailabilityDate;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public ArrayList<Shift> getRecurrentShifts() {
        return recurrentShifts;
    }

    public void setRecurrentShifts(ArrayList<Shift> recurrentShifts) {
        this.recurrentShifts = recurrentShifts;
    }

    public void addRecurrentShift(Shift shift) {
        recurrentShifts.add(shift);
    }
}
