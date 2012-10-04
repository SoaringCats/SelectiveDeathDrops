package tk.nekotech.sdd;

import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

public class SelectiveDeathDrops extends JavaPlugin {
    private List<Integer> alwaysDrop;
    private List<Integer> alwaysKeep;
    
    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.alwaysDrop = this.getConfig().getIntegerList("always_drop");
        this.alwaysKeep = this.getConfig().getIntegerList("always_keep");
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }
    
    public List<Integer> getAlwaysDrop() {
        return alwaysDrop;
    }
    
    public List<Integer> getAlwaysKeep() {
        return alwaysKeep;
    }
    
    public boolean doDrop(int id) {
        return alwaysDrop.contains(id);
    }
    
    public boolean doKeep(int id) {
        return alwaysKeep.contains(id);
    }
    
}
