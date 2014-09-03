package us.corenetwork.limbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LimboIO {

	public static void saveDeath(String uuid, long deathTime, String deathMessage)
	{
		try 
		{
			Connection conn = IO.getConnection();
			
			PreparedStatement statement = conn.prepareStatement("INSERT INTO deaths (UUID, DeathTime, DeathMessage) VALUES (?,?,?)");
			
			statement.setString(1, uuid);
			statement.setLong(2, deathTime);
			statement.setString(3, deathMessage);
			
			statement.execute();
			statement.close();
			conn.commit();
		} catch (SQLException e) {
			Logs.severe("Error while saving death to the database !");
			e.printStackTrace();
		}

	}
}
