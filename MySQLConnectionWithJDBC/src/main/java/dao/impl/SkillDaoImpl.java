package dao.impl;

import dao.inter.AbstractDao;
import dao.inter.SkillDaoInter;
import entitiy.Skill;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SkillDaoImpl extends AbstractDao implements SkillDaoInter {
    @Override
    public List<Skill> getAll() {
        List<Skill> list = new ArrayList<>();
        try(Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select name from skill");
            while (resultSet.next()) {
                list.add(new Skill(null, resultSet.getString("name")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
