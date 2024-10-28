package com.artillexstudios.axparties.commands.subcommands;

import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public enum Transfer {
    INSTANCE;

    public void execute(Player sender, Player player) {
        if (sender.equals(player)) {
            MESSAGEUTILS.sendLang(sender, "errors.self");
            return;
        }

        Optional<Party> optParty = PartyManager.getPartyOf(sender);
        if (optParty.isEmpty()) {
            MESSAGEUTILS.sendLang(sender, "errors.not-in-party");
            return;
        }

        Optional<Party> optParty2 = PartyManager.getPartyOf(player);
        if (!optParty2.equals(optParty)) {
            MESSAGEUTILS.sendLang(sender, "errors.not-same-party");
            return;
        }

        Party party = optParty.get();
        if (!party.getOwner().equals(sender)) {
            MESSAGEUTILS.sendLang(sender, "errors.not-the-leader");
            return;
        }

        for (Player member : party.getOnlineMembers()) {
            MESSAGEUTILS.sendLang(member, "transfer.broadcast", Map.of("%player%", player.getName()));
        }
        MESSAGEUTILS.sendLang(sender, "transfer.self", Map.of("%player%", player.getName()));
        party.setOwner(player);
    }
}
