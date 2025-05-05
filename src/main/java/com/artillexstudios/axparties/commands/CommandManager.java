package com.artillexstudios.axparties.commands;

import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axparties.AxParties;
import com.artillexstudios.axparties.commands.annotations.NoParty;
import com.artillexstudios.axparties.commands.annotations.PartyMember;
import com.artillexstudios.axparties.invite.Invite;
import com.artillexstudios.axparties.invite.InviteManager;
import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import com.artillexstudios.axparties.utils.CommandMessages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.orphan.Orphans;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static com.artillexstudios.axparties.AxParties.CONFIG;
import static com.artillexstudios.axparties.AxParties.LANG;

public class CommandManager {
    private static BukkitCommandHandler handler = null;

    public static void load() {
        handler = BukkitCommandHandler.create(AxParties.getInstance());

        handler.getAutoCompleter().registerSuggestionFactory(parameter -> {
            if (parameter.hasAnnotation(PartyMember.class)) {
                return (args, sender, command) -> {
                    Player player = sender.as(BukkitCommandActor.class).getAsPlayer();
                    var party = PartyManager.getPartyOf(player);
                    if (party.isEmpty()) return List.of();
                    return party.get().getMembers().stream().filter(player1 -> !player1.equals(player)).map(OfflinePlayer::getName).filter(Objects::nonNull).toList();
                };
            }
            if (parameter.hasAnnotation(NoParty.class)) {
                return (args, sender, command) -> {
                    List<Player> players = new ArrayList<>();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (PartyManager.getPartyOf(player).isEmpty()) players.add(player);
                    }
                    return players.stream().map(HumanEntity::getName).toList();
                };
            }
            return null;
        });

        handler.registerValueResolver(Invite.class, resolver -> {
            final String inviteName = resolver.popForParameter();
            var invites = InviteManager.getInvitesOf(resolver.actor().as(BukkitCommandActor.class).getAsPlayer());
            Optional<Invite> inviteOpt = invites.stream().filter(invite -> invite.party().getName().equals(inviteName)).findAny();
            if (inviteOpt.isEmpty()) {
                throw new CommandErrorException(StringUtils.formatToString(
                        CONFIG.getString("prefix") + LANG.getString("errors.not-invited")
                ));
            }

            return inviteOpt.get();
        });

        handler.getAutoCompleter().registerParameterSuggestions(Invite.class, (args, sender, command) -> {
            Player player = sender.as(BukkitCommandActor.class).getAsPlayer();
            return InviteManager.getInvitesOf(player).stream().map(invite -> invite.party().getName()).toList();
        });

        handler.registerValueResolver(Party.class, resolver -> {
            final String partyName = resolver.popForParameter();
            Party party = PartyManager.getParties().get(partyName);
            if (party == null) {
                throw new CommandErrorException(StringUtils.formatToString(
                        CONFIG.getString("prefix") + LANG.getString("errors.not-found")
                ));
            }
            return party;
        });

        handler.getAutoCompleter().registerParameterSuggestions(Party.class, (args, sender, command) -> {
            return PartyManager.getParties().keySet();
        });

        handler.getTranslator().add(new CommandMessages());
        handler.setLocale(new Locale("en", "US"));

        reload();
    }

    public static void reload() {
        handler.unregisterAllCommands();
        handler.register(Orphans.path(CONFIG.getStringList("command-aliases").toArray(String[]::new)).handler(new PartyCommand()));
        handler.registerBrigadier();
    }
}
