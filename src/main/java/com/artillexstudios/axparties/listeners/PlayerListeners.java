package com.artillexstudios.axparties.listeners;

import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.Optional;

import static com.artillexstudios.axparties.AxParties.CONFIG;
import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Optional<Party> partyOpt = PartyManager.getPartyOf(event.getPlayer());
        if (partyOpt.isEmpty()) return;

        Party party = partyOpt.get();

        var online = party.getOnlineMembers();
        online.remove(event.getPlayer());
        if (online.isEmpty() && CONFIG.getBoolean("disband-when-all-offline", false)) {
            party.clearInvites();
            PartyManager.getParties().remove(party.getName());
            return;
        }

        if (party.getOwner().equals(event.getPlayer()) && !CONFIG.getBoolean("allow-offline-owner", true)) {
            if (online.isEmpty()) return;

            for (Player member : online) {
                MESSAGEUTILS.sendLang(member, "new-owner", Map.of("%player%", party.getOwner().getName(), "%owner%", online.get(0).getName()));
            }
            party.setOwner(online.get(0));
            return;
        }
    }
}
