package model;
import annotation.*;


@Table(name="utenti")
public class Utente {
    @Id
    @Column(name="id")
    private Long id;
    @Column(name="nome")
    private String nome;
    @Column(name="email")
    private String email;
    @Column(name="eta")
    private int eta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }


    public Utente(String nome, String email, int eta){
        this.nome=nome;
        this.email=email;
        this.eta=eta;
    }

    public Utente(){

    }
}
