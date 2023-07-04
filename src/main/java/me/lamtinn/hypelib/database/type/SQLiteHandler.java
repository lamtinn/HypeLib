package me.lamtinn.hypelib.database.type;

import me.lamtinn.hypelib.database.DatabaseCredentials;
import me.lamtinn.hypelib.database.DatabaseType;
import me.lamtinn.hypelib.database.connector.impl.SQLConnector;
import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.DriverManager;

public abstract class SQLiteHandler extends SQLConnector {

    public SQLiteHandler(@NotNull DatabaseCredentials credentials) {
        super("SQLite", credentials);
    }

    @Override
    public void init() throws Exception {
        if (getCredentials().getType() != DatabaseType.SQLITE) {
            return;
        }
        AdventureUtils.consoleMessage("{prefix} <gray>Connecting the database with SQLite...");

        File FileSQL = new File(HypePlugin.plugin.getDataFolder(), getCredentials().getDatabase() + ".db");
        Class.forName("org.sqlite.JDBC");

        this.connection = DriverManager.getConnection("jdbc:sqlite:" + FileSQL);

        AdventureUtils.consoleMessage("{prefix} <green>Successfully connected to SQLite database!");
    }
}
