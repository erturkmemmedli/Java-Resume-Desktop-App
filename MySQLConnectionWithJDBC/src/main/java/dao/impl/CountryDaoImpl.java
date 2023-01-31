package dao.impl;

import dao.inter.AbstractDao;
import dao.inter.CountryDaoInter;
import entitiy.Country;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CountryDaoImpl extends AbstractDao implements CountryDaoInter {
    @Override
    public List<Country> getAllCountry() {
        List<Country> list = new ArrayList<>();
        try(Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select name from country");
            while (resultSet.next()) {
                list.add(new Country(null, resultSet.getString("name"), null));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Country> getAllNationality() {
        List<Country> list = new ArrayList<>();
        try(Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select nationality from country");
            while (resultSet.next()) {
                list.add(new Country(null, null, resultSet.getString("nationality")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
