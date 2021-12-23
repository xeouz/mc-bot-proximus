package me.Xeouz.ProximityMC;

//Imorting Bukkit Natives
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

//Importing Commands
import me.Xeouz.ProximityMC.classes.commands.*;

//Importing the Proximity Bot Core
import me.Xeouz.ProximityMC.classes.jda.ProxCore;
import me.Xeouz.ProximityMC.classes.server.ServerHandler;

public class Proximus extends JavaPlugin {
    ServerHandler ServerHandlerInstance = new ServerHandler();
    
    @Override
    public void onEnable() {
        getCommand("echo").setExecutor(new echo());
        getCommand("position").setExecutor(new position());
        new ProxCore(this,ServerHandlerInstance);
    }
    @Override
    public void onDisable() {
    }
}