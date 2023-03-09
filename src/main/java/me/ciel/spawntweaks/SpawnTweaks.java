package me.ciel.spawntweaks;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpawnTweaks extends JavaPlugin implements Listener {
    public boolean surfaceSpawn = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        System.out.println("[SpawnTweaks] Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /surfacespawn
        if (command.getName().equalsIgnoreCase("surfacespawn")){
            if (surfaceSpawn){
                surfaceSpawn = false;
                sender.sendMessage("Disabled surface spawning");
            } else {
                surfaceSpawn = true;
                sender.sendMessage("Enabled surface spawning");
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        if (!surfaceSpawn && event.getLocation().getWorld().getEnvironment() == World.Environment.NORMAL){
            EntityType type = event.getEntityType();

            if (type == EntityType.ZOMBIE ||
                type == EntityType.CREEPER ||
                type == EntityType.SKELETON ||
                type == EntityType.SPIDER ||
                type == EntityType.PHANTOM ||
                type == EntityType.ENDERMAN) {

                if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL &&
                    event.getLocation().getBlock().getLightFromSky() > 0) {

                    event.setCancelled(true);
                }
            }
        }
    }
}
