package ar.com.unpaz.organizerddd.infrastructure.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class DbConection {
	private static Connection con = null;

	private DbConection() {
		throw new IllegalStateException("Utility class");
	}

	public static synchronized Connection getConection() {
		if (con == null) {
			try {
				String driver = "org.hsqldb.jdbc.JDBCDriver";
				Class.forName(driver);
				con = createConnection();
				System.out.println("Conectado a la BD");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return con;
	}

	private static Connection createConnection() throws InvalidPropertiesFormatException, IOException, Exception {
		final String path = "resources/db-properties.xml";
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(path);
		prop.loadFromXML(fis);
		final String url = prop.getProperty("url");
		final String user = prop.getProperty("user");
		final String pass = prop.getProperty("pass");
		return DriverManager.getConnection(url, user, pass);
	}

}
