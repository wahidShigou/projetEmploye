package modeles;

import java.util.ArrayList;
import java.util.List;

public class Departement {

    private int id;
    private String libelle;
    private List<Service> listeService;

    public Departement() {
        listeService = new ArrayList<>();
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

    public List<Service> getListeService() {
        return listeService;
    }

    public void setListeService(List<Service> listeService) {
        this.listeService = listeService;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
