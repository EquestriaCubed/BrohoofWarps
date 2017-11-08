package com.brohoof.brohoofwarps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class Data {
	/**
	 * Our {@link java.sql.Connection}.
	 */
	private Connection connection;
	/**
	 * Our copy of the main {@link BrohoofWarpsPlugin}
	 */
	private BrohoofWarpsPlugin p;
	/**
	 * A copy of our settings.
	 */
	private Settings s;

	/**
	 * gets all of our needed variables, and creates the tables.
	 */
	public Data(BrohoofWarpsPlugin pl, Settings st) {
		p = pl;
		s = st;
		createTables();
	}

	public void addSign(UUID uniqueId, String warpName) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Closes this database connection.
	 */
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			error(e);
		}
	}

	/**
	 * Executes the query to create the table.
	 * 
	 * @param pQuery
	 *            the create table query
	 */
	private void createTable(String pQuery) {
		try {
			executeQuery(pQuery);
		} catch (final SQLException e) {
			error(e);
		}
	}

	/**
	 * Checks to see if the tables exists and if necessary, create them.
	 */
	private void createTables() {
		// Generate the information about the various tables
		final String warps = "CREATE TABLE " + s.dbPrefix + "warp (warpid INT NOT NULL AUTO_INCREMENT, warpName VARCHAR(30) NOT NULL, ownerUUID CHAR(36) NOT NULL, ownerName VARCHAR(16) NOT NULL, world VARCHAR(50) NOT NULL, x DOUBLE PRECISION NOT NULL, y DOUBLE PRECISION NOT NULL, z DOUBLE PRECISION NOT NULL, pitch FLOAT NOT NULL, yaw FLOAT NOT NULL, access INT NOT NULL, CONSTRAINT warp_pk PRIMARY KEY(warpid));";
		final String invites = "CREATE TABLE " + s.dbPrefix + "invites ( inviteID INT NOT NULL AUTO_INCREMENT, warpid INT NOT NULL, inviteUUID CHAR(36) NOT NULL, CONSTRAINT invite_pk PRIMARY KEY(inviteID), CONSTRAINT invite_fk FOREIGN KEY(warpid) REFERENCES " + s.dbPrefix + "warp(warpid));";
		// Generate the database tables
		if (!tableExists(s.dbPrefix + "warp"))
			createTable(warps);
		if (!tableExists(s.dbPrefix + "invites"))
			createTable(invites);
	}

	/**
	 * Method to check and print if errors need to be printed
	 * 
	 * @param e
	 *            the error
	 */
	private void error(Throwable e) {
		if (s.stackTraces) {
			e.printStackTrace();
			return;
		}
		if (e instanceof SQLException) {
			p.getLogger().severe("SQLException: " + e.getMessage());
			return;
		}
		p.getLogger().severe("Unhandled Exception " + e.getCause() + ": " + e.getMessage());
		e.printStackTrace();
	}

	/**
	 * Executes the specified query
	 * 
	 * @see java.sql.Statement#executeUpdate
	 * @param query
	 *            the SQL query to execute
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
	 * Returns a {@link ResultSet} of the query
	 * 
	 * @see java.sql.Statement#executeQuery
	 * @param query
	 *            the SQL query to execute
	 * 
	 */
	private ResultSet getResultSet(final String query) throws SQLException {
		if (connection == null || connection.isClosed()) {
			final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase);
			connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
			p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
		}
		return connection.createStatement().executeQuery(query);
	}

	public WarpSign[] getSigns(String warpName, UUID owner) {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<Warp> getWarp(String warpName, String ownerName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<Warp>[] getWarps(String warpName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Checks to see if the table exists
	 * 
	 * @param pTable
	 *            the table name.
	 * @return true if it does, false if it does not.
	 */
	private boolean tableExists(String pTable) {
		try {
			return getResultSet("SELECT * FROM " + pTable) != null;
		} catch (final SQLException e) {
			if (e.getMessage().equals("Table \"" + s.dbDatabase + "." + s.dbPrefix + pTable + "\" doesn't exist") || e.getMessage().equals("Table '" + s.dbDatabase + "." + s.dbPrefix + pTable + "' doesn't exist"))
				return false;
			error(e);
		}
		return false;
	}

	public void updateOwnerName(UUID ownerUUID, String newOwnerName) {
		// TODO Auto-generated method stub
	}

    public void updateLocation(Warp warpToUpdate, double x, double y, double z, float pitch, float yaw) {
        // TODO Auto-generated method stub
        
    }
}
