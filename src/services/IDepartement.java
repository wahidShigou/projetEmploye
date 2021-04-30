package services;

import modeles.Departement;

import java.util.List;

public interface IDepartement {

    public List<Departement> findAll() throws Exception;
}
