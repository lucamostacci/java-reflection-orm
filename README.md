# java-reflection-orm
Un ORM leggero scritto in Java puro che utilizza la Reflection API e Annotation custom per mappare oggetti Java su tabelle di database relazionali

------------

Ho creato un progetto Java che riesce a eseguire operazioni CRUD su un database andando ad eseguire le azioni base richieste utilizzando oggetti Java.

Prendendo in esempio il metodo save quando questo viene chiamato per prima cosa va ad accertarsi che la classe abbia una tabella altrimenti viene lanciata un'eccezione. Successivamente viene salvato il nome della tabella e scorrendo i campi della classe vengono salvati i suoi contenuti su degli ArrayList che successivamente verranno chiamati in causa andando a scrivere il comando SQL in una variabile di tipo PreparedStatement che verrà eseguita con l'executeUpdate().

Ho utilizzato Java, ReflectionAPI, Annotations e Sqlite. Le Annotation vengono applicate sia sulla classe che sui singoli campi — @Table indica a quale tabella corrisponde la classe, @Column indica a quale colonna corrisponde ogni campo, e @Id identifica la chiave primaria. La Reflection permette di leggere queste annotation a runtime, rendendo possibile generare le query SQL in modo dinamico senza conoscere in anticipo la struttura della classe.

Ho deciso di costruire questo progetto per capire come funzionano internamente framework come Hibernate o JPA, volevo capire come fa un ORM a prendere un oggetto Java e trasformarlo in una query SQL automaticamente.
