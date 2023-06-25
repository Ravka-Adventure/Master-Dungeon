package neu.masterdungeon;

import neu.masterdungeon.api.DungeonManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MasterDungeon extends JavaPlugin {

    private static MasterDungeon instance;

    private DungeonManager dungeonManager;

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
    }

    public static MasterDungeon getInstance() {
        return instance;
    }

    public DungeonManager getDungeonManager() {
        return dungeonManager;
    }
}
