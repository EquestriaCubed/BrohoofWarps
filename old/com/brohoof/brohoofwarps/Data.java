package com.brohoof.brohoofwarps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Data {
    private Connection connection;
    private final BrohoofWarpsPlugin p;
    private final Settings s;

    public Data(final BrohoofWarpsPlugin p, final Settings s) {
        this.s = s;
        this.p = p;
        createTables();
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            error(e);
        }
    }

    /** Creates a new table in the database */
    private boolean createTable(final String pQuery) {
        try {
            executeQuery(pQuery);
            return true;
        } catch (final SQLException e) {
            error(e);
            return false;
        }
    }

    private final void createTables() {
        // Generate the information about the various tables0
        // Boolean 0 = false, 1 = true.
        final String warps = "CREATE TABLE " + s.dbPrefix + "warp (warpid INT NOT NULL AUTO_INCREMENT PRIMARY KEY, warpName VARCHAR(30) ownerUUID VARCHAR(36), ownerName VARCHAR(16), world VARCHAR(2000), x DOUBLE PRECISION, y DOUBLE PRECISION, z DOUBLE PRECISION, pitch FLOAT, yaw FLOAT,  access INT);";
        final String invites = "CREATE TABLE " + s.dbPrefix + "invites (warpinviteid INT NOT NULL AUTO_INCREMENT PRIMARY KEY, warpid INT, inviteUUID VARCHAR(36));";
        // Generate the database tables
        if (!tableExists(s.dbPrefix + "warp"))
            createTable(warps);
        if (!tableExists(s.dbPrefix + "invites"))
            createTable(invites);
    }

    private boolean tableExists(final String pTable) {
        try {
            return getResultSet("SELECT * FROM " + pTable) != null;
        } catch (final SQLException e) {
            if (e.getMessage().equals("Table \"" + s.dbDatabase + "." + s.dbPrefix + pTable + "\" doesn't exist") || e.getMessage().equals("Table '" + s.dbDatabase + "." + s.dbPrefix + pTable + "' doesn't exist"))
                return false;
            error(e);
        }
        return false;
    }

    /**
     * Method used to handle errors
     *
     * @param e
     *            Exception
     */
    public void error(final Exception e) {
        if (s.stackTraces) {
            e.printStackTrace();
            return;
        }
        if (e instanceof SQLException) {
            p.getLogger().severe("SQLException: " + e.getMessage());
            return;
        }
        if (e instanceof IllegalArgumentException)
            // It was probably someone not putting in a valid UUID, so we can ignore.
            // p.getLogger().severe("IllegalArgumentException: " + e.getMessage());
            return;
        if (e instanceof ClassCastException)
            // It was probably someone trying to do something with an offline player.
            // p.getLogger().severe("ClassCastException: " + e.getMessage());
            return;
        p.getLogger().severe("Unhandled Exception " + e.getCause() + ": " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Executes the given SQL statement, which may be an <code>INSERT</code>, <code>UPDATE</code>, or <code>DELETE</code> statement or an SQL statement that returns nothing, such as an SQL DDL statement.
     * <p>
     *
     * @param query
     *            an SQL Data Manipulation Language (DML) statement, such as INSERT, UPDATE or DELETE; or an SQL statement that returns nothing, such as a DDL statement.
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws SQLException
     *             if a database access error occurs, this method is called on a closed <code>Statement</code>, the given SQL statement produces a <code>ResultSet</code> object, the method is called on a <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    private int executeQuery(final String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase);
            connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
            p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
        }
        return connection.createStatement().executeUpdate(query);
    }

    /**
     * Private method for getting an SQL connection, then submitting a query. This method throws an SQL Exception to allow another method to handle it.
     *
     * @param query
     *            an SQL statement to be sent to the database, typically a static SQL <code>SELECT</code> statement
     * @return a <code>ResultSet</code> object that contains the data produced by the given query; never <code>null</code>
     * @throws SQLException
     *             if a database access error occurs, this method is called on a closed <code>Statement</code>, the given SQL statement produces a <code>ResultSet</code> object, the method is called on a <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    private ResultSet getResultSet(final String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase);
            connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
            p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
        }
        return connection.createStatement().executeQuery(query);
    }

    public void forceConnectionRefresh() {
        try {
            if (connection == null || connection.isClosed()) {
                final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase);
                connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
                p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
            } else {
                connection.close();
                final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase);
                connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
                p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
            }
        } catch (final SQLException e) {
            error(e);
        }
    }

    public Optional<Warp> getWarp(String name, String owner) {
        ResultSet rs;
        try {
            rs = getResultSet("SELECT * FROM " + s.dbPrefix + "warp WHERE warpName = \"" + name + "\" AND WHERE ownerName = \"" + owner + "\";");
            if (rs.next()) {
                // final String warps = "CREATE TABLE " + s.dbPrefix + "warp (warpid "
                // + "INT NOT NULL AUTO_INCREMENT " + "PRIMARY KEY, warpName VARCHAR(30) "
                // + "ownerUUID VARCHAR(36), " + "ownerName VARCHAR(16), " +
                // "world VARCHAR(2000), x INT, " + "INT, z INT, access INT);";
                String warp = rs.getString("warpName");
                UUID ownerUUID = UUID.fromString(rs.getString("ownerUUID"));
                String ownerName = rs.getString("ownerName");
                Optional<World> world = Optional.<World> ofNullable((Bukkit.getWorld(rs.getString("world"))));
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                float pitch = rs.getFloat("pitch");
                float yaw = rs.getFloat("yaw");
                ArrayList<Invite> invites = new ArrayList<Invite>(0);
                ResultSet inrs = getResultSet("SELECT * FROM " + s.dbPrefix + "invites WHERE warpid = " + rs.getInt("warpid"));
                while (inrs.next())
                    invites.add(new Invite(UUID.fromString(rs.getString("inviteUUID"))));
                return Optional.<Warp> of(new Warp(warp, ownerName, ownerUUID, invites.toArray(new Invite[0]), world, x, y, z, yaw, pitch));
            }
            return Optional.<Warp> empty();
        } catch (SQLException e) {
            error(e);
            return Optional.<Warp> empty();
        }
    }

    @SuppressWarnings("deprecation")
    public Warp[] getWarps(String warp) {
        ArrayList<Warp> warps = new ArrayList<Warp>(0);
        Warp[] result;
        Bukkit.getScheduler().scheduleAsyncDelayedTask(p, () -> {
            ResultSet rs;
            try {
                rs = getResultSet("SELECT * FROM " + s.dbPrefix + "warp WHERE warpName = \"" + warp + "\";");
                while (rs.next()) {
                    String warpN = rs.getString("warpName");
                    UUID ownerUUID = UUID.fromString(rs.getString("ownerUUID"));
                    String ownerName = rs.getString("ownerName");
                    Optional<World> world = Optional.<World> ofNullable((Bukkit.getWorld(rs.getString("world"))));
                    double x = rs.getDouble("x");
                    double y = rs.getDouble("y");
                    double z = rs.getDouble("z");
                    float pitch = rs.getFloat("pitch");
                    float yaw = rs.getFloat("yaw");
                    ArrayList<Invite> invites = new ArrayList<Invite>(0);
                    ResultSet inrs = getResultSet("SELECT * FROM " + s.dbPrefix + "invites WHERE warpid = " + rs.getInt("warpid"));
                    while (inrs.next())
                        invites.add(new Invite(UUID.fromString(rs.getString("inviteUUID"))));
                    warps.add(new Warp(warpN, ownerName, ownerUUID, invites.toArray(new Invite[0]), world, x, y, z, yaw, pitch));
                }
                result = warps.toArray(new Warp[0]);
            } catch (SQLException e) {
                error(e);
                result = warps.toArray(new Warp[0]);
            }
        });
        return result;
    }

    public int update(UUID player, String newName) {
        try {
            return executeQuery("UPDATE " + s.dbPrefix + "warp SET ownerName = \"" + newName + "\" WHERE ownerUUID = \"" + player.toString() + "\";");
        } catch (SQLException e) {
            error(e);
            return -1;
        }
    }
}
