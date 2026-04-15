import java.sql.Connection;
import java.sql.DriverManager;

import core.EntityManager;
import model.Utente;

public class Main {
    public static void main(String[] args) throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        EntityManager entityManager = new EntityManager(connection);
        Utente utente = new Utente("Mario","mario.rossi@gmail.com",28);

        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS utenti (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, email TEXT, eta INTEGER)");

        entityManager.save(utente);

        System.out.println("Utente salvato!");
        Utente trovato = entityManager.findById(Utente.class, 1L);
        trovato.setEmail("nuovaMail@gmail.com");
        entityManager.update(trovato);
        System.out.println("Email aggiornata in " + trovato.getEmail());

        Utente aggiornato = entityManager.findById(Utente.class, 1L);
        System.out.println("Verifica: " + aggiornato.getEmail());   

        entityManager.delete(Utente.class, 1L);
        Utente cancellato = entityManager.findById(Utente.class, 1L);
        if (cancellato == null) {
            System.out.println("Utente cancellato correttamente!");
        } else {
            System.out.println("Qualcosa è andato storto!");
        }
    }
}
