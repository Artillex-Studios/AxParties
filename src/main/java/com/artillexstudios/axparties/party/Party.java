package com.artillexstudios.axparties.party;

import com.artillexstudios.axparties.invite.Invite;
import com.artillexstudios.axparties.invite.InviteManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.artillexstudios.axparties.AxParties.CONFIG;

public class Party {
    private String name;
    private UUID owner;
    private final Set<OfflinePlayer> members = new HashSet<>();
    private long created;

    public Party(String name, OfflinePlayer owner, long created) {
        this.name = name;
        this.owner = owner.getUniqueId();
        this.created = created;
        members.add(owner);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(owner);
    }

    public void setOwner(OfflinePlayer owner) {
        this.owner = owner.getUniqueId();
    }

    public Set<OfflinePlayer> getMembers() {
        return members.stream().map(player -> Bukkit.getOfflinePlayer(player.getUniqueId())).collect(Collectors.toUnmodifiableSet());
    }

    public void addMember(Player player) {
        members.add(player);
    }

    public void removeMember(Player player) {
        members.remove(player);
    }

    public List<Player> getOnlineMembers() {
        List<Player> players = new ArrayList<>();
        for (OfflinePlayer offlinePlayer : members) {
            Player pl;
            if ((pl = Bukkit.getPlayer(offlinePlayer.getUniqueId())) == null) continue;
            players.add(pl);
        }
        return players;
    }

    public List<OfflinePlayer> getOfflineMembers() {
        List<OfflinePlayer> players = new ArrayList<>();
        for (OfflinePlayer offlinePlayer : members) {
            if (Bukkit.getPlayer(offlinePlayer.getUniqueId()) != null) continue;
            players.add(offlinePlayer);
        }
        return players;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getMemberLimit() {
        int serverLimit = CONFIG.getInt("member-limit", 8);

        int am = 0;
        final Player player = Bukkit.getPlayer(owner);
        if (player != null) {
            for (PermissionAttachmentInfo effectivePermission : player.getEffectivePermissions()) {
                if (!effectivePermission.getPermission().startsWith("axparties.limit.")) continue;

                int value = Integer.parseInt(effectivePermission.getPermission().substring(effectivePermission.getPermission().lastIndexOf('.') + 1));
                if (value > am) {
                    am = value;
                }
            }
        }

        return Math.max(serverLimit, am);
    }

    public void clearInvites() {
        for (Invite invite : InviteManager.getInvitesOf(this)) {
            InviteManager.getInvites().remove(invite);
        }
    }
}
