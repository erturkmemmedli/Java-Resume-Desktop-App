package dao.inter;

import entity.EmploymentHistory;

import java.util.List;

public interface UserEmploymentHistoryDaoInter {
    public List<EmploymentHistory> getAllEmploymentHistoryByUserId(int id);
}
