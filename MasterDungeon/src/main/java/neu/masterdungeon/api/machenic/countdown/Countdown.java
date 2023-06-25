package neu.masterdungeon.api.machenic.countdown;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Data
public abstract class Countdown {

    public static final double INTERVAL = 0.1;

    private int duration;
    public int current;

    private boolean isRunning = false;
    private boolean isPaused = false;

    private boolean isAsync = false;

    private List<Player> receivers = Lists.newArrayList();
    private BukkitTask timerTask;

    public Countdown(boolean isAsync, int duration, @NonNull Player... receivers) {
        this.receivers.addAll(Arrays.asList(receivers));
        this.isAsync = isAsync;
        this.duration = duration;
    }

    public Countdown(boolean isAsync, int duration, List<Player> receivers) {
        this.receivers = receivers;
        this.isAsync = isAsync;
        this.duration = duration;
    }

    public abstract void start();
    public void pause() {
        setPaused(!isPaused);
        onPause();
    }
    public void stop(boolean normal) {
        setRunning(false);
        if(timerTask != null)
            timerTask.cancel();

        onStop(normal);
    }

    public abstract void onStart();

    public abstract void onPause();

    public abstract void onStop(boolean normal);

}
