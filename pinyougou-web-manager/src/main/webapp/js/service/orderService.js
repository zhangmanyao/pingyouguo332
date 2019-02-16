//服务层
app.service('orderService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../order/findAll.do');
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../order/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../order/findOne.do?id='+id);
	}

	//统计
	this.search=function(page,rows,createTime,searchEntity){
		return $http.post('../order/statistics.do?page='+page+"&rows="+rows+"&createTime="+createTime, searchEntity);
	}

	//查询
    this.search=function(page,rows,minPrice,maxPrice,searchEntity){
        return $http.post('../order/search.do?page='+page+"&rows="+rows+"&minPrice="+minPrice+"&maxPrice="+maxPrice, searchEntity);
    }

});

