package com.artillexstudios.axparties.commands.subcommands;

import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

import static com.artillexstudios.axparties.AxParties.CONFIG;
import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public enum Leave {
    INSTANCE;

    public void execute(Player sender) {
        Optional<Party> optParty = PartyManager.getPartyOf(sender);
        if (optParty.isEmpty()) {
            MESSAGEUTILS.sendLang(sender, "errors.not-in-party");
            return;
        }

        Party party = optParty.get();

        for (Player member : party.getOnlineMembers()) {
            MESSAGEUTILS.sendLang(member, "leave.broadcast", Map.of("%player%", sender.getName()));
        }
        MESSAGEUTILS.sendLang(sender, "leave.self", Map.of("%player%", sender.getName()));
        party.removeMember(sender);

        if (party.getOwner().equals(sender)) {
            var online = party.getOnlineMembers();
            if (party.getMembers().isEmpty() || (online.isEmpty() && CONFIG.getBoolean("disband-when-all-offline", false))) {
                party.clearInvites();
                PartyManager.getParties().remove(party.getName());
                return;
            }

            for (Player member : online) {
                MESSAGEUTILS.sendLang(member, "new-owner", Map.of("%player%", party.getOwner().getName(), "%owner%", online.get(0).getName()));
            }
            party.setOwner(online.get(0));
            return;
        }
    }
}
