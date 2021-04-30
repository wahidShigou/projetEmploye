package controller;

import com.sun.xml.internal.bind.v2.model.core.ID;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import modeles.Departement;
import modeles.Employe;
import modeles.Service;
import services.*;
import utilitaires.LoadView;
import utilitaires.Utils;

import javax.swing.text.DateFormatter;
import java.net.URL;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeController implements Initializable {

    @FXML
    private TextField nomTfd;

    @FXML
    private TextField prenomTfd;

    @FXML
    private TextField matriculeTfd;

    @FXML
    private TextField datNaisTfd;

    @FXML
    private TextField salaireTfd;

    @FXML
    private ChoiceBox<Departement> departementCbx;

    @FXML
    private ChoiceBox<Service> serviceCbx;

    @FXML
    private Button btnEnregistrer;

    @FXML
    private TableView<Employe> tableEmploye;

    @FXML
    private TableColumn<Employe, String> colMat;

    @FXML
    private TableColumn<Employe, String> colNom;

    @FXML
    private TableColumn<Employe, String> colPrenom;

    @FXML
    private TableColumn<Employe, String> colDatenais;

    @FXML
    private TableColumn<Employe, String> colService;

    @FXML
    private TableColumn<Employe, String> colDepartement;

    @FXML
    private TableColumn<Employe, Integer> colSalaire;

    IEmploye IE = new EmployeDao();

    @FXML
    void saveHandler(ActionEvent event) throws Exception {

        //Utils.showMessage("Gestion des employés","Ajout employé","ok");
        String mat = matriculeTfd.getText().trim();
        String nom = nomTfd.getText().trim();
        String pnom = prenomTfd.getText().trim();
        LocalDate datNais = null;
        int salaire = 0;
        boolean salok = false;
        boolean dateok = false;


        if(mat.equals("") || nom.equals("") || pnom.equals("") || datNaisTfd.getText().trim().equals("")
                || salaireTfd.getText().trim().equals("") || departementCbx.getSelectionModel().getSelectedIndex() == -1
                || serviceCbx.getSelectionModel().getSelectedIndex() == -1)
        {
            Utils.showMessage("Gestion des employes","Ajout employe","Veuillez remplir tous les champs");
        }
        else {
            try {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                datNais = LocalDate.parse(datNaisTfd.getText().trim(),df);
                if(datNais != null && datNais.getYear() < 2005)
                dateok = true;
                else dateok = false;
            }
            catch (Exception e)
            {
                dateok = false;
            }
            try {
                salaire = Integer.parseInt(salaireTfd.getText().trim());
                if(salaire >= 50000)
                {
                    salok = true;
                }

            }
            catch (Exception e)
            {
                salok = false;
            }
            if(dateok == false)
            {
                Utils.showMessage("Gestion des employes","Ajout employe","DATE INVALIDE (YYYY-MM-DD)");
            }
            else
            {
                if(salok == false)
                {
                    Utils.showMessage("Gestion des employes","Ajout employe","SALAIRE MINIMUM (50000)");
                }
                else
                {
                    Employe emp = new Employe();
                    emp.setMatricule(mat);
                    emp.setNom(nom);
                    emp.setPrenom(pnom);
                    emp.setSalaire(salaire);
                    emp.setDateNaissance(datNais);
                    emp.setService(serviceCbx.getValue());
                    if(IE.exist(emp))
                    {
                        if(IE.updateEmp(emp))
                        {
                            Utils.showMessage("Gestion des employes","Modification employe","succes");
                            viderChamps();
                            actualiserTab();
                        }
                        else
                        {
                            Utils.showMessage("Gestion des employes","Ajout employe","echec");
                        }
                    }
                    else {
                        if(IE.addEmploye(emp))
                        {
                            Utils.showMessage("Gestion des employes","Ajout employe","succes");
                            viderChamps();
                            actualiserTab();
                        }
                        else
                        {
                            Utils.showMessage("Gestion des employes","Ajout employe","echec");
                        }
                    }
                }
            }
        }
    }

    @FXML
    void modifierEmp(ActionEvent event) throws Exception{
        //Utils.showMessage("Gestion des employes","Modification employe","ICI");
        if(tableEmploye.getSelectionModel().getSelectedIndex() == -1)
        {
            Utils.showMessage("Gestion des employes","Modification employe","selectionner l'employe a modifier");
        }
        else {
            Employe emp = tableEmploye.getSelectionModel().getSelectedItem();
            matriculeTfd.setText(emp.getMatricule());
            nomTfd.setText(emp.getNom());
            prenomTfd.setText(emp.getPrenom());
            datNaisTfd.setText(""+emp.getDateNaissance());
            salaireTfd.setText(""+emp.getSalaire());
            departementCbx.getSelectionModel().select(emp.getDept().getId()-1);
            List<Service> listS = new ArrayList<>();
            listS = departementCbx.getSelectionModel().getSelectedItem().getListeService();
            for(Service sc : listS)
            {
                if(sc.getId() == emp.getService().getId())
                {
                    serviceCbx.getSelectionModel().select(listS.indexOf(sc));
                }
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            matriculeTfd.setText(IE.genereMat());
            IDepartement iDepartement =  new DepartementDao();
            List<Departement> listeDepartement = iDepartement.findAll();
            departementCbx.setItems(FXCollections.observableArrayList(listeDepartement));

            departementCbx.valueProperty().addListener(new ChangeListener<Departement>() {
                @Override
                public void changed(ObservableValue<? extends Departement> observable, Departement oldValue, Departement newValue) {

                    try{
                        if(departementCbx.getValue() != null) {
                            int id = departementCbx.getValue().getId();

                            List<Service> listeService = departementCbx.getValue().getListeService();
                            serviceCbx.setItems(FXCollections.observableArrayList(listeService));
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            });
            actualiserTab();



        }catch (Exception e){

        }

    }
    public void viderChamps()
    {
        matriculeTfd.setText(IE.genereMat());
        nomTfd.setText("");
        prenomTfd.setText("");
        datNaisTfd.setText("");
        salaireTfd.setText("");
        serviceCbx.getSelectionModel().clearSelection();
        departementCbx.getSelectionModel().clearSelection();
    }

    public void actualiserTab()
    {
        ObservableList<Employe> empList = FXCollections.observableArrayList();

        empList.addAll(IE.getAllEmp());

        colMat.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colDatenais.setCellValueFactory(new PropertyValueFactory<>("dateNais"));
        colSalaire.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        colDepartement.setCellValueFactory(new PropertyValueFactory<>("dept"));
        colService.setCellValueFactory(new PropertyValueFactory<>("service"));

        tableEmploye.setItems(empList);
    }
}
