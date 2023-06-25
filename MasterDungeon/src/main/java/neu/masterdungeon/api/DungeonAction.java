package neu.masterdungeon.api;

import org.bukkit.entity.Player;

public interface DungeonAction {

    public void join(Player player);

    public void left(Player player);

    public void start();

    public void pause();

    public void stop();

    public void summarize();

}
