package neu.masterdungeon.api.io;

import neu.masterdungeon.api.Dungeon;

import java.io.File;

public interface DungeonIO {
    public void save();

    public Dungeon load(File file);
}
