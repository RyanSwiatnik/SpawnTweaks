package me.ciel.spawntweaks;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SpawnTweaks extends JavaPlugin implements Listener {
    public boolean surfaceSpawn;
    public List<EntityType> surfaceSpawnMobs = new ArrayList<EntityType>();

    @Override
    public void onEnable() {
        // Load config
        saveDefaultConfig();
        surfaceSpawn = getConfig().getBoolean("surface-spawn", false);

        for (String entity : getConfig().getStringList("surface-spawn-mobs")) {
            try {
                surfaceSpawnMobs.add(EntityType.valueOf(entity));
            } catch (Exception e) {
                System.out.println("Error loading mob list, incorrect entity name: " + entity);
            }
        }

        getServer().getPluginManager().registerEvents(this, this);

        System.out.println("Spawn Tweaks enabled");
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
                getConfig().set("surface-spawn", false);
                sender.sendMessage("Disabled surface spawning");
            } else {
                surfaceSpawn = true;
                getConfig().set("surface-spawn", true);
                sender.sendMessage("Enabled surface spawning");
            }
            saveConfig();
        }
        return super.onCommand(sender, command, label, args);
    }

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        if (!surfaceSpawn &&
            event.getLocation().getWorld().getEnvironment() == World.Environment.NORMAL &&
            surfaceSpawnMobs.contains(event.getEntityType()) &&
            event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL &&
            event.getLocation().getBlock().getLightFromSky() > 0) {

            event.setCancelled(true);
        }
    }
}
