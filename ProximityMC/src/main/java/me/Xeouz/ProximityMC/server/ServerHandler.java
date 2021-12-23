package me.Xeouz.ProximityMC.classes.server;

// Importing Bukkit Natives
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.World;

//Importing Java Utils
import java.util.Collection;

public class ServerHandler{

    // Players and Distances
    public static Player getPlayerByName(String playername){
        return Bukkit.getPlayer(playername);
    }

    public static Location getLocationFromArray(World w, int[] a){
        return Location.Location(w, a[0], a[1], a[2]);
    }

    public static int[] getPlayerLocation(String playername){
        Location l = getPlayerByName(playername).getLocation();
        int[] array = {l.getBlockX(),l.getBlockY(),l.getBlockZ()};
        return array;
    }

    public static long getLocationalDistance(Location loc1, Location loc2){
        long l = (long) loc1.distance(loc2);
        return l;
    }
    public static long getPlayerDistanceByNames(String p1, String p2){
        Location _p1,_p2;
        _p1=getPlayerByName(p1).getLocation();
        _p2=getPlayerByName(p2).getLocation();
        return getLocationalDistance(_p1, _p2);
    }

    public static long getPlayerDistance(Player p1, Player p2){
        return getLocationalDistance(p1.getLocation(),p2.getLocation());
    }

    public static void kickPlayerByName(String playername, String message){
        getPlayerByName(playername).kickPlayer(message);
    }

    // Bukkit 
    public static String getVersion(){
        return Bukkit.getBukkitVersion();
    }

    public static String getIP(){
        return Bukkit.getIp();
    }

    public static void reloadServer(){
        Bukkit.reload();
    }

    public static void banIP(String ip){
        Bukkit.banIP(ip);
    }

    public static void unbanIP(String ip){
        Bukkit.unbanIP(ip);
    }

    public static void sendmsg(String context){
        Bukkit.broadcastMessage(context);
    }

    //UI
    public static BossBar newBar(String title, String color, String style, String flag){
        BarColor c;
        BarFlag f;
        BarStyle s;
        BossBar b;

        switch (color){
            case "blue":
                b=BarColor.BLUE;
                break;
            case "green":
                b=BarColor.GREEN;
                break;
            case "pink":
                b=BarColor.PINK;
                break;
            case "purple":
                b=BarColor.PURPLE;
                break;
            case "red":
                b=BarColor.RED;
                break;
            case "white":
                b=BarColor.WHITE;
                break;
            case "yellow":
                b=BarColor.YELLOW;
                break;
            default:
                b=BarColor.PURPLE;
        }

        switch (style){
            case "seg10":
                s=BarStyle.SEGMENTED_10;
                break;
            case "seg12":
                s=BarStyle.SEGMENTED_12;
                break;
            case "seg20":
                s=BarStyle.SEGMENTED_20;
                break;
            case "seg6":
                s=BarStyle.SEGMENTED_6;
                break;
            case "solid":
                s=BarStyle.SOLID;
                break;
            default:
                s=BarStyle.SEGMENTED_10;
        }

        switch (flag){
            case "fog":
                f=BarFlag.CREATE_FOG;
                break;
            case "dark":
                f=BarFlag.DARKEN_SKY;
                break;
            case "music":
                f=BarFlag.PLAY_BOSS_MUSIC;
                break;
            default:
                f=BarFlag.CREATE_FOG;
        }

        b=Bukkit.createBossBar(title,c,s,f);
    }

    public static void addFlagToBar(BossBar bar){
        BarFlag f;
        switch (flag){
            case "fog":
                f=BarFlag.CREATE_FOG;
                break;
            case "dark":
                f=BarFlag.DARKEN_SKY;
                break;
            case "music":
                f=BarFlag.PLAY_BOSS_MUSIC;
                break;
            default:
                f=BarFlag.CREATE_FOG;
        }
    }
    
    public static void addPlayerToBar(Player p, BossBar b){
        b.addPlayer(p);
    }

    public static BossBar addAllToBar(BossBar b){
        for (Player p: Bukkit.getServer().getOnlinePlayers()){
            b.addPlayer(p);
        }
        return b;
    }
    
}