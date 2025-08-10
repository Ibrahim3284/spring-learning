package org.example;

import java.sql.*;

public class DemoJdbc{
    public static void main(String[] args) throws Exception {

        // Load and register driver
        Class.forName("org.postgresql.Driver");

        // Get connection
        String url = "jdbc:postgresql://localhost:5432/demo";
        String username = "postgres";
        String password = "Mohd@2423";

        Connection con = DriverManager.getConnection(url, username, password);
        System.out.println("Connection established");

        // Statement
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * from public.student");
//        rs.next();
//        System.out.println(rs.getString("sname"));

        while(rs.next()) {
            System.out.print(rs.getInt(1) + " - ");
            System.out.print(rs.getString(3) + " - ");
            System.out.println(rs.getInt(2));
        }

        // Create operations
        // Prepared statement
        String sql = "INSERT INTO public.student values(?, ?, ?)";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setInt(1, 6);
        preparedStatement.setInt(2, 95);
        preparedStatement.setString(3, "Max");

        preparedStatement.execute();

        // Close connection
        con.close();
        System.out.println("Connection closed");

    }
}