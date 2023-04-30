package com.example.filter.repository;

import java.sql.SQLException;
import java.util.List;

public interface Repository<T> {
    List<T> list() throws SQLException;
    T byId(int id) throws SQLException;
    void save(T t) throws SQLException;
    void delete(int id) throws SQLException;
}
