package core;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
