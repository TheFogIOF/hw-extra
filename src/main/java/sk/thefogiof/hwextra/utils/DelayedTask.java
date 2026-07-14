package sk.thefogiof.hwextra.utils;

public class DelayedTask {
    private int ticksRemaining;
    private final Runnable action;
    private boolean completed = false;

    public DelayedTask(int delayTicks, Runnable action) {
        this.ticksRemaining = delayTicks;
        this.action = action;
    }

    public void tick() {
        if (completed) return;
        if (ticksRemaining > 0) {
            ticksRemaining--;
        } else {
            // Время вышло, выполняем действие
            action.run();
            completed = true;
        }
    }

    public boolean isCompleted() {
        return completed;
    }
}
