package com.artillexstudios.axparties.invite;

import com.artillexstudios.axparties.party.Party;
import org.bukkit.OfflinePlayer;

import static com.artillexstudios.axparties.AxParties.CONFIG;

public record Invite(Party party, OfflinePlayer invited, long date) {

    public boolean hasExpired() {
        long expire = CONFIG.getInt("invite-expiration-seconds") * 1_000L;
        return date + expire < System.currentTimeMillis();
    }
}
