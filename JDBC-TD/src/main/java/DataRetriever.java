import org.example.Category;
import org.example.Product;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataRetriever {
    private final Connection connection;
    public DataRetriever(Connection connection) {
        this.connection = connection;
    }
    public List<Category> getAllCategories() throws SQLException {
        String sql = "SELECT id, name, product_id from Product_category ORDER BY id";
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery();
        ) {
            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
            return categories;
        }
        catch (SQLException e) {
            throw new SQLException(e);
        }
    }
    public List<Product> getProductList(int page,int size) throws SQLException {
        String sql = "SELECT p.id, p.name, p.creation_datetime, c.id, c.name FROM Product as p JOIN Product_category as c on p.id = c.product_id" + " ORDER BY p.id" +  " LIMIT " + size + " OFFSET " + (page * size - size) ;
        List<Product> products = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
        ){
            while (rs.next()) {
                products.add(new org.example.Product(rs.getInt(1),
                        rs.getString(2),
                        rs.getTimestamp(3).toInstant(),
                        new Category(rs.getInt(4), rs.getString(5))
                ));
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return products;
     }
    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.creation_datetime,c.id, c.name FROM Product p JOIN Product_category c ON p.id = c.product_id " +
                "WHERE true = true ";
        List<Object> to_replace = new ArrayList<>();
        if(productName != null && !productName.isEmpty()) {
            sql += "AND p.name ILIKE ? ";
            to_replace.add("%" + productName + "%");
        }
        if(categoryName != null && !categoryName.isEmpty()) {
            sql += "AND c.name ILIKE ? ";
            to_replace.add("%" + categoryName + "%");
        }
        if(creationMin != null) {
            sql += "AND p.creation_datetime >= ? ";
            to_replace.add(Timestamp.from(creationMin));
        }
        if(creationMax != null) {
            sql += "AND p.creation_datetime <= ? ";
            to_replace.add(Timestamp.from(creationMax));
        }
        sql += " ORDER BY p.id ";

        PreparedStatement statement = connection.prepareStatement(sql);
        for(int i = 0; i < to_replace.size(); i++) {
            statement.setObject(i + 1, to_replace.get(i));
        }
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(new Product(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getTimestamp(3).toInstant(),
                        new Category(resultSet.getInt(4),resultSet.getString(5))
                        ));
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return products;
    }
    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax,int page, int size) throws SQLException {
       try{
           List<Product> products = this.getProductsByCriteria(productName, categoryName, creationMin, creationMax);
           return products.stream().skip((long) page * size - size).limit(size).collect(Collectors.toList());
       }catch(SQLException e){
            throw new SQLException(e);
       }
    }
}