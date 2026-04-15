import java.sql.Connection;
import java.sql.DriverManager;

import core.EntityManager;
import model.Utente;

public class Main {
    public static void main(String[] args) throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        EntityManager entityManager = new EntityManager(connection);
        Utente utente = new Utente("Luca","luca.mostacci@gmail.com",30);

        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS utenti (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, email TEXT, eta INTEGER)");

        entityManager.save(utente);

        System.out.println("Utente salvato!");
        Utente trovato = entityManager.findById(Utente.class, 1L);
        System.out.println("Trovato: " + trovato.getNome() + " - " + trovato.getEmail());
    }
}
