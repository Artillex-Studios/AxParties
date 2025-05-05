package com.artillexstudios.axparties.commands.subcommands;

import com.artillexstudios.axapi.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.artillexstudios.axparties.AxParties.CONFIG;
import static com.artillexstudios.axparties.AxParties.LANG;
import static com.artillexstudios.axparties.AxParties.MESSAGEUTILS;

public enum Reload {
    INSTANCE;

    public void execute(@NotNull CommandSender sender) {
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#0099FF[AxParties] &#77DDFFReloading configuration..."));
        if (!CONFIG.reload()) {
            MESSAGEUTILS.sendLang(sender, "reload.failed", Map.of("%file%", "config.yml"));
            return;
        }
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#0099FF╠ &#77DDFFReloaded &fconfig.yml&#77DDFF!"));

        if (!LANG.reload()) {
            MESSAGEUTILS.sendLang(sender, "reload.failed", Map.of("%file%", "lang.yml"));
            return;
        }
        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#0099FF╠ &#77DDFFReloaded &flang.yml&#77DDFF!"));

        Bukkit.getConsoleSender().sendMessage(StringUtils.formatToString("&#0099FF╚ &#0099FFSuccessful reload!"));
        MESSAGEUTILS.sendLang(sender, "reload.success");
    }
}
