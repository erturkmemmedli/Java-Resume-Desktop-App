package main;

import dao.inter.UserDaoInter;

public class MyJDBC {
    public static void main(String[] args) throws Exception {
        // sample call
        UserDaoInter userDao = Context.instanceUserDao();
        System.out.println(userDao.getAll());
    }
}
