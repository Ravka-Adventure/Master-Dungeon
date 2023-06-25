package neu.masterdungeon.api.stage;

import java.io.Serializable;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import neu.masterdungeon.api.Dungeon;

/**
 * Represents a dungeon stage.<br>
 * A stage is a phase that players have to finish a specific
 * task such as killing mobs, find items, open doors, ...
 * Each stage will have a time limit to finish.
 * If players finish the stage before the time limit or the time
 * has expired, it will switch to another stage.
 *
 * @author Phạm Văn Linh
 */
public interface Stage {

    /**
     * The type of the stage.
     */
    public StageType getType();

    /**
     * The name of the stage.
     */
    public String getName();

    /**
     * The time for players to complete this stage (in seconds).
     */
    public int getTime();

    /**
     * Check for the condition (depending on the stage type) to complete the stage.
     * @return {@code true} if the stage has been completed, {@code false} otherwise.
     */
    public boolean isCompleted();

    public void begin(Dungeon dungeon);

    public void end(Dungeon dungeon);
    
    public Map<String, Object> serialize();

    public Stage deserialize(Map<String, Object> map);
}
