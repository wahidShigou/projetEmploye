package services;

import modeles.Departement;
import modeles.Employe;
import modeles.Service;
import utilitaires.DatabaseHelper;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmployeDao implements IEmploye {
    @Override
    public String genereMat() {
        DatabaseHelper db = DatabaseHelper.getInstance();
        int id = db.lastIdFor("employe");
         String mat = "MAT" + new DecimalFormat("0000").format(id);
         return mat;
    }

    @Override
    public boolean addEmploye(Employe emp) {
        DatabaseHelper db = DatabaseHelper.getInstance();
        String sql = "INSERT INTO employe VALUES ("+db.lastIdFor("employe")+", ?, ?, ?, ?, "+emp.getSalaire()+","+emp.getService().getId()+") ";
        try
        {
            db.prepareStatement(sql);
            Object[] tab = {emp.getMatricule(), emp.getNom(), emp.getPrenom(), emp.getDateNaissance()};
            db.bindParams(tab);
            db.executeUpdate();
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public List<Employe> getAllEmp()
    {
        String sql = "SELECT * FROM employe";
        DatabaseHelper bd = DatabaseHelper.getInstance();
        List<Employe> listeEmp = new ArrayList<>();
        try {
            bd.prepareStatement(sql);
            ResultSet rs = bd.executeQuery();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (rs.next())
            {
                Employe em = new Employe();
                em.setId(rs.getInt(1));
                em.setMatricule(rs.getString(2));
                em.setPrenom(rs.getString(3));
                em.setNom(rs.getString(4));
                em.setDateNaissance(LocalDate.parse(rs.getString(5),df));
                em.setSalaire(rs.getInt(6));
                sql = "SELECT * FROM service WHERE id = " + rs.getInt(7);
                bd.prepareStatement(sql);
                ResultSet rs1 = bd.executeQuery();
                Service sc = new Service();
                int idDept = 0;
                while (rs1.next())
                {
                    sc.setId(rs1.getInt(1));
                    sc.setLibelle(rs1.getString(2));
                    idDept = rs1.getInt(3);
                }
                sql = "SELECT * FROM departement WHERE id = "+ idDept;
                rs1.close();

                bd.prepareStatement(sql);
                ResultSet rs2 = bd.executeQuery();
                Departement dept = new Departement();
                while (rs2.next())
                {
                    dept.setId(rs2.getInt(1));
                    dept.setLibelle(rs2.getString(2));
                }
                rs2.close();

                em.setService(sc);
                em.setDept(dept);
                listeEmp.add(em);
            }
            return listeEmp;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }

    }

    public boolean exist(Employe emp)
    {
        String sql = "SELECT * FROM employe WHERE matricule = ?";
        DatabaseHelper bd = DatabaseHelper.getInstance();
        try {
            bd.prepareStatement(sql);
            Object[] tab = {emp.getMatricule()};
            bd.bindParams(tab);
            ResultSet rs = bd.executeQuery();
            while (rs.next())
            {
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

    public boolean updateEmp(Employe emp)
    {
        DatabaseHelper db = DatabaseHelper.getInstance();
        String sql = "UPDATE INTO employe SET nom = ?,prenom ?,datenais ?,salaire ?, idService = "+emp.getService().getId()+" WHERE matricule = ?";
        try
        {
            db.prepareStatement(sql);
            Object[] tab = {emp.getNom(), emp.getPrenom(), emp.getDateNaissance(), emp.getSalaire(), emp.getMatricule()};
            db.bindParams(tab);
            db.executeUpdate();
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
}
