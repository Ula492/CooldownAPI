package com.ula.groovy.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class Cooldown
  implements Listener
{
  private static List<String> commands = new ArrayList();
  private static Plugin PLUGIN;
  private static Boolean COMMANDCOOLDOWN = Boolean.valueOf(false);
  private static Boolean PERMCACHE = Boolean.valueOf(false);
  private static Integer STANDARDVALUETIME = Integer.valueOf(0);
  private static String STANDARDTIMETYPE = null;
  
  public Cooldown(Plugin plugin)
  {
    PLUGIN = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, PLUGIN);
  }
  
  public Cooldown(Plugin plugin, Boolean activateIntelligentCommandCooldown, Boolean togglePermCache, Integer cooldownValue, String cooldownTimeType)
  {
    PLUGIN = plugin;
    COMMANDCOOLDOWN = activateIntelligentCommandCooldown;
    STANDARDVALUETIME = cooldownValue;
    STANDARDTIMETYPE = cooldownTimeType;
    PERMCACHE = togglePermCache;
    plugin.getServer().getPluginManager().registerEvents(this, PLUGIN);
  }
  
  public void addCommand(String... commands)
  {
    commands.addAll(Arrays.asList(commands));
  }
  
  public static Boolean hasCooldown(Player p, String command)
  {
    Long cool = getData(p, command);
    Long curUnix = Long.valueOf(new Date().getTime() / 1000L);
    if (cool.longValue() > curUnix.longValue()) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  public static Boolean hasCooldown(Player p)
  {
    Long cool = getData(p);
    Long curUnix = Long.valueOf(new Date().getTime() / 1000L);
    if (cool.longValue() > curUnix.longValue()) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  public static String returnCooldown(Player player, String command)
  {
    Long currentUnix = Long.valueOf(new Date().getTime() / 1000L);
    Long cooldown = getData(player, command);
    
    String cdReturn = null;
    if (cooldown.longValue() > currentUnix.longValue())
    {
      Double TimeLeft = Double.valueOf(cooldown.longValue() - currentUnix.longValue());
      String TimeOutputSeconds = "";
      String TimeOutputMinutes = "";
      String TimeOutputHours = "";
      String TimeOutputDays = "";
      if (TimeLeft.doubleValue() > 59.0D)
      {
        if (TimeLeft.doubleValue() > 3599.0D)
        {
          if (TimeLeft.doubleValue() > 86399.0D)
          {
            Integer TimeDays = Integer.valueOf(0);
            TimeDays = Integer.valueOf((int)(TimeLeft.doubleValue() / 86400.0D));
            Double TimeHours = Double.valueOf(0.0D);
            TimeHours = Double.valueOf(TimeLeft.doubleValue() / 3600.0D);
            Double TimeMinutes = Double.valueOf(0.0D);
            TimeMinutes = Double.valueOf((TimeHours.doubleValue() - (int)(TimeLeft.doubleValue() / 3600.0D)) * 60.0D);
            Double TimeSeconds = Double.valueOf(0.0D);
            TimeSeconds = Double.valueOf((TimeMinutes.doubleValue() - (int)TimeMinutes.doubleValue()) * 60.0D);
            if (TimeDays.intValue() == 1) {
              TimeOutputDays = "Day";
            } else {
              TimeOutputDays = "Days";
            }
            if (TimeMinutes.doubleValue() == 1.0D) {
              TimeOutputMinutes = "Minute";
            } else if ((TimeMinutes.doubleValue() == 0.0D) || (TimeMinutes.doubleValue() > 1.0D)) {
              TimeOutputMinutes = "Minutes";
            }
            if (TimeSeconds.doubleValue() == 1.0D) {
              TimeOutputSeconds = "Second";
            } else if ((TimeSeconds.doubleValue() == 0.0D) || (TimeSeconds.doubleValue() > 1.0D)) {
              TimeOutputSeconds = "Seconds";
            }
            cdReturn = TimeDays.intValue() + " " + TimeOutputDays + ", " + TimeHours + " " + TimeOutputHours + ", " + (int)TimeMinutes.doubleValue() + " " + TimeOutputMinutes + ", " + (int)Math.round(TimeSeconds.doubleValue()) + " " + TimeOutputSeconds;
          }
          else
          {
            Double TimeHours = Double.valueOf(0.0D);
            TimeHours = Double.valueOf(TimeLeft.doubleValue() / 3600.0D);
            Double TimeMinutes = Double.valueOf(0.0D);
            TimeMinutes = Double.valueOf((TimeHours.doubleValue() - (int)(TimeLeft.doubleValue() / 3600.0D)) * 60.0D);
            Double TimeSeconds = Double.valueOf(0.0D);
            TimeSeconds = Double.valueOf((TimeMinutes.doubleValue() - (int)TimeMinutes.doubleValue()) * 60.0D);
            if (TimeHours.doubleValue() == 1.0D) {
              TimeOutputHours = "Hour";
            } else {
              TimeOutputHours = "Hours";
            }
            if (TimeMinutes.doubleValue() == 1.0D) {
              TimeOutputMinutes = "Minute";
            } else if ((TimeMinutes.doubleValue() == 0.0D) || (TimeMinutes.doubleValue() > 1.0D)) {
              TimeOutputMinutes = "Minutes";
            }
            if (TimeSeconds.doubleValue() == 1.0D) {
              TimeOutputSeconds = "Second";
            } else if ((TimeSeconds.doubleValue() == 0.0D) || (TimeSeconds.doubleValue() > 1.0D)) {
              TimeOutputSeconds = "Seconds";
            }
            cdReturn = (int)TimeHours.doubleValue() + " " + TimeOutputHours + ", " + (int)TimeMinutes.doubleValue() + " " + TimeOutputMinutes + ", " + (int)Math.round(TimeSeconds.doubleValue()) + " " + TimeOutputSeconds;
          }
        }
        else
        {
          Double TimeMinutes = Double.valueOf(0.0D);
          TimeMinutes = Double.valueOf(TimeLeft.doubleValue() / 60.0D);
          Double TimeSeconds = Double.valueOf(0.0D);
          TimeSeconds = Double.valueOf((TimeMinutes.doubleValue() - (int)(TimeLeft.doubleValue() / 60.0D)) * 60.0D);
          if (TimeMinutes.doubleValue() == 1.0D) {
            TimeOutputMinutes = "Minute";
          } else {
            TimeOutputMinutes = "Minutes";
          }
          if (TimeSeconds.doubleValue() == 1.0D) {
            TimeOutputSeconds = "Second";
          } else if (TimeSeconds.doubleValue() == 0.0D) {
            TimeOutputSeconds = "Seconds";
          } else {
            TimeOutputSeconds = "Seconds";
          }
          cdReturn = (int)TimeMinutes.doubleValue() + " " + TimeOutputMinutes + ", " + (int)Math.round(TimeSeconds.doubleValue()) + " " + TimeOutputSeconds;
        }
      }
      else
      {
        if (TimeLeft.doubleValue() == 1.0D) {
          TimeOutputSeconds = "Second";
        } else {
          TimeOutputSeconds = "Seconds";
        }
        cdReturn = (int)TimeLeft.doubleValue() + " " + TimeOutputSeconds;
      }
    }
    return cdReturn;
  }
  
  public static String returnCooldown(Player player)
  {
    Long currentUnix = Long.valueOf(new Date().getTime() / 1000L);
    Long cooldown = getData(player);
    
    String cdReturn = null;
    if (cooldown.longValue() > currentUnix.longValue())
    {
      Double TimeLeft = Double.valueOf(cooldown.longValue() - currentUnix.longValue());
      String TimeOutputSeconds = "";
      String TimeOutputMinutes = "";
      String TimeOutputHours = "";
      String TimeOutputDays = "";
      if (TimeLeft.doubleValue() > 59.0D)
      {
        if (TimeLeft.doubleValue() > 3599.0D)
        {
          if (TimeLeft.doubleValue() > 86399.0D)
          {
            Integer TimeDays = Integer.valueOf(0);
            TimeDays = Integer.valueOf((int)(TimeLeft.doubleValue() / 86400.0D));
            Double TimeHours = Double.valueOf(0.0D);
            TimeHours = Double.valueOf(TimeLeft.doubleValue() / 3600.0D);
            Double TimeMinutes = Double.valueOf(0.0D);
            TimeMinutes = Double.valueOf((TimeHours.doubleValue() - (int)(TimeLeft.doubleValue() / 3600.0D)) * 60.0D);
            Double TimeSeconds = Double.valueOf(0.0D);
            TimeSeconds = Double.valueOf((TimeMinutes.doubleValue() - (int)TimeMinutes.doubleValue()) * 60.0D);
            if (TimeDays.intValue() == 1) {
              TimeOutputDays = "Day";
            } else {
              TimeOutputDays = "Days";
            }
            if (TimeMinutes.doubleValue() == 1.0D) {
              TimeOutputMinutes = "Minute";
            } else if ((TimeMinutes.doubleValue() == 0.0D) || (TimeMinutes.doubleValue() > 1.0D)) {
              TimeOutputMinutes = "Minutes";
            }
            if (TimeSeconds.doubleValue() == 1.0D) {
              TimeOutputSeconds = "Second";
            } else if ((TimeSeconds.doubleValue() == 0.0D) || (TimeSeconds.doubleValue() > 1.0D)) {
              TimeOutputSeconds = "Seconds";
            }
            cdReturn = TimeDays.intValue() + " " + TimeOutputDays + ", " + TimeHours + " " + TimeOutputHours + ", " + (int)TimeMinutes.doubleValue() + " " + TimeOutputMinutes + ", " + (int)Math.round(TimeSeconds.doubleValue()) + " " + TimeOutputSeconds;
          }
          else
          {
            Double TimeHours = Double.valueOf(0.0D);
            TimeHours = Double.valueOf(TimeLeft.doubleValue() / 3600.0D);
            Double TimeMinutes = Double.valueOf(0.0D);
            TimeMinutes = Double.valueOf((TimeHours.doubleValue() - (int)(TimeLeft.doubleValue() / 3600.0D)) * 60.0D);
            Double TimeSeconds = Double.valueOf(0.0D);
            TimeSeconds = Double.valueOf((TimeMinutes.doubleValue() - (int)TimeMinutes.doubleValue()) * 60.0D);
            if (TimeHours.doubleValue() == 1.0D) {
              TimeOutputHours = "Hour";
            } else {
              TimeOutputHours = "Hours";
            }
            if (TimeMinutes.doubleValue() == 1.0D) {
              TimeOutputMinutes = "Minute";
            } else if ((TimeMinutes.doubleValue() == 0.0D) || (TimeMinutes.doubleValue() > 1.0D)) {
              TimeOutputMinutes = "Minutes";
            }
            if (TimeSeconds.doubleValue() == 1.0D) {
              TimeOutputSeconds = "Second";
            } else if ((TimeSeconds.doubleValue() == 0.0D) || (TimeSeconds.doubleValue() > 1.0D)) {
              TimeOutputSeconds = "Seconds";
            }
            cdReturn = (int)TimeHours.doubleValue() + " " + TimeOutputHours + ", " + (int)TimeMinutes.doubleValue() + " " + TimeOutputMinutes + ", " + (int)Math.round(TimeSeconds.doubleValue()) + " " + TimeOutputSeconds;
          }
        }
        else
        {
          Double TimeMinutes = Double.valueOf(0.0D);
          TimeMinutes = Double.valueOf(TimeLeft.doubleValue() / 60.0D);
          Double TimeSeconds = Double.valueOf(0.0D);
          TimeSeconds = Double.valueOf((TimeMinutes.doubleValue() - (int)(TimeLeft.doubleValue() / 60.0D)) * 60.0D);
          if (TimeMinutes.doubleValue() == 1.0D) {
            TimeOutputMinutes = "Minute";
          } else {
            TimeOutputMinutes = "Minutes";
          }
          if (TimeSeconds.doubleValue() == 1.0D) {
            TimeOutputSeconds = "Second";
          } else if (TimeSeconds.doubleValue() == 0.0D) {
            TimeOutputSeconds = "Seconds";
          } else {
            TimeOutputSeconds = "Seconds";
          }
          cdReturn = (int)TimeMinutes.doubleValue() + " " + TimeOutputMinutes + ", " + (int)Math.round(TimeSeconds.doubleValue()) + " " + TimeOutputSeconds;
        }
      }
      else
      {
        if (TimeLeft.doubleValue() == 1.0D) {
          TimeOutputSeconds = "Second";
        } else {
          TimeOutputSeconds = "Seconds";
        }
        cdReturn = (int)TimeLeft.doubleValue() + " " + TimeOutputSeconds;
      }
    }
    return cdReturn;
  }
  
  public static void activateCooldown(Player player, String command, String time, Integer cooldown)
  {
    Long currentUnix = Long.valueOf(new Date().getTime() / 1000L);
    
    String MuteTimeOutput = "";
    Long MutedUntil = Long.valueOf(0L);
    try
    {
      if (time == "s")
      {
        if (cooldown.intValue() == 1) {
          MuteTimeOutput = "Second";
        } else {
          MuteTimeOutput = "Seconds";
        }
        MutedUntil = Long.valueOf(currentUnix.longValue() + cooldown.intValue());
      }
      else if (time == "m")
      {
        if (cooldown.intValue() == 1) {
          MuteTimeOutput = "Minute";
        } else {
          MuteTimeOutput = "Minutes";
        }
        MutedUntil = Long.valueOf(currentUnix.longValue() + 60 * cooldown.intValue());
      }
      else if (time == "h")
      {
        if (cooldown.intValue() == 1) {
          MuteTimeOutput = "Hour";
        } else {
          MuteTimeOutput = "Hours";
        }
        MutedUntil = Long.valueOf(currentUnix.longValue() + 3600 * cooldown.intValue());
      }
      else if (time == "d")
      {
        if (cooldown.intValue() == 1) {
          MuteTimeOutput = "Day";
        } else {
          MuteTimeOutput = "Days";
        }
        MutedUntil = Long.valueOf(currentUnix.longValue() + 86400 * cooldown.intValue());
      }
      setPlayerData(command, player, MutedUntil);
    }
    catch (NumberFormatException e)
    {
      System.err.println("Please use s,m,h,d as Time!" + e.getMessage());
    }
  }
  
  public static void activateCooldown(Player player, String time, Integer cooldown)
  {
    Long currentUnix = Long.valueOf(new Date().getTime() / 1000L);
    
    String MuteTimeOutput = "";
    Long MutedUntil = Long.valueOf(0L);
    try
    {
      if (time == "s")
      {
        if (cooldown.intValue() == 1) {
          MuteTimeOutput = "Second";
        } else {
          MuteTimeOutput = "Seconds";
        }
        MutedUntil = Long.valueOf(currentUnix.longValue() + cooldown.intValue());
      }
      else if (time == "m")
      {
        if (cooldown.intValue() == 1) {
          MuteTimeOutput = "Minute";
        } else {
          MuteTimeOutput = "Minutes";
        }
        MutedUntil = Long.valueOf(currentUnix.longValue() + 60 * cooldown.intValue());
      }
      else if (time == "h")
      {
        if (cooldown.intValue() == 1) {
          MuteTimeOutput = "Hour";
        } else {
          MuteTimeOutput = "Hours";
        }
        MutedUntil = Long.valueOf(currentUnix.longValue() + 3600 * cooldown.intValue());
      }
      else if (time == "d")
      {
        if (cooldown.intValue() == 1) {
          MuteTimeOutput = "Day";
        } else {
          MuteTimeOutput = "Days";
        }
        MutedUntil = Long.valueOf(currentUnix.longValue() + 86400 * cooldown.intValue());
      }
      setPlayerData(player, MutedUntil);
    }
    catch (NumberFormatException e)
    {
      System.err.println("Please use s,m,h,d as Time!" + e.getMessage());
    }
  }
  
  private static void cachePlayer(Player p)
  {
    File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    cfg.set("Cooldown.normalCooldown", Integer.valueOf(0));
    if (commands.size() >= 1) {
      for (int i = 0; i < commands.size(); i++) {
        cfg.set("Cooldown." + (String)commands.get(i), Integer.valueOf(0));
      }
    }
    try
    {
      cfg.save(file);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private static void removePlayerCache(Player p)
  {
    File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
    if (file.exists())
    {
      file.delete();
    }
    else
    {
      System.out.println("[Cooldown] Player Cache '" + p.getName() + "' not found. Abort.");
      return;
    }
  }
  
  private static Boolean isOnCooldown(Player p)
  {
    File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    Long curUnix = Long.valueOf(new Date().getTime() / 1000L);
    Long pUnix = Long.valueOf(cfg.getLong("Cooldown.normalCooldown"));
    if (pUnix.longValue() > curUnix.longValue()) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  private static Boolean isPlayerCache(Player p)
  {
    File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
    return Boolean.valueOf(file.exists());
  }
  
  private static Boolean isOnCooldown(Player p, String command)
  {
    File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    Long curUnix = Long.valueOf(new Date().getTime() / 1000L);
    Long pUnix = Long.valueOf(cfg.getLong("Cooldown." + command));
    if (pUnix.longValue() > curUnix.longValue()) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  private static void setPlayerData(String cmd, Player p, Long value)
  {
    File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    cfg.set("Cooldown." + cmd, value);
    try
    {
      cfg.save(file);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private static void setPlayerData(Player p, Long value)
  {
    File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    cfg.set("Cooldown.normalCooldown", value);
    try
    {
      cfg.save(file);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private static Long getData(Player p, String command)
  {
    File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    return Long.valueOf(cfg.getLong("Cooldown." + command));
  }
  
  private static Long getData(Player p)
  {
    File file = new File("plugins/" + PLUGIN.getDescription().getName() + "/Players", p.getName() + ".yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    return Long.valueOf(cfg.getLong("Cooldown.normalCooldown"));
  }
  
  @EventHandler(priority=EventPriority.LOW)
  private void onPlayerJoin(PlayerJoinEvent e)
  {
    Player p = e.getPlayer();
    if (isPlayerCache(p).booleanValue())
    {
      System.out.println("Player '" + p.getName() + "' is cached already. Abort.");
      return;
    }
    cachePlayer(p);
  }
  
  @EventHandler(priority=EventPriority.LOW)
  private void onPlayerLeave(PlayerQuitEvent e)
  {
    if (PERMCACHE.booleanValue())
    {
      Player p = e.getPlayer();
      if (isOnCooldown(p).booleanValue())
      {
        System.out.println("Player '" + p.getName() + "' still on Normal Cooldown. Abort.");
        return;
      }
      int i = 0;
      if (i < commands.size())
      {
        String cmd = (String)commands.get(i);
        if (isOnCooldown(p, cmd).booleanValue())
        {
          System.out.println("Player '" + p.getName() + "' still on Command Cooldown. Abort.");
          return;
        }
        removePlayerCache(p);
        return;
      }
    }
    else
    {
      Player p = e.getPlayer();
      removePlayerCache(p);
    }
  }
  
  @EventHandler(priority=EventPriority.HIGH)
  private void onPlayerChat(PlayerCommandPreprocessEvent e)
  {
    if (COMMANDCOOLDOWN.booleanValue())
    {
      Player p = e.getPlayer();
      String cmd = e.getMessage();
      for (int i = 0; i < commands.size(); i++)
      {
        String cmdT = (String)commands.get(i);
        if (cmd.contains(cmdT)) {
          if (hasCooldown(p, cmdT).booleanValue())
          {
            e.setCancelled(true);
            String cd = returnCooldown(p, cmdT);
            p.sendMessage("�cYou still have a cooldown! Time left: " + cd);
          }
          else
          {
            activateCooldown(p, cmdT, STANDARDTIMETYPE, STANDARDVALUETIME);
          }
        }
      }
    }
    else {}
  }
}
