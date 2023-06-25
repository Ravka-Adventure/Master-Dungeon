package neu.masterdungeon.api.machenic.countdown;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import net.kyori.adventure.title.TitlePart;
import neu.masterdungeon.MasterDungeon;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import pdx.mantlecore.java.StringBarPercent;
import pdx.mantlecore.math.PrimaryMath;

import java.util.List;

public class TitleCountdown extends Countdown {

    private String title = "%cd%";
    private String subtitle = "";

    private StringBarPercent.PercentColorMap percentColorMap;

    public TitleCountdown(boolean isAsync, int duration, @NotNull @NonNull Player... receivers) {
        super(isAsync, duration, receivers);
    }

    public TitleCountdown(boolean isAsync, int duration, List<Player> receivers) {
        super(isAsync, duration, receivers);
    }

    public TitleCountdown title(String title) {
        this.title = title;
        return this;
    }

    public TitleCountdown subtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public TitleCountdown percentColorMap(StringBarPercent.PercentColorMap percentColorMap) {
        this.percentColorMap = percentColorMap;
        return this;
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

                    int pcnt = (int) PrimaryMath.percentage((double) current, (double) getDuration(), PrimaryMath.PercentageMode.INTEGER);
                    getReceivers().forEach(player -> {
                        String color = percentColorMap.getColorOf(pcnt, "&a&l");
                        player.sendTitle(color + title.replace("%cd%", String.valueOf(current)), subtitle);

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
