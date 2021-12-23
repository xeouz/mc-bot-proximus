package me.Xeouz.ProximityMC.classes.commands;

//Importing Bukkit Natives
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

//Importing Server Handler
import me.Xeouz.ProximityMC.classes.server.ServerHandler;

public class position implements CommandExecutor {
    ServerHandler shandler = new ServerHandler();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String l, String[] args){
        String name = cs.getName();
        cs.sendMessage("Sending your Co-Ordinates");
        int[] loc1 = shandler.getPlayerLocation(name);
        int x = loc1[0]; 
        int y = loc1[1];
        int z = loc1[2];
        cs.sendMessage("X: "+x+"  | Y: "+y+" | Z: "+z);
        return true;
    }
}