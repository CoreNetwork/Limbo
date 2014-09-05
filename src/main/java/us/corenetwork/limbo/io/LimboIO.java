package us.corenetwork.limbo.io;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.Logs;

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
	
	public static Prisoner getPrisoner(Player player)
	{
		Prisoner prisoner = null;
		
		try 
		{
			Connection conn = IO.getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT UUID, StartTime, Duration, ToRelease, SpawnedOnce, Challenge, ChallengeStartTime FROM prisoners WHERE UUID = ?");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet rs = statement.executeQuery();
			

			if(rs.next())
			{
				boolean tr = rs.getBoolean(4);
				boolean so = rs.getBoolean(5);
				prisoner = new Prisoner(rs.getString(1), rs.getLong(2), rs.getLong(3), tr, so, rs.getString(6), rs.getLong(7));
			}
			
			statement.close();
		} catch (SQLException e) {
			Logs.severe("Error while retrieveing prisoner from database !");
			e.printStackTrace();
		}
		
		return prisoner;
	}

	public static void insertPrisoner(Prisoner prisoner)
	{
		try 
		{
			Connection conn = IO.getConnection();
			
			PreparedStatement statement = conn.prepareStatement("INSERT INTO prisoners (UUID, StartTime, Duration, ToRelease, SpawnedOnce, Challenge, ChallengeStartTime) VALUES (?,?,?,?,?,?,?)");
			statement.setString(1, prisoner.uuid);
			statement.setLong(2, prisoner.startTime);
			statement.setLong(3, prisoner.duration);
			statement.setBoolean(4, prisoner.toRelease);
			statement.setBoolean(5, prisoner.spawnedOnce);
			statement.setString(6, prisoner.challenge);
			statement.setLong(7, prisoner.challengeStartTime);
			
			statement.execute();
			statement.close();
			conn.commit();
		} catch (SQLException e) {
			Logs.severe("Error while saving prisoner from database !");
			e.printStackTrace();
		}
	}

	public static void updatePrisoner(Prisoner prisoner)
	{
		try 
		{
			Connection conn = IO.getConnection();
			
			PreparedStatement statement = conn.prepareStatement("UPDATE prisoners SET StartTime = ?, "
					+ "Duration = ?, ToRelease = ?, SpawnedOnce = ?, Challenge = ?, ChallengeStartTime = ? WHERE UUID = ?");
			statement.setString(7, prisoner.uuid);
			statement.setLong(1, prisoner.startTime);
			statement.setLong(2, prisoner.duration);
			statement.setBoolean(3, prisoner.toRelease);
			statement.setBoolean(4, prisoner.spawnedOnce);
			statement.setString(5, prisoner.challenge);
			statement.setLong(6, prisoner.challengeStartTime);

			statement.execute();
			statement.close();
			conn.commit();
		} catch (SQLException e) {
			Logs.severe("Error while saving prisoner from database !");
			e.printStackTrace();
		}
	}
	
	public static void deletePrisoner(Prisoner prisoner)
	{
		try 
		{
			Connection conn = IO.getConnection();
			
			PreparedStatement statement = conn.prepareStatement("DELETE FROM prisoners WHERE UUID = ?");
			statement.setString(1, prisoner.uuid);

			statement.execute();
			statement.close();
			conn.commit();
		} catch (SQLException e) {
			Logs.severe("Error while deleting prisoner from database !");
			e.printStackTrace();
		}
	}
	
	public static void insertRecord(Record record)
	{
		try 
		{
			Connection conn = IO.getConnection();
			
			PreparedStatement statement = conn.prepareStatement("INSERT INTO records (UUID, Challenge, Duration) VALUES (?,?,?)");
			statement.setString(1, record.uuid);
			statement.setString(2, record.challenge);
			statement.setLong(3, record.duration);
			
			statement.execute();
			statement.close();
			conn.commit();
		} catch (SQLException e) {
			Logs.severe("Error while saving record to database !");
			e.printStackTrace();
		}
	}

	public static Record getBestRecord(String challenge)
	{
		Record record = null;
		
		try 
		{
			Connection conn = IO.getConnection();
			
			PreparedStatement statement = conn.prepareStatement("SELECT UUID, Challenge, Duration  FROM records WHERE Challenge = ? ORDER BY Duration ASC LIMIT 1");
			statement.setString(1, challenge);
			ResultSet rs = statement.executeQuery();
			if(rs.next())
			{
				record = new Record(rs.getString(1), rs.getString(2), rs.getLong(3));
			}
			
			statement.close();
		} catch (SQLException e) {
			Logs.severe("Error while retrieveing best record from database !");
			e.printStackTrace();
		}
		
		return record;
	}
}
