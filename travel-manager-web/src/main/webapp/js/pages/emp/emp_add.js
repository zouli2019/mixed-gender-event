$(function(){
	//加载部门数据
	getDept();
	//加载职位数据
	getLevel();
})

function getLevel(){
	requestUrl = "/level/findLevel";
	$.when(ajaxRequest()).done(function (resultData){
		$("#emp_add_level").html("");
		$("#emp_add_level").append("<option>=======请选择该员工所在的职位=======</option>");
		
		$.each(resultData.data, function(i, level) {
			$("#emp_add_level").append('<option value="' + level.lid + '">' + level.title + '</option>');
		});
	});
}

function getDept(){
	requestUrl = "/dept/findDept";
	$.when(ajaxRequest()).done(function (resultData){
		//在这里处理业务
		//console.log(requestData);
		//清空原有的数据
		$("#emp_add_dept").html("");
		$("#emp_add_dept").append("<option>=======请选择该员工所在的部门=======</option>");
		
		//把查询到的数据进行遍历并添加到选项中
		$.each(resultData.data, function(i, dept){
			$("#emp_add_dept").append('<option value="' + dept.did + '">' + dept.dname + '</option>');
		});
	});
	/*
	 * 每一个函数若是都会那么写，肯定会重复、麻烦，所以在muyu.js中进行抽取
	 * $.ajax({
	 * 	type:"get",
	 * url:"",
	 * async:true
	 * });
	 */
}

function addEmp(){
	//使用插件进行数据的验证
	//验证通过，在这里进行提交的提示
	notie.confirm(
		"是否提交当前表单数据？？",
		'确认','取消', function(){
			//验证通过，完成表单的提交
			$("#addEmpForm")[0].submit();
		
		});
}
