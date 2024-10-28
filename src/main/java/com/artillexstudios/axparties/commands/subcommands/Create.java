package com.artillexstudios.axparties.commands.subcommands;

import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.artillexstudios.axparties.AxParties.CONFIG;
import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public enum Create {
    INSTANCE;

    public void execute(Player sender, @Nullable String name) {
        if (PartyManager.getPartyOf(sender).isPresent()) {
            MESSAGEUTILS.sendLang(sender, "errors.already-in-party");
            return;
        }

        if (name == null) name = sender.getName();

        if (PartyManager.getPartyByName(name).isPresent()) {
            MESSAGEUTILS.sendLang(sender, "errors.already-exists", Map.of("%name%", name));
            return;
        }

        if (name.length() < CONFIG.getInt("name-rules.length.min", 3) || name.length() > CONFIG.getInt("name-rules.length.max", 16)) {
            MESSAGEUTILS.sendLang(sender, "errors.name-length");
            return;
        }

        for (String word : CONFIG.getStringList("name-rules.blacklisted-words")) {
            if (name.contains(word)) {
                MESSAGEUTILS.sendLang(sender, "errors.blacklisted");
                return;
            }
        }

        Party party = new Party(name, sender, System.currentTimeMillis());
        PartyManager.getParties().put(party.getName(), party);
        MESSAGEUTILS.sendLang(sender, "created", Map.of("%party%", party.getName()));
    }
}
