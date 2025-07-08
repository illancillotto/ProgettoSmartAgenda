package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;
import javax.servlet.ServletContext;

public class DBConnection {
    private static String url;
    private static String user;
    private static String password;
    private static boolean initialized = false;

    public static void init(ServletContext context) {
        if (initialized) return; // già inizializzato
        try {
            Properties props = new Properties();
            InputStream input = context.getResourceAsStream("/WEB-INF/db.properties");
            if (input == null) throw new RuntimeException("Impossibile trovare db.properties!");
            props.load(input);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Errore nella configurazione DB: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws Exception {
        if (!initialized) throw new RuntimeException("DB non inizializzato!");
        return DriverManager.getConnection(url, user, password);
    }
}
