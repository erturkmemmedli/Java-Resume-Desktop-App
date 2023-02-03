package dao.inter;

import entity.Country;

import java.util.List;

public interface CountryDaoInter {
    public List<Country> getCountryAndNationality();
    public List<Country> getAllCountry();
    public List<Country> getAllNationality();
}
