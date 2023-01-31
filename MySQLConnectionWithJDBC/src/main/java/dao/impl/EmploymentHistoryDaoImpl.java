package dao.impl;

import dao.inter.AbstractDao;
import dao.inter.EmploymentHistoryDaoInter;
import entitiy.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmploymentHistoryDaoImpl extends AbstractDao implements EmploymentHistoryDaoInter {
    @Override
    public List<EmploymentHistory> getAllEmploymentHistoryByUserId(int userId) {
        List<EmploymentHistory> result = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("select "
                    + "* from employment_history "
                    + "where user_id = ?");
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
        return new EmploymentHistory(null, header, beginDate, endDate,jobDescription, new User(userId));
    }
}
