package main.java.org.ufu.ds.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduledRun {

    Timer timer;

    TimerTask timerTask;

    Long period;

    List<TimerTask> timerTaskList = new ArrayList<>();

    public ScheduledRun(Timer timer, TimerTask timerTask, Long period) {
        this.timer = timer;
        this.timerTask = timerTask;
        this.period = period;
    }

    public void schedule() {
        timerTaskList.add(timerTask);
        timer.schedule(timerTask, 0, period);
    }

    public void removeFromTrackList(TimerTask timerTask) {
        timerTaskList.remove(timerTask);
    }

    public void cancel() {
        timer.cancel();
    }

    public boolean isTimerNotScheduled() {
        return timerTaskList.isEmpty();
    }

}
