package neu.masterdungeon.api.machenic.countdown;

import lombok.Builder;
import lombok.NonNull;
import neu.masterdungeon.MasterDungeon;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import pdx.mantlecore.java.StringBarPercent;
import pdx.mantlecore.math.PrimaryMath;

import java.util.List;

@Builder
public class ActionBarCountdown extends Countdown {

    private String content = "%cd%";

    public ActionBarCountdown(boolean isAsync, int duration, @NotNull @NonNull Player... receivers) {
        super(isAsync, duration, receivers);
    }

    public ActionBarCountdown(boolean isAsync, int duration, List<Player> receivers) {
        super(isAsync, duration, receivers);
    }

    @Override
    public void start() {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (!isRunning() || isPaused()) return;

                    if (current <= 0) {
                        stop(true);
                        return;
                    }

                    current--;

                    getReceivers().forEach(player -> {
                        player.sendActionBar(content.replace("&", "§").replace("%cd%", String.valueOf(current)));

                        player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    stop(false);
                }
            }
        };

        setTimerTask(isAsync() ?
                task.runTaskTimerAsynchronously(MasterDungeon.getInstance(), 20, 20) :
                task.runTaskTimer(MasterDungeon.getInstance(), 20, 20));

        onStart();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop(boolean normal) {

    }
}
