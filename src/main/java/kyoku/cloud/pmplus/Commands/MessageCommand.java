package kyoku.cloud.pmplus.Commands;

import kyoku.cloud.pmplus.PMPlus;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageCommand implements CommandExecutor {

    PMPlus plugin;
    public List<String> toggled = ToggleCommand.getToggled();

    public MessageCommand(PMPlus pmPlus) {
        plugin = pmPlus;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 1) {
                if (Bukkit.getOfflinePlayer(args[0]).getPlayer() != null) {
                    if (sender.hasPermission("pmplus.msg")) {
                        Player recipient = Bukkit.getOfflinePlayer(args[0]).getPlayer();
                        Player sendr = (Player) sender;
                        if (!(toggled.contains(recipient.getName())) || sender.hasPermission("pmplus.bypass")) {
                            // checks config to see if players can message themselves
                            if (((sender.getName() == recipient.getName() && PMPlus.plugin.getConfig().getBoolean("Options.AllowSelfMessage") == true) || (sender.getName() != recipient.getName()))) {
                                plugin.mM.setReplyTarget((Player) sender, recipient);

                                // set the message
                                StringBuilder sb = new StringBuilder();
                                for (int i = 1; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }
                                sb.setLength(sb.length() - 1);
                                String message = sb.toString();

                                // send the message to the msg sender
                                String msg = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.MessageToSender"));
                                msg = msg.replace("%sender%", ((Player) sender).getDisplayName());
                                msg = msg.replace("%message%", message);
                                msg = msg.replace("%recipient%", recipient.getDisplayName());
                                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                                    msg = PlaceholderAPI.setPlaceholders((Player) sender, msg);
                                }
                                sender.sendMessage(msg);

                                // send the message to the recipient
                                String msgto = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.MessageToRecipient"));
                                msgto = msgto.replace("%sender%", ((Player) sender).getDisplayName());
                                msgto = msgto.replace("%message%", message);
                                msgto = msgto.replace("%recipient%", recipient.getDisplayName());
                                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                                    msgto = PlaceholderAPI.setPlaceholders(sendr, msgto);
                                }
                                recipient.sendMessage(msgto);

                                // send message to players with social spy
                                for (Player spy : SocialSpyCommand.getSpies()) {
                                    if (!spy.getName().equals(sender.getName()) && !spy.getName().equals(recipient.getName())) {
                                        String msgspy = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.SocialSpyFormat"));
                                        msgspy = msgspy.replace("%sender%", ((Player) sender).getDisplayName());
                                        msgspy = msgspy.replace("%message%", message);
                                        msgspy = msgspy.replace("%receiver%", recipient.getDisplayName());
                                        spy.sendMessage(msgspy);
                                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                                            msgspy = PlaceholderAPI.setPlaceholders((Player) sender, msgspy);
                                        }
                                    }
                                }
                            } else {
                                String cannotmsgself = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.CannotMessageSelf"));
                                cannotmsgself = cannotmsgself.replace("%sender%", ((Player) sender).getDisplayName());
                                cannotmsgself = cannotmsgself.replace("%receiver%", recipient.getDisplayName());
                                sender.sendMessage(cannotmsgself);
                            }
                        } else {
                            // tell sender that recipient has messages off
                            String msgtoggled = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.RecipientMessagesOff"));
                            msgtoggled = msgtoggled.replace("%recipient%", recipient.getDisplayName());
                            msgtoggled = msgtoggled.replace("%sender%", ((Player) sender).getDisplayName());
                            sender.sendMessage(msgtoggled);
                            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                                msgtoggled = PlaceholderAPI.setPlaceholders(recipient, msgtoggled);
                            }
                        }
                    } else {
                        // tells sender that they do not have permission
                        String noperms = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.NoPermission"));
                        noperms = noperms.replace("%sender%", ((Player) sender).getDisplayName());
                        sender.sendMessage(noperms);
                    }
                } else {
                    // tells sender that the player they are trying to message cannot be found
                    String cannotfind = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.PlayerNotFound"));
                    cannotfind = cannotfind.replace("%sender%", ((Player) sender).getDisplayName());
                    cannotfind = cannotfind.replace("%recipient%", args[0]);
                    sender.sendMessage(cannotfind);
                }
            } else {
                String invalidargsmsg = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.MessageCommandUsage"));
                invalidargsmsg = invalidargsmsg.replace("%sender%", ((Player) sender).getDisplayName());
                sender.sendMessage(invalidargsmsg);
            }
        } else if (!(sender instanceof Player)) {
            // sends a message to console with information
            Bukkit.getServer().getLogger().info(ChatColor.RED + "Only players can use this command. " + ChatColor.WHITE + "We plan to implement messages from console in a future update.");
        }
        return true;
        }
    }