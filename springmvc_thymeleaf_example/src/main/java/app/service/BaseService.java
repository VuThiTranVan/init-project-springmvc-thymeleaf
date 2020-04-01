package app.service;

import java.io.Serializable;

public interface BaseService<PK, T> {
	public T findById(Serializable key);

	public T saveOrUpdate(T entity);
	
	public T save(T entity);

	public boolean delete(T entity);
}
