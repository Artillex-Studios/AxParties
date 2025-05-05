package com.artillexstudios.axparties.libraries;

import revxrsal.zapper.Dependency;
import revxrsal.zapper.relocation.Relocation;

import java.util.ArrayList;
import java.util.List;

public enum Libraries {

    MYSQL_CONNECTOR("com{}mysql:mysql-connector-j:8.0.33"),

    SQLITE_JDBC("org{}xerial:sqlite-jdbc:3.42.0.0"),

    H2_JDBC("com{}h2database:h2:2.1.214"),

    POSTGRESQL("org{}postgresql:postgresql:42.5.4"),

    HIKARICP("com{}zaxxer:HikariCP:5.1.0", relocation("com{}zaxxer{}hikari", "com.artillexstudios.axparties.libs.hikari")),

    JDBI_CORE("org{}jdbi:jdbi3-core:3.47.0", relocation("org{}jdbi{}v3", "com.artillexstudios.axparties.libs.jdbi")),

    JDBI_SQLOBJECT("org{}jdbi:jdbi3-sqlobject:3.47.0", relocation("org{}jdbi{}v3", "com.artillexstudios.axparties.libs.jdbi"));

    private final List<Relocation> relocations = new ArrayList<>();
    private final Dependency library;

    public Dependency fetchLibrary() {
        return this.library;
    }

    private static revxrsal.zapper.relocation.Relocation relocation(String from, String to) {
        return new revxrsal.zapper.relocation.Relocation(from.replace("{}", "."), to);
    }

    public List<revxrsal.zapper.relocation.Relocation> relocations() {
        return List.copyOf(this.relocations);
    }

    Libraries(String lib, revxrsal.zapper.relocation.Relocation relocation) {
        String[] split = lib.replace("{}", ".").split(":");

        this.library = new Dependency(split[0], split[1], split[2]);
        this.relocations.add(relocation);
    }

    Libraries(String lib) {
        String[] split = lib.replace("{}", ".").split(":");

        this.library = new Dependency(split[0], split[1], split[2]);
    }
}
