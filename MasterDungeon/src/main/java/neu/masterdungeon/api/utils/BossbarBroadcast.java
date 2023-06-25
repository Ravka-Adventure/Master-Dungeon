package neu.masterdungeon.api.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import neu.masterdungeon.MasterDungeon;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossbarBroadcast {

    public static final NamespacedKey BROADCAST_BOSSBAR_NAMESPACED_KEY = new NamespacedKey(MasterDungeon.getInstance(),
            "BROADCAST_BOSSBAR_NAMESPACED_KEY");
    private static final ConcurrentHashMap<UUID, PlayerBossbarBroadcast> broadcastCache = new ConcurrentHashMap<>();

    public static void broadcast(String content, BarColor barColor, BarStyle barStyle, @Nullable Sound sound, Player...players) {
        for(Player player : players) {
            PlayerBossbarBroadcast instance = broadcastCache.getOrDefault(player.getUniqueId(), new PlayerBossbarBroadcast());
            instance.setBarColor(barColor);
            instance.setBarStyle(barStyle);
            instance.setCurrentMessage(content);

            if(sound != null) {
                player.playSound(player.getEyeLocation(), sound, 1, 1);
            }

            instance.broadcast();
            broadcastCache.put(player.getUniqueId(), instance);
        }
    }

    public static void broadcast(String content, Player... players) {
        broadcast(content, BarColor.BLUE, BarStyle.SOLID, null, players);
    }

    public static void broadcast(String content, Sound sound, Player... players) {
        broadcast(content, BarColor.BLUE, BarStyle.SOLID, sound, players);
    }


    @Data
    public static class PlayerBossbarBroadcast {
        private String currentMessage = "";
        private BarColor barColor = BarColor.BLUE;
        private BarStyle barStyle = BarStyle.SOLID;

        @Setter(AccessLevel.PRIVATE)
        private BossBar instance;

        public void broadcast() {
            if(instance == null) {
                this.instance = Bukkit.createBossBar(BROADCAST_BOSSBAR_NAMESPACED_KEY, currentMessage, barColor, barStyle);
            }
            this.instance.setTitle(currentMessage);
            this.instance.setColor(barColor);
            this.instance.setStyle(barStyle);

            toggle(true);
        }

        public void toggle(boolean show) {
            if(instance == null) return;

            if(show) instance.setVisible(true);
            else instance.setVisible(false);
        }

        public void toggle() {
            if(instance == null) return;
            toggle(instance.isVisible());
        }
    }

}
