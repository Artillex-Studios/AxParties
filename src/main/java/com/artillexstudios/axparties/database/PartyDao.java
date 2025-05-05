package com.artillexstudios.axparties.database;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface PartyDao {

    @SqlUpdate("INSERT INTO axparties_parties (name, owner, created) VALUES (?, ?, ?)")
    void insert(String name, int owner, long created);

    @SqlUpdate("DELETE FROM axparties_parties WHERE name = ?")
    void delete(String name);
}
