package dao.inter;

import entity.UserSkill;

import java.util.List;

public interface UserSkillDaoInter {
    public List<UserSkill> getAllSkillByUserId(int userId);
    public void deleteSkillById(int id);
    public void insertSkillByUser(UserSkill u);
    public void updateUserSkill(UserSkill u);
}
