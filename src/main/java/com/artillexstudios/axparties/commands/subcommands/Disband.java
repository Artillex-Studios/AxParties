package com.artillexstudios.axparties.commands.subcommands;

import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public enum Disband {
    INSTANCE;

    public void execute(Player sender) {
        Optional<Party> optParty = PartyManager.getPartyOf(sender);
        if (optParty.isEmpty()) {
            MESSAGEUTILS.sendLang(sender, "errors.not-in-party");
            return;
        }

        Party party = optParty.get();
        if (!party.getOwner().equals(sender)) {
            MESSAGEUTILS.sendLang(sender, "errors.not-the-leader");
            return;
        }

        for (Player member : party.getOnlineMembers()) {
            MESSAGEUTILS.sendLang(member, "disband.broadcast", Map.of("%party%", party.getName()));
        }
        MESSAGEUTILS.sendLang(sender, "disband.self", Map.of("%party%", party.getName()));
        PartyManager.getParties().remove(party.getName());
        party.clearInvites();
    }
}
