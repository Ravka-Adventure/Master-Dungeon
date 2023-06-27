package neu.masterdungeon.api;

import neu.masterdungeon.DungeonState;
import neu.masterdungeon.MasterDungeon;
import neu.masterdungeon.api.stage.Stage;
import neu.masterdungeon.api.stage.TimedCheckableStage;
import org.bukkit.scheduler.BukkitRunnable;
import pdx.mantlecore.task.TaskQueue;

public class DungeonThread extends BukkitRunnable {

    private boolean isRunning;

    private Dungeon dungeon;

    public DungeonThread(Dungeon dungeon) {
        this.dungeon = dungeon;
        this.isRunning = true;

        runTaskTimerAsynchronously(MasterDungeon.getInstance(), 20, 20);
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        try {
            if(getDungeon().getDungeonState() == DungeonState.CLOSING) {
               this.cancel();
               return;
            }

            if(getDungeon().isPaused()) return;

            Stage currentStage = dungeon.getCurrentStage();
            if(currentStage.isCompleted()) {
                currentStage.end(getDungeon());

                new TaskQueue().thenRun(3 * 20, () -> getDungeon().nextStage());
            } else if(currentStage instanceof TimedCheckableStage timedCheckableStage){
                timedCheckableStage.check(getDungeon());
            }

            getDungeon().updateParticipants();
        } catch (Exception ex) {
            ex.printStackTrace();
            cancel();
        }
    }
}
