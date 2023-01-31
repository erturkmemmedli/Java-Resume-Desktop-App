package dao.inter;

import entitiy.User;

import java.util.List;

public interface UserDaoInter {
    List<User> getAll();
    User getById(int id);
    boolean updateUser(User u);
    boolean removeUser(int id);
    boolean addUser(User u);
}
