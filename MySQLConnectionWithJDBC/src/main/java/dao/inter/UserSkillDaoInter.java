package dao.inter;

import entitiy.UserSkill;

import java.util.List;

public interface UserSkillDaoInter {
    public List<UserSkill> getAllSkillByUserId(int userId);
}
