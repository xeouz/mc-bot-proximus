package me.Xeouz.ProximityMC.classes.commands;

//Importing Bukkit Natives
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class echo implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender csender, Command cmd, String l, String[] args) {
        
        Player p = (Player) csender;
        p.sendMessage("You Said - "+l);
        return true;
    }
}