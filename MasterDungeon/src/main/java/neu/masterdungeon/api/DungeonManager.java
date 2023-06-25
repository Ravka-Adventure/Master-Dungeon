package neu.masterdungeon.api;

import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;

public class DungeonManager {

    private static final HashMap<String, Dungeon> registeredDungeons = new HashMap<>();
    private static final HashMap<String, Dungeon> generatedInstances = new HashMap<>();

    public void registerInstance(Dungeon dungeon) {
        registeredDungeons.put(dungeon.getID(), dungeon);
    }

    public Dungeon create(String instance) throws IOException {
        if (!registeredDungeons.containsKey(instance))
            throw new NullPointerException("The dungeon instance with ID '" + instance + "' has not been initialized yet!");

        Dungeon instanceDungeon = registeredDungeons.get(instance);
        DungeonStructureBuilder structureBuilder = new DungeonStructureBuilder(instanceDungeon);

        Dungeon generatedDungeon = structureBuilder.generate();
        generatedInstances.put(generatedDungeon.getID(), generatedDungeon);

        return generatedDungeon;
    }

    public Dungeon getDungeonOf(Player player) {
        for(var dungeons : generatedInstances.values()) {
            if(dungeons.getParticipants().contains(player)) return dungeons;
        }
        return null;
    }

}
