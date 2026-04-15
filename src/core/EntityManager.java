package core;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import annotation.*;

public class EntityManager {
    private Connection connection;

    public EntityManager(Connection connection){
        this.connection=connection;
    }

    public <T> void save(T value) throws Exception{
        Class<?> c = value.getClass();
        if(!c.isAnnotationPresent(Table.class)){
            throw new Exception("Classe senza @Table");
        }
        String nomeTabella=c.getAnnotation(Table.class).name();

        List<String> colonne = new ArrayList<>();
        List<Object> valori = new ArrayList<>();
        Field[] campi = c.getDeclaredFields();

        for (Field campo : campi){
            campo.setAccessible(true);
            if (campo.isAnnotationPresent(Id.class)){
                continue;
            }
            if (campo.isAnnotationPresent(Column.class)){
                String nomeColonna=campo.getAnnotation(Column.class).name();
                colonne.add(nomeColonna);
                valori.add(campo.get(value));
            }
        }
        String nomeColonne = String.join(",  ", colonne);
        List<String> numeroElementi = Collections.nCopies(colonne.size(), "?");
        String valoriStringaUniti = String.join(", ", numeroElementi);
        String sqlFinale = String.format("INSERT INTO %s (%s) VALUES (%s)", nomeTabella, nomeColonne, valoriStringaUniti);
        PreparedStatement ps = connection.prepareStatement(sqlFinale);
        for (int i = 0; i < valori.size(); i++) {
            ps.setObject(i+1, valori.get(i));
        }
        ps.executeUpdate();
    }

    public <T> T findById(Class<T> classe, Long id) throws Exception{
        String nomeColonna = "";
        if(!classe.isAnnotationPresent(Table.class)){
            throw new Exception("Classe senza @Table");
        }
        String nomeTabella = classe.getAnnotation(Table.class).name();
        Field[] campi = classe.getDeclaredFields();

        for (Field campo:campi){
            campo.setAccessible(true);
            if (campo.isAnnotationPresent(Id.class)){
                nomeColonna = campo.getAnnotation(Column.class).name();
                break;
            }
        }
        String sqlFinale = String.format("SELECT * FROM %s WHERE %s = ?", nomeTabella, nomeColonna);
        PreparedStatement ps = connection.prepareStatement(sqlFinale);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()){
            return null;
        }
        T istanza = classe.getDeclaredConstructor().newInstance();
        String nomeColonnaRisultato = "";
        for (Field campo : campi){
            campo.setAccessible(true);
            if (campo.isAnnotationPresent(Column.class)){
                nomeColonnaRisultato = campo.getAnnotation(Column.class).name();
                Object valore = rs.getObject(nomeColonnaRisultato);
                campo.set(istanza, valore);
            }
        }
        return istanza;
    }

    public <T> void update(T value) throws Exception{
        Class<?> c = value.getClass();
        if(!c.isAnnotationPresent(Table.class)){
            throw new Exception("Classe senza @Table");
        }
        String nomeTabella=c.getAnnotation(Table.class).name();
        List<String> setClauses = new ArrayList<>();
        List<Object> valori = new ArrayList<>();
        String colonnaId = "";
        Object valoreId = null;
        Field[] campi = c.getDeclaredFields();

        for (Field campo:campi){
            campo.setAccessible(true);
            if (campo.isAnnotationPresent(Id.class)){
                colonnaId=campo.getAnnotation(Column.class).name();
                valoreId=campo.get(value);
                continue;
            }
            if (campo.isAnnotationPresent(Column.class)){
                setClauses.add(campo.getAnnotation(Column.class).name() +  " = ?");
                valori.add(campo.get(value));
            }
        }
    String nomeColonne = String.join(",  ", setClauses); 
    String sqlFinale = String.format("UPDATE %s SET %s WHERE %s = ?", nomeTabella, nomeColonne, colonnaId);
    PreparedStatement ps = connection.prepareStatement(sqlFinale);
    for (int i = 0; i < valori.size(); i++) {
            ps.setObject(i+1, valori.get(i));
        }
    ps.setObject(valori.size() + 1, valoreId);    
    ps.executeUpdate();
    }
}
