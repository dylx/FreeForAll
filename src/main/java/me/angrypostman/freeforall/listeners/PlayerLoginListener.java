package me.angrypostman.freeforall.listeners;

import me.angrypostman.freeforall.FreeForAll;
import me.angrypostman.freeforall.data.DataStorage;
import me.angrypostman.freeforall.user.User;
import me.angrypostman.freeforall.user.UserManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;
import java.util.UUID;

public class PlayerLoginListener implements Listener {

    private FreeForAll plugin = null;
    private DataStorage storage = null;
    public PlayerLoginListener(FreeForAll plugin) {
        this.plugin = plugin;
        this.storage = plugin.getDataStorage();
    }

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {

        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;

        String playerName = event.getName();
        UUID playerUUID = event.getUniqueId();

        Optional<User> optional = storage.loadUser(playerUUID);
        if (!optional.isPresent()) {
            optional = storage.createUser(playerUUID, playerName);
            if (!optional.isPresent()) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Failed to load player data, please relog");
                return;
            }w
        }

        User user = optional.get();

        if (!user.getName().equals(playerName)) {
            user.setName(playerName);
            storage.saveUser(user);
        }

        UserManager.getUsers().add(user);

    }

}
