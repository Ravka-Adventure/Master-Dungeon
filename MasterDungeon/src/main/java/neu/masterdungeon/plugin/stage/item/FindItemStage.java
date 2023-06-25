package neu.masterdungeon.plugin.stage.item;

import neu.masterdungeon.api.Dungeon;
import neu.masterdungeon.api.stage.Stage;
import neu.masterdungeon.api.stage.StageType;
import neu.masterdungeon.api.stage.TimedCheckableStage;
import neu.masterdungeon.api.stage.type.MapAmountStage;
import neu.masterdungeon.api.utils.BroadcastType;
import org.bukkit.inventory.ItemStack;
import pdx.mantlecore.item.ItemNameUtil;
import pdx.mantlecore.java.Pair;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindItemStage extends MapAmountStage<String, Integer> implements TimedCheckableStage {

    private boolean finished = false;

    public FindItemStage(String stageName, int time, Pair<ItemStack, Integer>... requirement) {
        super(StageType.FIND_ITEM, stageName, time);

        for (var entry : requirement) {
            var key = ItemNameUtil.formatItemName(entry.getKey());
            registerRequirement(key, entry.getValue());
            setProgress(key, 0);
        }
    }

    @Override
    public boolean isCompleted() {
        return finished;
    }

    @Override
    public void begin(Dungeon dungeon) {
        dungeon.broadcast(BroadcastType.TITLE_AND_SUBTITLE, "§a§lMỤC TIÊU", "Thu thập vật phẩm");

        StringBuilder stringBuilder = new StringBuilder();
        for (var entry : getRequirementMap().entrySet()) {
            stringBuilder.append("- ").append(entry.getKey()).append(" §fx").append(entry.getValue()).append("\n");
        }
        dungeon.broadcast(BroadcastType.MESSAGE, "§a§lMỤC TIÊU", "§fHãy thu thập đủ lượng vật phẩm yêu cầu, chi tiết như sau:", stringBuilder.toString());
    }

    @Override
    public void end(Dungeon dungeon) {

    }

    @Override
    public void check(Dungeon dungeon) {
        Map<String, Long> items = dungeon.getParticipants().stream().map(p -> p.getInventory().getContents())
                .flatMap(Stream::of)
                .collect(Collectors.groupingBy(new Function<ItemStack, String>() {
                    @Override
                    public String apply(ItemStack itemStack) {
                        return ItemNameUtil.formatItemName(itemStack);
                    }
                }, Collectors.counting()));

        int finishedCount = 0;
        for (var entry : getRequirementMap().entrySet()) {
            var counted = items.getOrDefault(entry.getKey(), 0L);
            if (counted >= entry.getValue()) {
                finishedCount++;
            }
        }

        if (finishedCount == getRequirementMap().size()) {
            finished = true;
        }
    }

}
