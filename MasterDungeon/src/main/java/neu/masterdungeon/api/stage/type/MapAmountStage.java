package neu.masterdungeon.api.stage.type;

import com.google.common.collect.Maps;
import neu.masterdungeon.api.stage.AbstractStage;
import neu.masterdungeon.api.stage.Stage;
import neu.masterdungeon.api.stage.StageType;
import neu.masterdungeon.plugin.stage.item.FindItemStage;

import java.util.HashMap;
import java.util.Map;

public abstract class MapAmountStage<K, V extends Number> extends AbstractStage {

    private final Map<K, V> requirement = Maps.newHashMap();

    private final Map<K, V> progress = Maps.newHashMap();

    public MapAmountStage(StageType stageType, String stageName, int time) {
        super(stageType, stageName, time);
    }

    public Map<K, V> getRequirementMap() {
        return requirement;
    }

    public V getRequirement(K key) {
        if (!requirement.containsKey(key))
            throw new NullPointerException("The stage " + this.getClass() + " does not require " + key + " to complete.");
        return requirement.get(key);
    }

    public void registerRequirement(K key, V value) {
        requirement.put(key, value);
    }

    public V getProgress(K key) {
        if (!requirement.containsKey(key))
            throw new NullPointerException("The stage " + this.getClass() + " does not require " + key + " to complete.");
        return progress.get(key);
    }

    public V setProgress(K key, V value) {
        if (!requirement.containsKey(key))
            throw new NullPointerException("The stage " + this.getClass() + " does not require " + key + " to complete.");
        return requirement.put(key, value);
    }

    @Override
    public boolean isCompleted() {
        for (var entry : requirement.entrySet()) {
            V require = entry.getValue();
            V progress = getProgress(entry.getKey());

            if (progress.doubleValue() < require.doubleValue())
                return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("stage-name", getStageName());
        map.put("time", getTime());
        for (var entry : getRequirementMap().entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue());
        }
        return map;
    }

    @Override
    public MapAmountStage<K, V> deserialize(Map<String, Object> map) {
        String stageName = (String) map.get("stage-name");
        int time = (int) map.get("time");
        
        setStageName(stageName);
        setTime(time);

        for (var entry : map.entrySet()) {
            if (!(entry.getKey().equals("stage-name") || entry.getKey().equals("time"))) {
                registerRequirement((K) entry.getKey(), (V) entry.getValue());
            }
        }

        return this;
    }
}
