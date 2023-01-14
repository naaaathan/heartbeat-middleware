package main.java.org.ufu.ds.schedule;

import java.util.Timer;
import java.util.TimerTask;

public class ScheduledRun {

    Timer timer;

    TimerTask timerTask;

    Long delay;

    public ScheduledRun(Timer timer, TimerTask timerTask, Long delay) {
        this.timer = timer;
        this.timerTask = timerTask;
        this.delay = delay;
    }

    public void schedule() {
        timer.schedule(timerTask, delay);
    }


}
