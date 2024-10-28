package com.artillexstudios.axparties.party;

import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PartyManager {
    private static final ConcurrentHashMap<String, Party> parties = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Party> getParties() {
        return parties;
    }

    public static Optional<Party> getPartyOf(Player player) {
        return parties.values().stream().filter(party -> party.getMembers().contains(player)).findAny();
    }

    public static Optional<Party> getPartyByName(String name) {
        return parties.values().stream().filter(party -> party.getName().equalsIgnoreCase(name)).findAny();
    }
}
