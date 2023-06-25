package neu.masterdungeon.plugin.stage.killmob;

import neu.masterdungeon.MasterDungeon;
import neu.masterdungeon.api.Dungeon;
import neu.masterdungeon.api.stage.StageType;
import neu.masterdungeon.api.stage.type.MapAmountStage;
import neu.masterdungeon.api.utils.BroadcastType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pdx.mantlecore.java.Pair;
import pdx.mantlecore.java.StringUtils;

public class KillVanillaMobStage extends MapAmountStage<EntityType, Integer> {

    public static final Listener KILL_VANILLA_MOB_STAGE_LISTENER = new Listener() {
        @EventHandler
        public void onVanillaMobStage(EntityDamageByEntityEvent e) {
            if (e.getDamager() instanceof Player p && e.getEntity() instanceof LivingEntity en) {
                Dungeon dungeon = MasterDungeon.getInstance().getDungeonManager().getDungeonOf(p);
                if (dungeon == null) return;

                if (!(dungeon.getCurrentStage() instanceof KillVanillaMobStage stage)) return;

                EntityType entityType = en.getType();
                if (!stage.getRequirementMap().containsKey(entityType)) return;

                if (en.getHealth() - e.getFinalDamage() <= 0) {
                    stage.setProgress(entityType, stage.getProgress(entityType) + 1);
                }
            }
        }
    };

    public KillVanillaMobStage(String stageName, int time, Pair<EntityType, Integer>... requirements) {
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
            stringBuilder.append("- ").append(StringUtils.format(entry.getKey().toString())).append(" §fx").append(entry.getValue()).append("\n");
        }
        dungeon.broadcast(BroadcastType.MESSAGE, "§a§lMỤC TIÊU", "§fHãy giết đủ bọn quái, chi tiết như sau:", stringBuilder.toString());
    }

    @Override
    public void end(Dungeon dungeon) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'end'");
    }
}
