package services;

import modeles.Departement;
import modeles.Service;
import utilitaires.DatabaseHelper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartementDao implements IDepartement {

    @Override
    public List<Departement> findAll() throws Exception {
        String sql = "SELECT * FROM departement ";

        DatabaseHelper db = DatabaseHelper.getInstance();
        db.prepareStatement(sql);

        ResultSet rs = db.executeQuery();

        List<Departement> listeDepartement = new ArrayList<>();

        while(rs.next()){
            Departement d = new Departement();
            d.setId(rs.getInt(1));
            d.setLibelle(rs.getString(2));

            sql = "SELECT * FROM service WHERE idDept =  " + d.getId();
            db.prepareStatement(sql);
            ResultSet rs1 = db.executeQuery();
            List<Service> listeService = new ArrayList<>();

            while (rs1.next()){
                Service s = new Service();
                s.setId(rs1.getInt(1));
                s.setLibelle(rs1.getString(2));
                listeService.add(s);
            }
            rs1.close();
            d.setListeService(listeService);
            listeDepartement.add(d);
        }

        return listeDepartement;
    }
}
