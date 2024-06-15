package lk.ijse.repository;

import lk.ijse.db.DBConnection;
import lk.ijse.model.RawMaterial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RawMaterialRepo {
    public static boolean saveMaterials(RawMaterial rawMaterial) throws SQLException {
        String sql = "INSERT INTO Raw_Material VALUES (?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,rawMaterial.getMaterialId());
        pstm.setObject(2,rawMaterial.getMaterialName());
        pstm.setObject(3,rawMaterial.getUnitPrice());

        return pstm.executeUpdate() > 0;

    }

    public static boolean updateMaterials(RawMaterial rawMaterial) throws SQLException {
        String sql = "UPDATE Raw_Material SET material_name = ?, unit_price = ? WHERE material_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,rawMaterial.getMaterialName());
        pstm.setObject(2,rawMaterial.getUnitPrice());
        pstm.setObject(3,rawMaterial.getMaterialId());

        return  pstm.executeUpdate() > 0;
    }

    public static RawMaterial searchMaterialById(String materialId) throws SQLException {
        String sql = "SELECT * FROM Raw_Material WHERE material_id = ?";

        Connection connection =DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,materialId);

        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
           String material_id = resultSet.getString(1);
           String material_name = resultSet.getString(2);
           double unit_price = resultSet.getDouble(3);

           RawMaterial rawMaterial = new RawMaterial(material_id,material_name,unit_price);

           return rawMaterial;
        }
        else {
            return null;
        }
    }

    public static boolean deleteMaterials(String materialId) throws SQLException {
        String sql = "DELETE FROM Raw_Material WHERE material_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,materialId);

        return pstm.executeUpdate() > 0;
    }

    public static List<RawMaterial> getAllMaterials() throws SQLException {
        String sql = "SELECT * FROM Raw_Material";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<RawMaterial> mtList = new ArrayList<>();

        while (resultSet.next()){
            String materialId = resultSet.getString(1);
            String materialName = resultSet.getString(2);
           double unitPrice = Double.parseDouble(resultSet.getString(3));

            RawMaterial rawMaterial = new RawMaterial(materialId,materialName,unitPrice);

            mtList.add(rawMaterial);
        }
        return mtList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT material_id FROM Raw_Material";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<String> mtIDList = new ArrayList<>();
        while (resultSet.next()){
            mtIDList.add(resultSet.getString(1));
        }
        return mtIDList;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT material_id FROM Raw_Material ORDER BY material_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            String materialId = resultSet.getString(1);
            return materialId;
        }
        return null;
    }
}
