package me.Xeouz.ProximityMC.classes.jda;

// --- Importing JDA Natives
import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import net.dv8tion.jda.api.managers.AudioManager;

import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.EnumSet;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.security.auth.login.LoginException;
import java.lang.InterruptedException;

// --- Importing Bukkit Natives
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.EventHandler;

// --- Importing Server Handler
import me.Xeouz.ProximityMC.classes.server.ServerHandler;

// --- Importing Java Utils and Exceptions
import java.util.logging.Level;
import java.util.Arrays;
import java.util.List;

public class ProxCore extends ListenerAdapter implements Listener{
    public me.Xeouz.ProximityMC.Proximus PluginInstance;
    protected JDA bot;
    public ServerHandler handler;
    protected final String token = "OTIyNDE5ODIzMTg5ODI3NTk1.YcBMZw.Nymph7PHyagPmzcbksvwhn7DD5s";

    public TextChannel LiveChannel;
    public boolean LiveEnabled=false;
    
    //JDA
    public ProxCore(me.Xeouz.ProximityMC.Proximus main, ServerHandler hand){
        this.PluginInstance=main;
        log("CONFIG","--ProxPlugin-- Using GuildArray<Guild> Default List");
        this.handler=hand;
        log("INFO","--ProxPlugin-- Attempting Discord API Handshake || Establishing Connection to Discord API");
        try{
        construct();
        log("FINE","--ProxPlugin-- Successfully Connected to the Discord API");
        }
        catch (LoginException e){
        log("SEVERE","--ProxPlugin-- Could not Establish Connection to Discord API, Invalid Bot Token");
        }
        catch (InterruptedException e){
        log("SEVERE","--ProxPlugin-- Could not Establish Connection to Discord API, Invalid Bot Token");
        }
    }

    private void construct() throws LoginException, InterruptedException{
        //Bot build
        //bot = JDABuilder.createLight(token, EnumSet.noneOf(GatewayIntent.class)).addEventListeners(new proxbot()).build();
        
        bot = JDABuilder.createDefault(token)
        .setStatus(OnlineStatus.DO_NOT_DISTURB)
        .setActivity(Activity.listening("to Updates"))
        .addEventListeners(this)
        .build();

        bot.awaitReady();

        Guild guild;
        List l = bot.getGuilds();

        //Slash Commands
        for (int i=0;i<l.size();i+=1){
            guild=(Guild) l.get(i);
            update(guild);
        }
    }
        @Override
        public void onSlashCommand(SlashCommandEvent event){
        // Only accept commands from guilds
        if (event.getGuild() == null){ 
            return;
            }
        AudioManager ad;
        String name;
        MessageChannel mchannel;
        int[] loc;
        switch (event.getName())
        {
        case "join":
            VoiceChannel channel = event.getGuild().getVoiceChannelById(event.getOption("vc").getAsGuildChannel().getId());
            ad = event.getGuild().getAudioManager();
            ad.openAudioConnection(channel);
            event.reply("**`I am in that Voice Channel now (pog)`**").setEphemeral(true).queue();
            break;
        case "leave":
            ad = event.getGuild().getAudioManager();
            if (ad.isConnected()){
                ad.closeAudioConnection();
            }
            else{
                event.reply("**`Not in any Voice Channel yet bro`**");
            }
            break;
        case "echo":
            say(event, "You said - "+event.getOption("content").getAsString());
            break;
        case "register":
            break;
        case "unregister":
            break;
        case "getpos":
            name=event.getOption("name").getAsString();
            loc=handler.getPlayerLocation(name);
            event.reply("The Co-Oridnates are as Follows\n"+"X: "+loc[0]+"\nY: "+loc[1]+"\nZ: "+loc[2]);
            break;
        case "distance":
            String n1, n2;
            n1=event.getOption("first player").getAsString();
            n2=event.getOption("second player").getAsString();
            long d = handler.getPlayerDistanceByNames(n1,n2);
            event.reply("Distance is "+String.valueOf(d));
            break;
        case "locational distance":
            int x,y,z;
            name=event.getOption("player name").getAsString();
            x=Integer.parseInt(event.getOption("x").getAsString());
            y=Integer.parseInt(event.getOption("y").getAsString());
            z=Integer.parseInt(event.getOption("z").getAsString());
            int[] ar1 = {x,y,z};
            Player p = handler.getPlayerByName(name);
            long d = handler.getLocationalDistance(p, handler.getLocationFromArray(p.getWorld()));
            event.reply("Distance is "+String.valueOf(d));
            break;
        case "bind":
            mchannel=(TextChannel) event.getOption("channel").getAsMessageChannel();
            LiveChannel=mchannel;
            event.reply("Bound to "+mchannel.getName());
            LiveEnabled=true;
            break;
        default:
            event.reply("Random Error Occured Lmao.").setEphemeral(true).queue();
        }
    }
    public void say(SlashCommandEvent event, String content)
    {
        event.reply(content).queue(); // This requires no permissions!
    }

