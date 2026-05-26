package com.artillexstudios.axparties.invite;

import com.artillexstudios.axparties.party.Party;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InviteManager {
    private static final Set<Invite> invites = new HashSet<>();

    public static Set<Invite> getInvites() {
        invites.removeIf(Invite::hasExpired);
        return invites;
    }

    public static List<Invite> getInvitesOf(Player player) {
        List<Invite> inviteList = new ArrayList<>();

        for (Invite invite : getInvites()) {
            if (!invite.invited().equals(player)) continue;
            inviteList.add(invite);
        }

        return inviteList;
    }

    public static List<Invite> getInvitesOf(Party party) {
        List<Invite> inviteList = new ArrayList<>();

        for (Invite invite : getInvites()) {
            if (!invite.party().equals(party)) continue;
            inviteList.add(invite);
        }

        return inviteList;
    }
}
