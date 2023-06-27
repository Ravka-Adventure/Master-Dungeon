package neu.masterdungeon.plugin.stage.killmob;

import lombok.Data;
import neu.masterdungeon.api.stage.StageMachenic;
import org.bukkit.Location;

import java.util.List;

@Data
public class MobSpawningMachenic implements StageMachenic {

    private final MobType mobType;
    private final String[] args;

    private List<Location> spawners;

    public MobSpawningMachenic(MobType mobType, String... args) {
        this.mobType = mobType;
        this.args = args;
    }

    @Override
    public void tick() {

    }
}
