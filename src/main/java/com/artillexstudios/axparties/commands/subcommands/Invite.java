package com.artillexstudios.axparties.commands.subcommands;

import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axparties.invite.InviteManager;
import com.artillexstudios.axparties.party.Party;
import com.artillexstudios.axparties.party.PartyManager;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

import static com.artillexstudios.axparties.AxParties.CONFIG;
import static com.artillexstudios.axparties.AxParties.LANG;
import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public enum Invite {
    INSTANCE;

    public void execute(Player sender, Player player) {
        if (sender.equals(player)) {
            MESSAGEUTILS.sendLang(sender, "errors.self");
            return;
        }

        Optional<Party> optParty = PartyManager.getPartyOf(sender);
        if (optParty.isEmpty()) {
            MESSAGEUTILS.sendLang(sender, "errors.not-in-party");
            return;
        }

        Optional<Party> optParty2 = PartyManager.getPartyOf(player);
        if (optParty2.isPresent()) {
            MESSAGEUTILS.sendLang(sender, "errors.already-has-party");
            return;
        }

        Party party = optParty.get();
        if (!party.getOwner().equals(sender)) {
            MESSAGEUTILS.sendLang(sender, "errors.not-the-leader");
            return;
        }

        for (com.artillexstudios.axparties.invite.Invite invite : InviteManager.getInvitesOf(party)) {
            if (invite.invited().equals(player)) {
                MESSAGEUTILS.sendLang(sender, "errors.already-invited");
                return;
            }
        }

        for (Player member : party.getOnlineMembers()) {
            MESSAGEUTILS.sendLang(member, "invite.broadcast", Map.of("%player%", player.getName()));
        }
        MESSAGEUTILS.sendLang(sender, "invite.inviter", Map.of("%player%", player.getName()));

        Map<String, String> rp = Map.of("%party%", party.getName());
        ServerPlayerWrapper wrapper = ServerPlayerWrapper.wrap(player);
        wrapper.message(StringUtils.format(CONFIG.getString("prefix") + LANG.getString("invite.invited.info"), rp));
        wrapper.message(StringUtils.format(LANG.getString("invite.invited.accept.message"), rp)
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, StringUtils.format(LANG.getString("invite.invited.accept.hover"), rp)))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + party.getName())));
        wrapper.message(StringUtils.format(LANG.getString("invite.invited.deny.message"), rp)
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, StringUtils.format(LANG.getString("invite.invited.deny.hover"), rp)))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + party.getName())));

        com.artillexstudios.axparties.invite.Invite invite = new com.artillexstudios.axparties.invite.Invite(party, player, System.currentTimeMillis());
        InviteManager.getInvites().add(invite);
    }
}
