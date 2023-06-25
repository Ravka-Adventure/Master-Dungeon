package neu.masterdungeon.plugin.stage.location;

import neu.masterdungeon.api.Dungeon;
import neu.masterdungeon.api.stage.AbstractStage;
import neu.masterdungeon.api.stage.Stage;
import neu.masterdungeon.api.stage.StageType;
import neu.masterdungeon.api.stage.TimedCheckableStage;
import neu.masterdungeon.api.utils.BroadcastType;

import java.util.Map;

import org.bukkit.Location;

public class ReachLocationStage extends AbstractStage implements TimedCheckableStage {

    public static final double DISTANCE_ERROR = 1.0;

    private Location destination;

    private boolean finished = false;

    public ReachLocationStage(String stageName, int time) {
        super(StageType.REACH_LOCATION, stageName, time);
    }

    @Override
    public boolean isCompleted() {
        return finished;
    }

    @Override
    public void begin(Dungeon dungeon) {
        dungeon.broadcast(BroadcastType.TITLE_AND_SUBTITLE, "§a§lMỤC TIÊU", "Đến vị trí");

        dungeon.broadcast(BroadcastType.MESSAGE, "§a§lMỤC TIÊU", "§fHãy mau chóng đến vị trí có tọa độ như sau: ",
                "",
                "\tX: §a" + destination.getBlockX(), "\t§fY: §a" + destination.getBlockY(), "\t§fZ: §a" + destination.getBlockZ());
    }

    @Override
    public void end(Dungeon dungeon) {

    }

    @Override
    public void check(Dungeon dungeon) {
        for(var p : dungeon.getParticipants()) {
            if(destination.distance(p.getLocation()) <= DISTANCE_ERROR) {
                finished = true;
                break;
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }

    @Override
    public Stage deserialize(Map<String, Object> map) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deserialize'");
    }
}
