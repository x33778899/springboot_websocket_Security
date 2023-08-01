package com.jacob.springcloud.service;

import java.util.List;

import com.jacob.springcloud.model.Dept;

/**
 * 自己練習不重要
 * @author user
 *
 */
public interface DeptService {
	public boolean add(Dept dept);
	public Dept get(Long id);
	public List<Dept> list();
}
