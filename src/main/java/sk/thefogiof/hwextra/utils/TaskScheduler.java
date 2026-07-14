package sk.thefogiof.hwextra.utils;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskScheduler {
    private static final List<DelayedTask> tasks = new ArrayList<>();

    public static void schedule(int delayTicks, Runnable action) {
        tasks.add(new DelayedTask(delayTicks, action));
    }

    public static void tick(Minecraft minecraft) {
        Iterator<DelayedTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            DelayedTask task = iterator.next();
            task.tick();
            if (task.isCompleted()) {
                iterator.remove();
            }
        }
    }
}

