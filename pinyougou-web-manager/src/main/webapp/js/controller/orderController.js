 //控制层 
app.controller('orderController' ,function($scope,$controller,orderService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
        orderService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){
        orderService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){
        orderService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象

	$scope.searchEntity.creatTimeStr=$filter("date")($scope.searchEntity.creatTimeStr,"yyyy-MM-dd")
	
	//搜索
	$scope.search=function(page,rows){
        /*orderService.statistics(page,rows,$scope.createTime,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);*/

        orderService.search(page,rows,$scope.minPrice,$scope.maxPrice,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );



	}


    
	// 显示状态
	$scope.status = ["未审核","审核通过","审核未通过","关闭"];
});
