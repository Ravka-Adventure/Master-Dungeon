package neu.masterdungeon.plugin.dungeon;

import neu.masterdungeon.api.Dungeon;
import neu.masterdungeon.api.DungeonThread;
import neu.masterdungeon.api.LocationType;
import neu.masterdungeon.api.machenic.countdown.TitleCountdown;
import neu.masterdungeon.api.utils.BroadcastType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pdx.mantlecore.java.IntRange;
import pdx.mantlecore.java.StringBarPercent.PercentColorMap;

public class RPGDungeon extends Dungeon {

    @Override
    public void join(Player player) {
        broadcast(BroadcastType.MESSAGE, "§e" + player.getName() +
                " vừa tham gia đấu trường (" + getParticipants().size() + "/" + getPlayerToStart() + ")");

        getParticipants().add(player);

        if (countPlayer() >= getPlayerToStart()) {
            start();
        }
    }

    @Override
    public void left(Player player) {
        broadcast(BroadcastType.MESSAGE, "§c" + player.getName() +
                " vừa rời khỏi đấu trường");

        getParticipants().remove(player);
    }

    @Override
    public void start() {
        Dungeon dungeonInstance = this;

        PercentColorMap percentColorMap = new PercentColorMap()
                .append(IntRange.of(0, 30), "§c§l")
                .append(IntRange.of(31, 100), "§e§l");
        TitleCountdown countdown = new TitleCountdown(true, 10, getParticipants()) {
            @Override
            public void onStop(boolean normal) {
                if (!normal) {
                    broadcast(BroadcastType.MESSAGE, "§c§lCó vấn đề đã xảy ra trong quá trình tải đấu trường. Vui lòng thử lại sau.");
                    return;
                }

                setDungeonThread(new DungeonThread(dungeonInstance));

                Location enterLocation = getLocation(LocationType.START);
                getParticipants().forEach(p -> p.teleportAsync(enterLocation));

                nextStage();
            }
        }
                .title("%cd%")
                .subtitle("§6chuẩn bị vào trận")
                .percentColorMap(percentColorMap);

        setCurrentCountdown(countdown);
        countdown.start();
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void summarize() {

    }

}
