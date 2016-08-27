package com.brohoof.brohoofwarps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Data {
    /**
     * Our copy of the main {@link BrohoofWarpsPlugin}
     */
    private static BrohoofWarpsPlugin p;
    /**
     * Our {@link java.sql.Connection}.
     */
    private static Connection connection;
    /**
     * A copy of our settings.
     */
    private static Settings s;
    /**
     * gets all of our needed variables, and creates the tables.
     */
    static {
        p = BrohoofWarpsPlugin.getPlugin();
        // Possible NullPointerException? if none of the variables in the main plugin are not initalized yet, it ay cause problems here.
        s = p.getSettings();
        createTables();
    }

    /**
     * Checks to see if the tables exists and if necessary, create them.
     */
    private static void createTables() {
        // Generate the information about the various tables
        final String warps = "CREATE TABLE " + s.dbPrefix + "warp (warpid INT NOT NULL AUTO_INCREMENT PRIMARY KEY, warpName VARCHAR(30) ownerUUID VARCHAR(36), ownerName VARCHAR(16), world VARCHAR(2000), x DOUBLE PRECISION, y DOUBLE PRECISION, z DOUBLE PRECISION, pitch FLOAT, yaw FLOAT,  access INT);";
        final String invites = "CREATE TABLE " + s.dbPrefix + "invites (warpinviteid INT NOT NULL AUTO_INCREMENT PRIMARY KEY, warpid INT, inviteUUID VARCHAR(36));";
        // Generate the database tables
        if (!tableExists(s.dbPrefix + "warp"))
            createTable(warps);
        if (!tableExists(s.dbPrefix + "invites"))
            createTable(invites);
    }

    /**
     * Executes the query to create the table.
     * 
     * @param pQuery
     *            the create table query
     */
    private static void createTable(String pQuery) {
        try {
            executeQuery(pQuery);
        } catch (final SQLException e) {
            error(e);
        }
    }

    /**
     * Checks to see if the table exists
     * 
     * @param pTable
     *            the table name.
     * @return true if it does, false if it does not.
     */
    private static boolean tableExists(String pTable) {
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
     * Method to check and print if errors need to be printed
     * 
     * @param e
     *            the error
     */
    private static void error(Throwable e) {
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
     * Returns a {@link ResultSet} of the query
     * 
     * @see java.sql.Statement#executeQuery
     * @param query
     *            the SQL query to execute
     * 
     */
    private static ResultSet getResultSet(final String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase);
            connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
            p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
        }
        return connection.createStatement().executeQuery(query);
    }

    /**
     * Executes the specified query
     * 
     * @see java.sql.Statement#executeUpdate
     * @param query
     *            the SQL query to execute
     */
    private static int executeQuery(final String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase);
            connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
            p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
        }
        return connection.createStatement().executeUpdate(query);
    }

    /**
     * Closes this database connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            error(e);
        }
    }
}
