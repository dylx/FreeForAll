package me.angrypostman.freeforall.commands;

import me.angrypostman.freeforall.FreeForAll;
import me.angrypostman.freeforall.data.DataStorage;
import me.angrypostman.freeforall.user.User;
import me.angrypostman.freeforall.user.UserData;
import me.angrypostman.freeforall.user.UserManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static me.angrypostman.freeforall.FreeForAll.doAsync;
import static me.angrypostman.freeforall.FreeForAll.doSync;

public class StatsCommand implements CommandExecutor {

    private FreeForAll plugin = null;
    private DataStorage storage = null;

    public StatsCommand(FreeForAll plugin) {
        this.plugin = plugin;
        this.storage = plugin.getDataStorage();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {

        if (!command.getName().equalsIgnoreCase("stats")) {

        } else if (!(commandSender instanceof Player)) {

        } else if (!(commandSender.hasPermission("freeforall.command.savekit"))) {

        } else {

            final Player player = (Player) commandSender;

            if (args.length >= 1 && player.hasPermission("freeforall.command.stats.viewOther")) {

                doAsync(() -> {

                    String lookupName = args[0].toLowerCase();
                    Optional<User> tempUser = UserManager.getUser(lookupName);
                    if (!tempUser.isPresent()) {
                        player.sendMessage(ChatColor.RED + "Failed to find " + lookupName + " in database records.");
                        return;
                    }

                    final User target = tempUser.get();
                    final UserData userData = target.getUserData();

                    doSync(()->{
                        player.sendMessage(ChatColor.GOLD + "FFA >> " + target.getName() + "'s stats");
                        player.sendMessage(ChatColor.GOLD + "FFA >> Ranking: 0");
                        player.sendMessage(ChatColor.GOLD + "FFA >> Points: " + userData.getPoints());
                        player.sendMessage(ChatColor.GOLD + "FFA >> Kills: " + userData.getKills());
                        player.sendMessage(ChatColor.GOLD + "FFA >> Deaths: " + userData.getDeaths());
                    });

                });

            } else {

                doAsync(() -> {

                    Optional<User> tempUser = UserManager.getUser(player.getUniqueId());
                    if (!tempUser.isPresent()) {
                        player.sendMessage(ChatColor.RED + "Failed to load your player data, please relog.");
                        return;
                    }

                    final User user = tempUser.get();
                    final UserData userData = user.getUserData();

                    doSync(()->{
                        player.sendMessage(ChatColor.GOLD + "FFA >> " + user.getName() + "'s stats");
                        player.sendMessage(ChatColor.GOLD + "FFA >> Ranking: 0");
                        player.sendMessage(ChatColor.GOLD + "FFA >> Points: " + userData.getPoints());
                        player.sendMessage(ChatColor.GOLD + "FFA >> Kills: " + userData.getKills());
                        player.sendMessage(ChatColor.GOLD + "FFA >> Deaths: " + userData.getDeaths());
                    });

                });

            }

        }

        return false;
    }

}
