package modeles;

import java.util.ArrayList;
import java.util.List;

public class Service {

    private int id;
    private String libelle;
    private List<Employe> employes;


    public Service() {
        employes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }


    @Override
    public String toString() {
        return libelle;
    }
}
