package neu.masterdungeon;

import neu.masterdungeon.api.DungeonManager;
import neu.masterdungeon.plugin.stage.killmob.KillNamedMobStage;
import neu.masterdungeon.plugin.stage.killmob.KillVanillaMobStage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MasterDungeon extends JavaPlugin {

    private static MasterDungeon instance;

    private DungeonManager dungeonManager;

    @Override
    public void onEnable() {
        registerStageListeners();
    }

    @Override
    public void onDisable() {
    }

    private void registerStageListeners() {
        Bukkit.getPluginManager().registerEvents(KillNamedMobStage.KILL_NAMED_MOB_STAGE_LISTENER, this);
        Bukkit.getPluginManager().registerEvents(KillVanillaMobStage.KILL_VANILLA_MOB_STAGE_LISTENER, this);
    }

    public static MasterDungeon getInstance() {
        return instance;
    }

    public DungeonManager getDungeonManager() {
        return dungeonManager;
    }
}
