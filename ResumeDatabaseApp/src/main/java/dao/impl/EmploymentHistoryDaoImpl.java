package dao.impl;

import dao.inter.AbstractDao;
import dao.inter.EmploymentHistoryDaoInter;
import entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmploymentHistoryDaoImpl extends AbstractDao implements EmploymentHistoryDaoInter {
    @Override
    public List<EmploymentHistory> getAllEmploymentHistoryByUserId(int userId) {
        List<EmploymentHistory> result = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("select " +
                    "* from employment_history " +
                    "where user_id = ?");
            statement.setInt(1, userId);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                EmploymentHistory eh = getEmploymentHistory(resultSet);
                result.add(eh);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private EmploymentHistory getEmploymentHistory(ResultSet resultSet) throws Exception {
        int userId = resultSet.getInt("user_id");
        String header = resultSet.getString("header");
        String jobDescription = resultSet.getString("job_description");
        Date beginDate = resultSet.getDate("begin_date");
        Date endDate = resultSet.getDate("end_date");
        return new EmploymentHistory(userId, header, beginDate, endDate,jobDescription, new User(userId));
    }

    @Override
    public EmploymentHistory getEmploymentHistoryId(int id) {
        EmploymentHistory e = null;
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("select " +
                    "* from employment_history " +
                    "where id = ?");
            statement.setInt(1, id);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                e = getEmploymentHistory(resultSet);
                return e;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return e;
    }

    @Override
    public void updateEmploymentHistory(EmploymentHistory e) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("update " +
                    "employment_history " +
                    "set header=?, job_description=?, begin_date = ?, end_date = ? " +
                    "where id = ?");
            statement.setString(1, e.getHeader());
            statement.setString(2, e.getJobDescription());
            statement.setDate(3, e.getBeginDate());
            statement.setDate(4, e.getEndDate());
            statement.setInt(5, e.getId());
            statement.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void insertEmploymentHistory(EmploymentHistory e) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("insert " +
                            "employment_history (id, header, job_description, begin_date , end_date, user_id ) " +
                            "values (? , ? , ? , ? , ?, ?) ",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, e.getId());
            statement.setString(2, e.getHeader());
            statement.setString(3, e.getJobDescription());
            statement.setDate(4, e.getBeginDate());
            statement.setDate(5, e.getEndDate());
            statement.setInt(6, e.getUser().getId());
            statement.execute();
            ResultSet genKeys = statement.getGeneratedKeys();
            if(genKeys.next()){
                e.setId(genKeys.getInt(1));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteEmploymentHistory(int id) {
        try(Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("delete " +
                    "from employment_history " +
                    "where id = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
