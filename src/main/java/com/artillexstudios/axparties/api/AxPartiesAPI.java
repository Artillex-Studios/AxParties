package com.artillexstudios.axparties.api;

import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class AxPartiesAPI {

    @Unmodifiable
    public static Map<String, Party> getParties() {
        return Collections.unmodifiableMap(PartyManager.getParties());
    }

    public static Optional<Party> getPartyOf(Player player) {
        return PartyManager.getPartyOf(player);
    }

    public static Optional<Party> getPartyByName(String name) {
        return PartyManager.getPartyByName(name);
    }
}
