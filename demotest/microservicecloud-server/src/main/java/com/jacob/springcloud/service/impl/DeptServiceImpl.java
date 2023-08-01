package com.jacob.springcloud.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacob.springcloud.dao.DeptDao;
import com.jacob.springcloud.model.Dept;
import com.jacob.springcloud.service.DeptService;
/**
 * 自己練習不重要
 * @author user
 *
 */
@Service
public class DeptServiceImpl implements DeptService {
	
	@Autowired
	private DeptDao dao;
	@Override
	public boolean add(Dept dept) {
		return dao.addDept(dept);
	}
	@Override
	public Dept get(Long id) {
		return dao.findById(id);
	}

	@Override
	public List<Dept> list() {
		return dao.findAll();
	}

}
