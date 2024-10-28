package com.artillexstudios.axparties.commands.subcommands;

import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public enum Rename {
    INSTANCE;

    public void execute(Player sender, String name) {
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

        if (PartyManager.getPartyByName(name).isPresent()) {
            MESSAGEUTILS.sendLang(sender, "errors.already-exists", Map.of("%name%", name));
            return;
        }

        party.setName(name);
        for (Player member : party.getOnlineMembers()) {
            MESSAGEUTILS.sendLang(member, "rename.broadcast", Map.of("%party%", party.getName()));
        }
        MESSAGEUTILS.sendLang(sender, "rename.self", Map.of("%party%", party.getName()));
    }
}
