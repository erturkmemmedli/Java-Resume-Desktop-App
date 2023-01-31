package dao.impl;

import dao.inter.AbstractDao;
import dao.inter.UserSkillDaoInter;
import entitiy.Skill;
import entitiy.User;
import entitiy.UserSkill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserSkillDaoImpl extends AbstractDao implements UserSkillDaoInter {
    @Override
    public List<UserSkill> getAllSkillByUserId(int userId) {
        List<UserSkill> result = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("select "
                    + "u.*, us.skill_id, us.power, s.name as skill_name " +
                    "from user_skill us " +
                    "left join user u on us.user_id = u.id " +
                    "left join skill s on us.skill_id = s.id " +
                    "where us.user_id = ?");
            statement.setInt(1, userId);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                UserSkill us = getUserSkill(resultSet);
                result.add(us);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
        }

    private UserSkill getUserSkill(ResultSet resultSet) throws Exception {
        int userId = resultSet.getInt("id");
        int skillId = resultSet.getInt("skill_id");
        String skillName = resultSet.getString("skill_name");
        int power = resultSet.getInt("power");
        return new UserSkill(null, new User(userId), new Skill(skillId, skillName), power);
    }
}
