package dao.impl;

import entitiy.Country;
import entitiy.User;
import dao.inter.AbstractDao;
import dao.inter.UserDaoInter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends AbstractDao implements UserDaoInter {
    private User getUser(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        String profileDescription = resultSet.getString("profile_description");
        String email = resultSet.getString("email");
        String phone = resultSet.getString("phone");
        Date birthday = resultSet.getDate("birthday");
        String address = resultSet.getString("address");
        int countryId = resultSet.getInt("country_id");
        int nationalityId = resultSet.getInt("nationality_id");
        String countryStr = resultSet.getString("birthplace");
        String nationalityStr = resultSet.getString("nationality");
        Country country = new Country(countryId, countryStr, null);
        Country nationality = new Country(nationalityId, null, nationalityStr);
        return new User(id, name, surname, profileDescription, email, phone, address, birthday, country, nationality);
    }

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select " +
                    "u.*, n.name as birthplace, c.nationality as nationality " +
                    "from user u " +
                    "left join country as n on u.country_id = n.id " +
                    "left join country as c on u.nationality_id = c.id");
            while (resultSet.next()) {
                User u = getUser(resultSet);
                list.add(u);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public User getById(int userId) {
        User result = null;
        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select " +
                    "u.*, n.name as birthplace, c.nationality as nationality " +
                    "from user u " +
                    "left join country as n on u.country_id = n.id " +
                    "left join country as c on u.nationality_id = c.id " +
                    "where u.id = " + userId);
            while (resultSet.next()) {
                result = getUser(resultSet);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean updateUser(User u) {
        try (Connection connection = connect()) {
            PreparedStatement s = connection.prepareStatement("update user "
                                    + "set name = ?, surname = ?, email = ?, phone = ?, "
                                    + "profile_description = ?, birthday = ?, address = ? "
                                    + "where id = ?");
            s.setString(1, u.getName());
            s.setString(2, u.getSurname());
            s.setString(3, u.getEmail());
            s.setString(4, u.getPhone());
            s.setString(5, u.getProfileDescription());
            s.setDate(6, u.getBirthday());
            s.setString(7, u.getAddress());
            s.setInt(8, u.getId());
            return s.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeUser(int id) {
        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            return statement.execute("delete from user where id = " + id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addUser(User u) {
        try (Connection connection = connect()) {
            PreparedStatement s = connection.prepareStatement("insert " +
                    "into user(name, surname, email, phone, profile_description) " +
                    "values (?, ?, ? ,?, ?)");
            s.setString(1, u.getName());
            s.setString(2, u.getSurname());
            s.setString(3, u.getEmail());
            s.setString(4, u.getPhone());
            s.setString(5, u.getProfileDescription());
            return s.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
