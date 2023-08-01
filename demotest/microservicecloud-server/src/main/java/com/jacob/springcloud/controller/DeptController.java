//package com.jacob.springcloud.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.jacob.springcloud.entities.Dept;
//import com.jacob.springcloud.service.DeptService;
///**
// * 自己練習不重要
// * @author user
// *
// */
//@RestController
//public class DeptController {
//
//
//	private DeptService service;
//	
//    @Autowired
//    public DeptController(DeptService service) {
//        this.service = service;
//    }
//	
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String home() {
//        return "Connected to the controller successfully!";
//    }
//	
//
//    @RequestMapping(value = "/aa", method = RequestMethod.GET)
//    public String aaa() {
//        return "Connected to the controller successfully!";
//    }
//	
//	
//	@RequestMapping(value = "/dept/add", method = RequestMethod.POST)
//	public boolean add(@RequestBody Dept dept) {
//		return service.add(dept);
//	}
//	
//	@RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
//	public Dept get(@PathVariable("id") Long id) {
//		return service.get(id);
//	}
//	
//	@RequestMapping(value = "/dept/list", method = RequestMethod.GET)
//	public List<Dept> list() {
//		return service.list();
//	}
//	
//}