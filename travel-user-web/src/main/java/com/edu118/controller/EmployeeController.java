package com.edu118.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.edu118.api.BaseController;
import com.edu118.api.PinYinUtils;
import com.edu118.entity.Employee;
import com.edu118.service.EmployeeService;
import com.edu118.utils.md5.MD5Utils;
import com.edu118.validateGroup.IAddGroups;

@Controller
@RequestMapping("/emp")
public class EmployeeController extends BaseController {
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping("/addEmp")
	@RequiresPermissions("emp:add")
	public ModelAndView addEmp(
			HttpServletRequest request,
			@Validated(IAddGroups.class) Employee emp,
			MultipartFile file) {
		//获取当前时间
		LocalDateTime localDate = LocalDateTime.now();
		String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		emp.setHiredate(date);
		
		//获取拼音
		String py = PinYinUtils.getPinYinHeadChar(emp.getEname());
		emp.setEmpNumber("XYD-" + py + "-"
				+ localDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
		
		//加密
		emp.setPassword(MD5Utils.getPassword(emp.getPassword()));
		logger.info("Emp = {}", emp);
		logger.info("file = {}", file);
		
		//上传文件
		if(null != file && !StringUtils.isEmpty(file.getOriginalFilename())) {
			//上传图片
			String contentType = file.getContentType();
			System.out.println("上传文件的类型：" + contentType);
			//对类型进行逻辑处理，如果不是图片将返回错误信息
			if("image/jpg | image/jpeg | image/png | image/gif".contains(contentType)) {
				String fileName = upLoadFile(request,file,emp.getEmpNumber());
				emp.setPhoto(fileName);
			}
		}
		
		//保存
		employeeService.insertEntity(emp);
		
		ModelAndView modelAndView = new ModelAndView("../pages/emp/emp_add.jsp");
		modelAndView.addObject("message", "数据添加完成！");
		
		return modelAndView;
	}
	
	/**
	 * 保存上传的文件，保存完成，就返回当前保存的文件名称
	 * @return 返回文件名称
	 */
	private String upLoadFile(
			HttpServletRequest request,
			MultipartFile multipartFile,
			String empNumber) {
//		BufferedInputStream bis = null;
//		BufferedOutputStream bos = null;
		try {
//			bis = new BufferedInputStream(multipartFile.getInputStream());
			
			//保存到当前项目的webapp下的images/photo
			String path = request.getServletContext().getRealPath("/images/photo");
			System.out.println("path = " + path);
			
			//文件后缀的获取
			String name = multipartFile.getOriginalFilename();
			String hz = name.substring(name.lastIndexOf("."));
			String fileName = empNumber + hz;
			File file = new File(path + "/" + fileName);
			System.out.println("file = " + file);
//			bos = new BufferedOutputStream(new FileOutputStream(file));
//			int b;
//			while((b = bis.read()) != -1) {
//				bos.write(b);
//			}
			multipartFile.transferTo(file);
			
			return fileName;
		}catch (IllegalStateException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
