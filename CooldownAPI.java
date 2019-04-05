ackage com.ula.fallen.Utils;

import java.util.HashMap;

import com.ula.fallen.main;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Cooldowns
{
    private HashMap<Player, Integer> cooldown = new HashMap();

    public Integer getPlayer(Player player)
    {
        return (Integer)this.cooldown.get(player);
    }

    public boolean containsPlayer(Player player)
    {
        return this.cooldown.containsKey(player);
    }

    public void addPlayer(Player player, int seconds)
    {
        this.cooldown.put(player, Integer.valueOf(seconds));
    }

    public void removePlayer(Player player)
    {
        this.cooldown.remove(player);
    }

    public void runTimer(final Player player)
    {
        new BukkitRunnable()
        {
            public void run()
            {
                if (((Integer)Cooldowns.this.cooldown.get(player)).intValue() > 0) {
                    Cooldowns.this.cooldown.put(player, Integer.valueOf(((Integer)Cooldowns.this.cooldown.get(player)).intValue() - 1));
                }
                if (((Integer)Cooldowns.this.cooldown.get(player)).intValue() == 0)
                {
                    Cooldowns.this.cooldown.remove(player);

                    cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(main.class), 20L, 20L);
    }

    public int getDaysLeft(Player player)
    {
        return getPlayer(player).intValue() / 86400;
    }

    public int getHoursLeft(Player player)
    {
        return getPlayer(player).intValue() % 86400 / 3600;
    }

    public int getMinutesLeft(Player player)
    {
        return getPlayer(player).intValue() % 86400 % 3600 / 60;
    }

    public int getSecondsLeft(Player player)
    {
        return getPlayer(player).intValue() % 86400 % 3600 % 60;
    }
}
