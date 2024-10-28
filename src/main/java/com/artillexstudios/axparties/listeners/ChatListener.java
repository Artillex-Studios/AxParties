package com.artillexstudios.axparties.listeners;

import com.artillexstudios.axparties.commands.subcommands.Chat;
import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(AsyncPlayerChatEvent event) {
        if (!Chat.INSTANCE.chatEnabled.contains(event.getPlayer())) return;
        Optional<Party> optParty = PartyManager.getPartyOf(event.getPlayer());
        if (optParty.isEmpty()) return;

        Party party = optParty.get();
        event.setCancelled(true);
        Chat.INSTANCE.send(event.getPlayer(), party, event.getMessage());
    }
}
