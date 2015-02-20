package us.corenetwork.limbo.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import us.corenetwork.limbo.ChallengeManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.Settings;

public class IO {
	public static YamlConfiguration config;
    private static Connection connection;

	public static void LoadSettings()
	{
		try {
			config = new YamlConfiguration();

			if (!new File(LimboPlugin.instance.getDataFolder(),"config.yml").exists()) 
				config.save(new File(LimboPlugin.instance.getDataFolder(),"config.yml"));

			config.load(new File(LimboPlugin.instance.getDataFolder(),"config.yml"));

			for (Settings s : Settings.values())
			{
				if (config.get(s.getName()) == null && s.getDefault() != null) 
					config.set(s.getName(), s.getDefault());
			}

			saveConfig();

			ChallengeManager.reloadChMap();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static void saveConfig()
	{
		try {
			config.save(new File(LimboPlugin.instance.getDataFolder(),"config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static synchronized Connection getConnection() 
    {
        if (connection == null) connection = createConnection();
        return connection;
    }
    
    public static Connection createConnection() 
    {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection ret = DriverManager.getConnection("jdbc:sqlite:" + new File(LimboPlugin.instance.getDataFolder().getPath(), "limbo.sqlite").getPath());
            ret.setAutoCommit(false);
            return ret;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized void freeConnection() 
    {
        Connection conn = getConnection();
        if(conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void PrepareDB()
    {
        Connection conn;
        Statement st = null;
        try {
            conn = IO.getConnection();
            st = conn.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS prisoners "
            		+ "(UUID STRING NOT NULL, "
            		+ "StartTime INTEGER NOT NULL, "
            		+ "Duration INTEGER, "
            		+ "ToRelease INTEGER, "
            		+ "SpawnedOnce INTEGER, "
            		+ "Challenge INTEGER, "
            		+ "ChallengeStartTime INTEGER, "
            		+ "Notify INTEGER,"
            		+ "ExpOnDeath DOUBLE DEFAULT (0),"
            		+ "PRIMARY KEY(UUID))");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS deaths "
            		+ "(UUID STRING NOT NULL, "
            		+ "DeathTime INTEGER NOT NULL, "
            		+ "DeathMessage STRING, "
            		+ "PRIMARY KEY(UUID, DeathTime))");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS records "
            		+ "(UUID STRING NOT NULL, "
            		+ "Challenge STRING NOT NULL, "
            		+ "DURATION INTEGER"
            		+ ")");
            conn.commit();
            st.close();
        } catch (SQLException e) {
            LimboPlugin.instance.getLogger().log(Level.SEVERE, "[Limbo]: Error while creating tables! - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
