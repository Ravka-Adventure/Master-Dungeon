package neu.masterdungeon.api.io;

import neu.masterdungeon.api.Dungeon;
import neu.masterdungeon.api.stage.Stage;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pdx.mantlecore.java.Splitter;

import java.io.File;
import java.io.IOException;

public class StandardDungeonIO implements DungeonIO {

    private final Dungeon dungeon;

    public StandardDungeonIO(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    @Override
    public void save() {
        File file = new File("plugins/MasterDungeon/instances/" + dungeon.getID() + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("id", dungeon.getID());
        config.set("name", dungeon.getName());
        config.set("world", dungeon.getWorld());
        config.set("schematic", dungeon.getSchematicFile());
        config.set("dungeon-mode", dungeon.getDungeonMode().toString());

        for(var entry : dungeon.getDungeonLocations().entrySet()) {
            Location location = entry.getValue();
            Splitter splitter = Splitter.newInstance().appendElements(
                    location.getWorld().getName(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    location.getYaw(),
                    location.getPitch()
            ).splitBy(",");
            config.set("locations." + entry.getKey(), splitter);
        }

        int index = 0;
        for(Stage stage : dungeon.getDungeonStages()) {
            config.set("stage." + index, stage.serialize());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dungeon load(File file) {
        return null;
    }
}
