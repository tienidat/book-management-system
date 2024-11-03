package org.example.eproject_2.common;

import java.util.List;

public interface IMethod<T> {
    boolean add(T t);
    List<T> getAll();
    boolean update(T obj, int id);
    boolean delete(int id);
    List<T> findByName(String name);
}

