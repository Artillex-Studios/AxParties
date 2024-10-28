package com.artillexstudios.axparties.commands.subcommands;

import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

import static com.artillexstudios.axparties.AxParties.LANG;
import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public enum Chat {
    INSTANCE;

    public final Set<Player> chatEnabled = Collections.newSetFromMap(new WeakHashMap<>());

    public void execute(Player sender, @Nullable String message) {
        Optional<Party> optParty = PartyManager.getPartyOf(sender);
        if (optParty.isEmpty()) {
            MESSAGEUTILS.sendLang(sender, "errors.not-in-party");
            return;
        }

        Party party = optParty.get();
        if (message != null) {
            send(sender, party, message);
            return;
        }

        if (chatEnabled.contains(sender)) {
            chatEnabled.remove(sender);
            MESSAGEUTILS.sendLang(sender, "chat.disabled");
        } else {
            chatEnabled.add(sender);
            MESSAGEUTILS.sendLang(sender, "chat.enabled");
        }
    }

    public void send(Player player, Party party, String message) {
        for (Player member : party.getOnlineMembers()) {
            member.sendMessage(StringUtils.formatToString(
                    LANG.getString("chat.format"),
                    Map.of("%player%", player.getName(), "%message%", message)
            ));
        }
    }
}
