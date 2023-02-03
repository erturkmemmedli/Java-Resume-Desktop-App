package dao.impl;

import dao.inter.AbstractDao;
import dao.inter.UserSkillDaoInter;
import entity.Skill;
import entity.User;
import entity.UserSkill;

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
            PreparedStatement statement = connection.prepareStatement("select " +
                    "u.*, us.skill_id, us.power, s.name as skill_name, us.id as userSkillId " +
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
        int userSkillId = resultSet.getInt("userSkillId");
        String skillName = resultSet.getString("skill_name");
        int power = resultSet.getInt("power");
        return new UserSkill(userSkillId, new User(userId), new Skill(skillId, skillName), power);
    }

    @Override
    public void deleteSkillById(int id) {
        List<UserSkill> result = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("delete " +
                    "from user_skill where id = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void insertSkillByUser(UserSkill u) {
        List<UserSkill> result = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("insert " +
                    "into user_skill (user_id, skill_id, power) " +
                    "select ?, ?, ? from dual " +
                    "where not exists " +
                    "(select * from user_skill " +
                    "where skill_id = ? " +
                    "limit 1)");
            statement.setInt(1, u.getUser().getId());
            statement.setInt(2, u.getSkill().getId());
            statement.setInt(3, u.getPower());
            statement.setInt(4, u.getSkill().getId());
            statement.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateUserSkill(UserSkill u) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("update " +
                    "user_skill set skill_id = ?, user_id = ?, power = ? " +
                    "where id = ?");
            statement.setInt(1, u.getSkill().getId());
            statement.setInt(2, u.getUser().getId());
            statement.setInt(3, u.getPower());
            statement.setInt(4, u.getId());
            statement.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
