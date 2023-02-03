package dao.inter;

import entity.EmploymentHistory;

import java.util.List;

public interface EmploymentHistoryDaoInter {
    public List<EmploymentHistory> getAllEmploymentHistoryByUserId(int userId);
    public EmploymentHistory getEmploymentHistoryId(int id);
    public void updateEmploymentHistory(EmploymentHistory e);
    public void insertEmploymentHistory(EmploymentHistory e);
    public void deleteEmploymentHistory(int id);
}
