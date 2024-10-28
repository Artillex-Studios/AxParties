package com.artillexstudios.axparties.commands.subcommands;

import com.artillexstudios.axparties.invite.Invite;
import com.artillexstudios.axparties.invite.InviteManager;
import com.artillexstudios.axparties.party.Party;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public enum Deny {
    INSTANCE;

    public void execute(Player sender, @Nullable Invite invite) {
        if (invite == null) {
            invite = InviteManager.getInvitesOf(sender).get(0);
            if (invite == null) {
                MESSAGEUTILS.sendLang(sender, "errors.no-invites");
                return;
            }
        }

        Party party = invite.party();

        for (Player member : party.getOnlineMembers()) {
            MESSAGEUTILS.sendLang(member, "deny.broadcast", Map.of("%player%", sender.getName()));
        }
        MESSAGEUTILS.sendLang(sender, "deny.self", Map.of("%party%", party.getName()));
        InviteManager.getInvites().remove(invite);
    }
}
