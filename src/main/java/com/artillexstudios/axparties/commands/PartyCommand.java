package com.artillexstudios.axparties.commands;

import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axparties.commands.annotations.NoParty;
import com.artillexstudios.axparties.commands.annotations.PartyMember;
import com.artillexstudios.axparties.commands.subcommands.Accept;
import com.artillexstudios.axparties.commands.subcommands.Chat;
import com.artillexstudios.axparties.commands.subcommands.Create;
import com.artillexstudios.axparties.commands.subcommands.Deny;
import com.artillexstudios.axparties.commands.subcommands.Disband;
import com.artillexstudios.axparties.commands.subcommands.Kick;
import com.artillexstudios.axparties.commands.subcommands.Leave;
import com.artillexstudios.axparties.commands.subcommands.List;
import com.artillexstudios.axparties.commands.subcommands.Reload;
import com.artillexstudios.axparties.commands.subcommands.Rename;
import com.artillexstudios.axparties.commands.subcommands.Transfer;
import com.artillexstudios.axparties.invite.Invite;
import com.artillexstudios.axparties.party.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.orphan.OrphanCommand;

import static com.artillexstudios.axparties.AxParties.LANG;

@CommandPermission("axparties.use")
public class PartyCommand implements OrphanCommand {

    @DefaultFor({"~", "~ help"})
    public void help(@NotNull CommandSender sender) {
        for (String m : LANG.getStringList("help")) {
            sender.sendMessage(StringUtils.formatToString(m));
        }
    }

    @CommandPermission("axparties.reload")
    @Subcommand("reload")
    public void reload(CommandSender sender) {
        Reload.INSTANCE.execute(sender);
    }

    @CommandPermission("axparties.create")
    @Subcommand("create")
    public void create(Player sender, @Optional String name) {
        Create.INSTANCE.execute(sender, name);
    }

    @CommandPermission("axparties.disband")
    @Subcommand("disband")
    public void disband(Player sender) {
        Disband.INSTANCE.execute(sender);
    }

    @CommandPermission("axparties.invite")
    @Subcommand("invite")
    public void invite(Player sender, @NoParty Player player) {
        com.artillexstudios.axparties.commands.subcommands.Invite.INSTANCE.execute(sender, player);
    }

    @CommandPermission("axparties.kick")
    @Subcommand("kick")
    public void kick(Player sender, @PartyMember Player player) {
        Kick.INSTANCE.execute(sender, player);
    }

    @CommandPermission("axparties.accept")
    @Subcommand("accept")
    public void accept(Player sender, @Optional Invite invite) {
        Accept.INSTANCE.execute(sender, invite);
    }

    @CommandPermission("axparties.deny")
    @Subcommand("deny")
    public void deny(Player sender, @Optional Invite invite) {
        Deny.INSTANCE.execute(sender, invite);
    }

    @CommandPermission("axparties.leave")
    @Subcommand("leave")
    public void leave(Player sender) {
        Leave.INSTANCE.execute(sender);
    }

    @CommandPermission("axparties.list")
    @Subcommand("list")
    public void list(Player sender, @Optional @CommandPermission("axparties.list.other") Party party) {
        List.INSTANCE.execute(sender, party);
    }

    @CommandPermission("axparties.chat")
    @Subcommand("chat")
    public void chat(Player sender, @Optional String message) {
        Chat.INSTANCE.execute(sender, message);
    }

    @CommandPermission("axparties.rename")
    @Subcommand("rename")
    public void rename(Player sender, String name) {
        Rename.INSTANCE.execute(sender, name);
    }

    @CommandPermission("axparties.transfer")
    @Subcommand("transfer")
    public void transfer(Player sender, Player player) {
        Transfer.INSTANCE.execute(sender, player);
    }
}
