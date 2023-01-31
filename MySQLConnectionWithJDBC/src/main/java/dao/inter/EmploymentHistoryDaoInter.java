package dao.inter;

import entitiy.EmploymentHistory;

import java.util.List;

public interface EmploymentHistoryDaoInter {
    public List<EmploymentHistory> getAllEmploymentHistoryByUserId(int userId);

}
