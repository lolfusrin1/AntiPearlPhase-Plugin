package h4dro.me.antipearlphase;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiPearlPhase extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && isPlayerLookingDown(player)) {
            if (isDestinationSafe(player.getLocation(), to)) {
                event.setCancelled(true);
                player.sendMessage("§cPhase pearls are not allowed on this server.");
            }
        }
    }

    private boolean isPlayerLookingDown(Player player) {
        Location location = player.getLocation();
        double pitch = location.getPitch();
        return pitch > 45.0 || pitch < -45.0;
    }

    private boolean isDestinationSafe(Location from, Location to) {
        double distance = from.distance(to);
        if (distance > 1) {
            return false; // Nếu khoảng cách lớn hơn 1 block, không an toàn
        }

        // Kiểm tra xem có bức tường nào xung quanh không
        for (BlockFace face : BlockFace.values()) {
            if (face == BlockFace.DOWN || face == BlockFace.UP) {
                continue; // Bỏ qua kiểm tra trên và dưới
            }
            Location adjacent = to.clone().add(face.getModX(), 0, face.getModZ());
            if (adjacent.getBlock().getType() != Material.AIR) {
                return true; // Nếu có ít nhất một bức tường xung quanh, không an toàn
            }
        }

        return false; // Nếu không có bức tường xung quanh, không an toàn
    }
}
