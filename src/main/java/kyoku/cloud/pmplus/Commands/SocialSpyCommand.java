package kyoku.cloud.pmplus.Commands;

import kyoku.cloud.pmplus.PMPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SocialSpyCommand implements CommandExecutor {

    public static List<Player> spies = new ArrayList<>();

    public static List<Player> getSpies() {
        return spies;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("pmplus.socialspy")) {
                if (!spies.contains(p)) {
                    // toggles sender's social spy on if they had it off
                    spies.add(p);
                    String spyenabled = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.SocialSpyEnabled"));
                    spyenabled = spyenabled.replace("%sender%", ((Player) sender).getDisplayName());
                    sender.sendMessage(spyenabled);
                } else {
                    // toggles sender's social spy off if they had it on
                    spies.remove(p);
                    String spydisabled = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.SocialSpyDisabled"));
                    spydisabled = spydisabled.replace("%sender%", ((Player) sender).getDisplayName());
                    sender.sendMessage(spydisabled);
                }
            } else {
                // tells sender that they do not have permission
                String noperms = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.NoPermission"));
                noperms = noperms.replace("%sender%", ((Player) sender).getDisplayName());
                sender.sendMessage(noperms);
            }
        } else {
            // sends a message to console with information
            Bukkit.getServer().getLogger().info(ChatColor.RED + "Only players can use that command.");
        }
        return true;
    }
}
