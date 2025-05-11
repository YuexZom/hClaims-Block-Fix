package me.rexoz.xyz.snea7erhed;

import com.hakan.claim.Claim;
import com.hakan.claim.ClaimHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Optional;

public class Main extends JavaPlugin implements Listener {
    private final HashMap<Player, Boolean> playerStatus = new HashMap<>();

    public void onEnable() {
        this.getLogger().info("RexozBlockFix aktif!");
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        boolean isInClaim = isPlayerInClaim(player, location);
        boolean wasInClaim = playerStatus.getOrDefault(player, false);

        if (player.isOp()) {
            return;
        }

        if (isInClaim && !wasInClaim) {
            new Payload(player, "Teyyap", "blockbreak###deactive").send();
            new Payload(player, "Teyyap", "blockplace###deactive").send();
            playerStatus.put(player, true);
        } else if (!isInClaim && wasInClaim) {
            new Payload(player, "Teyyap", "blockbreak###active").send();
            new Payload(player, "Teyyap", "blockplace###active").send();
            playerStatus.put(player, false);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerStatus.remove(player);
        new Payload(player, "Teyyap", "blockbreak###active").send();
        new Payload(player, "Teyyap", "blockplace###active").send();
    }

    private boolean isPlayerInClaim(Player player, Location location) {
        Optional<Claim> optional = ClaimHandler.findByLocation(location);
        return optional.map(claim -> !claim.getMembers().containsKey(player.getUniqueId())).orElse(false);
    }
}