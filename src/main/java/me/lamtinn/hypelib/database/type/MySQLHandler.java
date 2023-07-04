package me.lamtinn.hypelib.database.type;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.lamtinn.hypelib.database.DatabaseCredentials;
import me.lamtinn.hypelib.database.DatabaseType;
import me.lamtinn.hypelib.database.connector.impl.SQLConnector;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class MySQLHandler extends SQLConnector {

    private HikariDataSource dataSource;

    public MySQLHandler(@NotNull final DatabaseCredentials credentials) {
        super("MySQL", credentials);
    }

    @Override
    public void init() throws Exception {
        if (getCredentials().getType() != DatabaseType.MYSQL) {
            return;
        }
        final HikariConfig config = new HikariConfig();

        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", getCredentials().getHost(), getCredentials().getPort(), getCredentials().getDatabase()));

        config.setUsername(getCredentials().getUsername());
        config.setPassword(getCredentials().getPassword());

        config.setMaximumPoolSize(getCredentials().getMaxConnections());
        config.setMinimumIdle(getCredentials().getMinConnections());

        config.setMaxLifetime(TimeUnit.MINUTES.toMinutes(30));
        config.setConnectionTimeout(TimeUnit.SECONDS.toMillis(10));
        config.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(10));

        Map<String, String> properties = ImmutableMap.<String, String>builder()
                .put("useUnicode", "true").put("characterEncoding", "utf8")
                .put("cachePrepStmts", "true").put("prepStmtCacheSize", "250")
                .put("prepStmtCacheSqlLimit", "2048").put("useServerPrepStmts", "true")
                .put("useLocalSessionState", "true").put("rewriteBatchedStatements", "true")
                .put("cacheResultSetMetadata", "true").put("cacheServerConfiguration", "true")
                .put("elideSetAutoCommits", "true").put("maintainTimeStats", "false")
                .put("alwaysSendSetIsolation", "false").put("cacheCallableStmts", "true")
                .put("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30))).build();
        for (Map.Entry<String, String> property : properties.entrySet()) {
            config.addDataSourceProperty(property.getKey(), property.getValue());
        }

        AdventureUtils.consoleMessage("{prefix} <gray>Connecting the database with MySQL...");

        this.dataSource = new HikariDataSource(config);
        this.connection = dataSource.getConnection();

        AdventureUtils.consoleMessage("{prefix} <green>Successfully connected to MySQL database!");
    }

    public @NotNull HikariDataSource getDataSource() {
        return this.dataSource;
    }
}
