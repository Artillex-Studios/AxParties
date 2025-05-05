package com.artillexstudios.axparties.database;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.UUID;

public interface UserDao {

    @SqlUpdate("INSERT INTO axparties_users (uuid) VALUES (?)")
    void insert(UUID uuid);

}
