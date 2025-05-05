package com.artillexstudios.axparties.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;

import static com.artillexstudios.axparties.AxParties.DATABASE;

public class H2 implements Database {
    private final HikariConfig hConfig = new HikariConfig();
    private HikariDataSource dataSource;
    private Jdbi jdbi;

    @Override
    public void setup() {
        hConfig.setPoolName("axparties-pool");

        hConfig.setMaximumPoolSize(DATABASE.getInt("database.pool.maximum-pool-size"));
        hConfig.setMinimumIdle(DATABASE.getInt("database.pool.minimum-idle"));
        hConfig.setMaxLifetime(DATABASE.getInt("database.pool.maximum-lifetime"));
        hConfig.setKeepaliveTime(DATABASE.getInt("database.pool.keepalive-time"));
        hConfig.setConnectionTimeout(DATABASE.getInt("database.pool.connection-timeout"));

        hConfig.setDriverClassName("org.h2.Driver");
        hConfig.setJdbcUrl("jdbc:h2://" + DATABASE.getString("database.address") + ":" + DATABASE.getString("database.port") + "/" + DATABASE.getString("database.database"));
        hConfig.addDataSourceProperty("user", DATABASE.getString("database.username"));
        hConfig.addDataSourceProperty("password", DATABASE.getString("database.password"));

        dataSource = new HikariDataSource(hConfig);
        jdbi = Jdbi.create(dataSource);

        jdbi.useHandle(handle -> {
            handle.execute("""
                    CREATE TABLE IF NOT EXISTS axparties_users (
                    	id INT NOT NULL AUTO_INCREMENT,
                    	uuid VARCHAR(36) NOT NULL,
                    	party_id INT DEFAULT NULL,
                    	PRIMARY KEY (id),
                        UNIQUE (uuid)
                    );
                    """
            );
            handle.execute("""
                     CREATE TABLE axparties_parties (
                         id INT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(256) NOT NULL,
                         owner INT NOT NULL,
                         created BIGINT NOT NULL,
                         PRIMARY KEY (id),
                         UNIQUE (name)
                     );
                    """
            );
        });
    }

    @Override
    public void disable() {
        try {
            dataSource.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
