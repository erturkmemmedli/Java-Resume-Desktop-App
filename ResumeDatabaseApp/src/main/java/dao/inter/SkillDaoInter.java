package dao.inter;

import entity.Skill;

import java.util.List;

public interface SkillDaoInter {
    public List<Skill> getAll();
    public int insertSkill(Skill skill);
}
