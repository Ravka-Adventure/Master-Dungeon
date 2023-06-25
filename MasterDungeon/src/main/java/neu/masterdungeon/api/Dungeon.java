package neu.masterdungeon.api;

import lombok.Data;
import neu.masterdungeon.DungeonState;
import neu.masterdungeon.api.machenic.countdown.Countdown;
import neu.masterdungeon.api.stage.Stage;
import neu.masterdungeon.api.utils.BossbarBroadcast;
import neu.masterdungeon.api.utils.BroadcastType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Data
public abstract class Dungeon implements DungeonAction, Cloneable {

    private World world;

    private String ID;
    private String name;
    private String schematicFile;

    private boolean isPaused = false;

    private Stage currentStage;

    private DungeonThread dungeonThread;

    private DungeonState dungeonState = DungeonState.WAITING;
    private DungeonMode dungeonMode = DungeonMode.SINGLE;

    private Countdown currentCountdown;

    private Queue<Stage> dungeonStages = new LinkedList<>();
    private List<Player> participants = new ArrayList<>();

    private Map<String, Location> dungeonLocations = new HashMap<>();

    public void registerLocation(LocationType locationType, Location position) {
        this.dungeonLocations.put(locationType.toString(), position);
    }

    public void registerLocation(String customType, Location location) {
        this.dungeonLocations.put(customType, location);
    }

    @Nullable
    public Location getLocation(LocationType locationType) {
        return this.dungeonLocations.get(locationType.toString());
    }

    public void nextStage() {
        this.currentStage = dungeonStages.poll();
        if (this.currentStage != null)
            this.currentStage.begin(this);
        else
            summarize();
    }

    public void updateParticipants() {
        this.participants = this.participants.stream().filter(
                Player::isOnline
        ).collect(Collectors.toList());

        if (this.participants.isEmpty()) {
            this.dungeonState = DungeonState.CLOSING;
        }
    }

    public List<Player> getRandomPlayer(int amount) {
        Collections.shuffle(participants);
        return this.participants.stream().limit(amount).collect(Collectors.toList());
    }

    public int getPlayerToStart() {
        return switch (getDungeonMode()) {
            case SINGLE -> 1;
            case DUO -> 2;
            default -> throw new RuntimeException("If the dungeon mode is not SINGLE or DUO, " +
                    "you need to specify the number of player to start the game.");
        };
    }

    public int countPlayer() {
        return getParticipants().size();
    }

    public void broadcast(BroadcastType broadcastType, String... contents) {
        StringBuilder message = new StringBuilder();
        for (String s : contents)
            message.append(s).append("\n");

        switch (broadcastType) {
            case MESSAGE -> {
                getParticipants().forEach(p -> p.sendMessage(message.toString()));
            }
            case ACTION_BAR -> {
                getParticipants().forEach(p -> p.sendActionBar(message.toString()));
            }
            case TITLE -> {
                getParticipants().forEach(p -> p.sendTitle(contents[0], ""));
            }
            case SUBTITLE -> {
                getParticipants().forEach(p -> p.sendTitle("", contents[0]));
            }
            case TITLE_AND_SUBTITLE -> {
                getParticipants().forEach(p -> p.sendTitle(contents[0], contents[1]));
            }
            case BOSS_BAR -> {
                getParticipants().forEach(p -> BossbarBroadcast.broadcast(contents[0], p));
            }
        }
    }

    @Override
    public Dungeon clone() {
        try {
            Dungeon dungeon = (Dungeon) super.clone();
            dungeon.dungeonStages = new LinkedList<>(getDungeonStages());
            return dungeon;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
