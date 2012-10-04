package tk.nekotech.sdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
    private SelectiveDeathDrops sdd;
    private HashMap<String, Map<String, Object>[]> dropOnRespawn;
    
    public PlayerListener(SelectiveDeathDrops par1) {
        this.sdd = par1;
        this.dropOnRespawn = new HashMap<String, Map<String, Object>[]>();
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        ArrayList<Map<String, Object>> keep = new ArrayList<Map<String, Object>>();
        for (ItemStack i : new ArrayList<ItemStack>(event.getDrops())) {
            if (!sdd.doDrop(i.getTypeId())) {
                event.getDrops().remove(i);
            }
            if (sdd.doKeep(i.getTypeId())) {
                keep.add(i.serialize());
                if (sdd.doDrop(i.getTypeId())) {
                    sdd.getLogger().warning("Warning! Item " + i.getType().toString() + " was duplicated in inventory of " + event.getEntity().getName() + " due to configuration! Ensure your configuration is OK.");
                }
            }
        }
        Map<String, Object>[] arr = new Map[keep.size()];
        keep.toArray(arr);
        this.dropOnRespawn.put(event.getEntity().getName(), arr);
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (this.dropOnRespawn.containsKey(event.getPlayer().getName())) {
            for (Map<String, Object> i : dropOnRespawn.get(event.getPlayer().getName())) {
                event.getPlayer().getInventory().addItem(ItemStack.deserialize(i));
            }
        }
    }
    
}
