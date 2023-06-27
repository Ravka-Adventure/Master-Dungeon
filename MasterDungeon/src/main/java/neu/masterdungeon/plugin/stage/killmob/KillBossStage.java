package neu.masterdungeon.plugin.stage.killmob;

import io.lumine.mythic.core.mobs.ActiveMob;
import neu.masterdungeon.api.Dungeon;
import neu.masterdungeon.api.stage.StageType;
import neu.masterdungeon.api.stage.TimedCheckableStage;
import neu.masterdungeon.api.stage.type.MapAmountStage;
import neu.masterdungeon.api.utils.BroadcastType;

public class KillBossStage extends MapAmountStage<String, Integer> implements TimedCheckableStage {

    private ActiveMob bossInstance;
    private boolean isFinished = false;

    public KillBossStage(String stageName, int time, ActiveMob bossInstance) {
        super(StageType.KILL_BOSS, stageName, time);
        this.bossInstance = bossInstance;
    }

    @Override
    public boolean isCompleted() {
        return isFinished;
    }

    @Override
    public void begin(Dungeon dungeon) {
        dungeon.broadcast(BroadcastType.TITLE_AND_SUBTITLE, "§c§lGIẾT BOSS", bossInstance.getDisplayName());

        dungeon.broadcast(BroadcastType.MESSAGE, "§a§lMỤC TIÊU", "§fHãy tiêu diệt trùm "
                + bossInstance.getDisplayName());
    }

    @Override
    public void end(Dungeon dungeon) {
    }

    @Override
    public void run(Dungeon dungeon) {
        this.isFinished = bossInstance.isDead();
    }
}