    public JDA getBot(){
        return bot;
    }

    public void update(Guild guildins){
        CommandListUpdateAction commands;
        log("INFO",guildins.getName());

        commands = guildins.updateCommands();

        //Join VC
        commands.addCommands(
            new CommandData("join","Joins a Voice Channel")
                .addOptions(new OptionData(CHANNEL, "vc", "Voice Channel")
                    .setRequired(true))
        );

        commands.addCommands(
            new CommandData("bind","Binds Server Live Chat to a Channel")
                .addOptions(new OptionData(CHANNEL, "channel", "Text Channel")
                    .setRequired(true))
        );

        //Register to ProxChat
        commands.addCommands(
            new CommandData("register","Register to Prox Chat")
        );

        //Unregister from ProxChat 
        commands.addCommands(
            new CommandData("unregister","Unregister from Prox Chat")
        );

        //Leave VC
        commands.addCommands(
            new CommandData("leave","Leave Voice Channel")
        );

        //Get Player Position
        commands.addCommands(
            new CommandData("getpos","Gets a Player Position")
                .addOptions(new OptionData(STRING, "player name", "Name of the Player")
                    .setRequired(true))
        );

        //Get Player Distance 
        commands.addCommands(
            new CommandData("distance","Gets a Player distance from another Player")
                .addOptions(new OptionData(STRING, "first player", "Name of the First Player")
                    .setRequired(true))
                        .addOptions(new OptionData(STRING, "second player","Name of the Second Player")
                            .setRequired(true))
        );

        commands.addCommands(
            new CommandData("locational distance","Gets Player distance from a different location")
                .addOptions(new OptionData(STRING, "player name","Name of the Player")
                    .setRequired(true))
                        .addOptions(new OptionData(INTEGER, "x","X Co-ordinate of the Target Location")
                            .setRequired(true))
                                .addOptions(new OptionData(INTEGER, "y","Y Co-ordinate of the Target Location")
                                    .setRequired(true))
                                        .addOptions(new OptionData(INTEGER, "z","Z Co-ordinate of the Target Location")
                                            .setRequired(true))
        );

        //Echo Debug
        commands.addCommands(
            new CommandData("echo","Echo for Debugging")
                .addOptions(new OptionData(STRING, "content","Text to Echo")
                    .setRequired(true))
        );

        commands.queue();
        
        log("FINER","--ProxPlugin-- Loaded all Commands");
    }

    public void log(String LEVEL,String input){
        Level l;
        ChatColor c;
        switch(LEVEL){
        case "SEVERE":
            l=Level.SEVERE;
            c=ChatColor.DARK_RED;
        case "WARNING":
            l=Level.WARNING;
            c=ChatColor.RED;
        case "INFO":
            l=Level.INFO;
            c=ChatColor.BLACK;
        case "CONFIG":
            l=Level.CONFIG;
            c=ChatColor.GRAY;
        case "FINE":
            l=Level.FINE;
            c=ChatColor.BLUE;
        case "FINER":
            l=Level.FINER;
            c=ChatColor.GREEN;
        case "FINEST":
            l=Level.FINEST;
            c=ChatColor.DARK_GREEN;
        default:
            l=Level.INFO;
            c=ChatColor.BLACK;
        }
        PluginInstance.getLogger().log(l, c+input);
    }

    //Bukkit
    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event){
        if (LiveEnabled){
            String msg = event.getMessage();
            LiveChannel.sendMessage("**"+event.getPlayer().getName()+"** says - "+msg).queue();
        }
    }
    
    @Override
    public void onGuildMesssageRecieved(GuildMessageReceivedEvent event){
        if (event.getAuthor().isBot() || event.getAuthor().isFake() || event.isWebhookMesage() || !LiveEnabled){
            return;
        }
        Bukkit.broadcastMessage("&b"+event.getAuthor().getName()+"&b"+" says - "+event.getMessage().getContentRaw());
    }
    }
