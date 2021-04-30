package utilitaires;

import java.sql.*;

public class DatabaseHelper {
    private Connection cnx;
    private Statement stmt;
    private PreparedStatement pstmt;
    private static DatabaseHelper db;


    private DatabaseHelper(){}

    public static DatabaseHelper getInstance(){

        if (db == null ){
            db = new DatabaseHelper();
        }
        return db;
    }

    private void getConnection() throws Exception{
        try{
            if(cnx == null || cnx.isClosed()){
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/java_entreprise?serverTimezone=UTC";
                String user = "root", pwd = "";
                cnx = DriverManager.getConnection(url,user,pwd);
                stmt = cnx.createStatement();
                System.out.println(true);
            }
        }catch (Exception e){
            throw e;
        }
    }

    public ResultSet executeQuery(String sql) throws Exception{
        try{
            getConnection();
            return stmt.executeQuery(sql);
        }catch (Exception e){
            throw e;
        }
    }

    public int executeUpdate(String sql) throws Exception{
        try{
            getConnection();
            return stmt.executeUpdate(sql);
        }catch (Exception e){
            throw e;
        }
    }

    public ResultSet executeSelect(String tableName, String critere) throws Exception{
        try{
            getConnection();
            String sql = "SELECT * FROM "+tableName+" ";
            if(!critere.equals("")){
                sql += "WHERE "+critere;
            }
            return stmt.executeQuery(sql);
        }catch (Exception e){
            throw e;
        }
    }

    public void prepareStatement(String sql) throws Exception{
        try {
            getConnection();
            if (sql.trim().toLowerCase().startsWith("insert")){
                pstmt = cnx.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            }else {
                pstmt = cnx.prepareStatement(sql);
            }

        }catch (Exception e){

        }
    }

    public void bindParams(Object[] parametres) throws Exception{
        for (int i=0; i<parametres.length; i++){
            pstmt.setObject(i+1,parametres[i]);
        }
    }

    public ResultSet executeQuery() throws Exception{
        try{
            getConnection();
            return pstmt.executeQuery();
        }catch (Exception e){
            throw e;
        }
    }

    public int executeUpdate() throws Exception{
        try{
            getConnection();
            return pstmt.executeUpdate();
        }catch (Exception e){
            throw e;
        }
    }

    public void beginTransaction() throws Exception{
        try {
            cnx.setAutoCommit(false);
        }catch (Exception e){
            throw e;
        }
    }

    public void endTransaction() throws Exception{
        try {
            cnx.setAutoCommit(true);
        }catch (Exception e){
            throw e;
        }
    }

    public void commit() throws Exception{
        try {
            cnx.commit();
        }catch (Exception e){
            throw e;
        }
    }

    public void closeConnection() throws Exception{
        try {
            if(stmt!=null && stmt.isClosed()){
                stmt.close();
            }
            if(pstmt!=null && pstmt.isClosed()){
                pstmt.close();
            }
            if(cnx!=null && cnx.isClosed()){
                cnx.close();
            }
        }catch (Exception e){
            throw e;
        }
    }

    public int lastIdFor(String table){
        ResultSet rs;
        String sql = "SELECT id FROM "+table+" ORDER BY id DESC";
        try{
            prepareStatement(sql);
            rs = executeQuery();
            if(rs.next()) {
                return (rs.getInt("id") + 1);
            }
            else
            {
                return 1;
            }
        }catch (Exception e)
        {

        }
        return 0;
    }
}
