package main.java.org.ufu.ds.schedule;

import java.util.Timer;
import java.util.TimerTask;

public class ScheduledRun {

    Timer timer;

    TimerTask timerTask;

    Long period;

    public ScheduledRun(Timer timer, TimerTask timerTask, Long period) {
        this.timer = timer;
        this.timerTask = timerTask;
        this.period = period;
    }

    public void schedule() {
        timer.schedule(timerTask, 0, period);
    }


}
