package dao.impl;

import dao.inter.AbstractDao;
import dao.inter.SkillDaoInter;
import entity.Skill;
import entity.UserSkill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SkillDaoImpl extends AbstractDao implements SkillDaoInter {
    @Override
    public List<Skill> getAll() {
        List<Skill> list = new ArrayList<>();
        try(Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from skill");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String skill = resultSet.getString("name");
                list.add(new Skill(id, skill));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public int insertSkill(Skill skill) {
        try(Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("insert " +
                            "into skill (name) " +
                            "select ? from dual " +
                            "where not exists " +
                            "(select * from skill " +
                            "where name = ? " +
                            "limit 1)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, skill.getName());
            statement.setString(2, skill.getName());
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                skill.setId(generatedKeys.getInt(1));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return skill.getId();
    }

    public void deleteSkill(int id) {
        List<UserSkill> result = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("delete " +
                    "from skill where id = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
