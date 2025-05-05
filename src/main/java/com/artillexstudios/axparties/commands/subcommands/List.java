package com.artillexstudios.axparties.commands.subcommands;

import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.artillexstudios.axparties.AxParties.LANG;
import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public enum List {
    INSTANCE;

    public void execute(Player sender, @Nullable Party party) {
        if (party == null) {
            Optional<Party> optParty = PartyManager.getPartyOf(sender);
            if (optParty.isEmpty()) {
                MESSAGEUTILS.sendLang(sender, "errors.not-in-party");
                return;
            }
            party = optParty.get();
        }

        Map<String, String> rp = new HashMap<>();
        rp.put("%party%", party.getName());
        rp.put("%leader%", party.getOwner().getName());
        rp.put("%online%", String.join(", ", party.getOnlineMembers().stream().map(HumanEntity::getName).toList()));
        rp.put("%offline%", String.join(", ", party.getOfflineMembers().stream().map(OfflinePlayer::getName).toList()));

        for (String list : LANG.getStringList("list")) {
            sender.sendMessage(StringUtils.formatToString(list, rp));
        }
    }
}
