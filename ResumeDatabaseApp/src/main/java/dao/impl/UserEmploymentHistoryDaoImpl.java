package dao.impl;

import dao.inter.AbstractDao;
import dao.inter.UserEmploymentHistoryDaoInter;
import entity.EmploymentHistory;
import entity.User;
import entity.UserSkill;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserEmploymentHistoryDaoImpl extends AbstractDao implements UserEmploymentHistoryDaoInter {

    public EmploymentHistory getUserEmploymentHistory(ResultSet rs) throws SQLException {
        int id = rs.getInt("em_id");
        int userId = rs.getInt("id");
        String header = rs.getString("header");
        String jobDescription = rs.getString("job_description");
        Date beginDate = rs.getDate("begin_date");
        Date endDate = rs.getDate("end_date");
        return new EmploymentHistory(id, header, beginDate, endDate, jobDescription, new User(userId));
    }

    @Override
    public List<EmploymentHistory> getAllEmploymentHistoryByUserId(int id) {
        List<EmploymentHistory> list = new ArrayList<>();
        List<UserSkill> result = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("select " +
                    "u.id, em.id as em_id, em.header, em.begin_date, em.end_date, em.job_description " +
                    "from employment_history em " +
                    "left join user u " +
                    "on em.user_id = u.id " +
                    "where em.user_id = ?");
            statement.setInt(1, id);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                EmploymentHistory eh = getUserEmploymentHistory(rs);
                list.add(eh);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
}