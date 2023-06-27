package neu.masterdungeon.plugin.stage.killmob;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import neu.masterdungeon.MasterDungeon;
import neu.masterdungeon.api.Dungeon;
import neu.masterdungeon.api.stage.StageType;
import neu.masterdungeon.api.stage.TimedCheckableStage;
import neu.masterdungeon.api.stage.type.MapAmountStage;
import neu.masterdungeon.api.utils.BroadcastType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pdx.mantlecore.java.Pair;

import java.util.ArrayList;
import java.util.List;

public class KillNamedMobStage extends MapAmountStage<String, Integer> implements TimedCheckableStage {

    public static final Listener KILL_NAMED_MOB_STAGE_LISTENER = new Listener() {
        @EventHandler
        public void onVanillaMobStage(EntityDamageByEntityEvent e) {
            if (e.getDamager() instanceof Player p && e.getEntity() instanceof LivingEntity en) {
                Dungeon dungeon = MasterDungeon.getInstance().getDungeonManager().getDungeonOf(p);
                if (dungeon == null) return;

                if (!(dungeon.getCurrentStage() instanceof KillNamedMobStage stage)) return;

                if(en.getCustomName() == null) return;

                String name = en.getCustomName();
                if (!stage.getRequirementMap().containsKey(name)) return;

                if (en.getHealth() - e.getFinalDamage() <= 0) {
                    stage.setProgress(name, stage.getProgress(name) + 1);
                    dungeon.broadcast(BroadcastType.SUBTITLE, name + " §f"
                            + stage.getProgress(name) + "/" + stage.getRequirement(name));
                }
            }
        }
    };

    private boolean autoSpawn = true;
    private List<String> mythicMobIDs = new ArrayList<>();
    private List<Location> spawnerLocations = new ArrayList<>();
    private int amountPerSpawn = 5;
    private int interval = 3;
    private int totalSpawn = -1;

    private int currentSec = 0;
    private int spawnedCount = 0;

    public KillNamedMobStage(String stageName, int time, Pair<String, Integer>... requirements) {
        super(StageType.KILL_MOB, stageName, time);

        for (var entry : requirements) {
            registerRequirement(entry.getKey(), entry.getValue());
            setProgress(entry.getKey(), 0);
        }
    }

    @Override
    public void begin(Dungeon dungeon) {
        dungeon.broadcast(BroadcastType.TITLE_AND_SUBTITLE, "§a§lMỤC TIÊU", "Giết bọn quái");

        StringBuilder stringBuilder = new StringBuilder();
        for (var entry : getRequirementMap().entrySet()) {
            stringBuilder.append("- ").append(entry.getKey()).append(" §fx").append(entry.getValue()).append("\n");
        }
        dungeon.broadcast(BroadcastType.MESSAGE, "§a§lMỤC TIÊU", "§fHãy giết đủ bọn quái, chi tiết như sau:", stringBuilder.toString());
    }

    @Override
    public void end(Dungeon dungeon) {

    }

    @Override
    public void run(Dungeon dungeon) {
        if(!autoSpawn) return;

        if(spawnedCount >= totalSpawn) return;

        currentSec++;
        if(currentSec % interval == 0) {
            for(int )
            MythicBukkit.inst().getMobManager().spawnMob()
        }
    }
}
