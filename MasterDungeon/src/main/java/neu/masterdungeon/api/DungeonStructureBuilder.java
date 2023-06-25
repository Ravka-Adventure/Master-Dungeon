package neu.masterdungeon.api;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Location;
import org.bukkit.World;
import pdx.mantlecore.java.collection.Lists;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DungeonStructureBuilder {

    public static final List<Triple<Double, Double, Double>> plotPool = new ArrayList<>();

    static {
        for (int x = 1; x <= 100; x++) {
            for (int z = 1; z <= 100; z++) {
                plotPool.add(Triple.of(x * 500.0, 60.0, z * 500.0));
            }
        }
    }

    public static Triple<Double, Double, Double> retrieveRandomPlot() {
        Triple<Double, Double, Double> randomTriple = Lists.random(plotPool);
        plotPool.remove(randomTriple);
        return randomTriple;
    }

    private final Dungeon instance;

    public DungeonStructureBuilder(Dungeon instance) {
        this.instance = instance;
    }

    public Dungeon generate() throws IOException {
        World world = instance.getWorld();

        /**
         * Load the schematic
         */
        File schematicFile = new File("plugins/MasterDungeon/schematics/" + instance.getSchematicFile());
        Clipboard clipboard = FaweAPI.load(schematicFile);

        /**
         * Pasting
         */
        Triple<Double, Double, Double> plotBase = retrieveRandomPlot();
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(plotBase.getLeft(), plotBase.getMiddle(), plotBase.getRight()))
                    .ignoreAirBlocks(true)
                    .build();
            Operations.complete(operation);
        }

        /**
         * Setting up dungeon
         */

        // Re-calculating locations
        Location baseLocation = instance.getLocation(LocationType.POS_BASE);
        assert baseLocation != null;
        double offsetX = baseLocation.getX() - plotBase.getLeft();
        double offsetY = baseLocation.getY() - plotBase.getMiddle();
        double offsetZ = baseLocation.getZ() - plotBase.getRight();

        Dungeon dungeon = instance.clone();
        dungeon.registerLocation(LocationType.POS_BASE, new Location(world,
                plotBase.getLeft(), plotBase.getMiddle(), plotBase.getRight()));

        for (var entry : dungeon.getDungeonLocations().entrySet()) {
            if (!entry.getKey().equals(LocationType.POS_BASE.toString())) {
                entry.setValue(entry.getValue().offset(offsetX, offsetY, offsetZ).toLocation(world));
            }
        }

        // Changing the ID
        dungeon.setID(dungeon.getID() + "_" + System.currentTimeMillis());

        return dungeon;
    }
}
