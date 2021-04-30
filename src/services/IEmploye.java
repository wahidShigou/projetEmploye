package services;

import modeles.Employe;

import java.util.List;

public interface IEmploye {
    public String genereMat();

    public boolean addEmploye(Employe emp);

    public boolean updateEmp(Employe emp);

    public List<Employe> getAllEmp();

    public boolean exist(Employe emp);
}
