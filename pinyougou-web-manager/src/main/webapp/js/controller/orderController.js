 //控制层 
<<<<<<< HEAD
app.controller('orderController' ,function($scope,$controller,$orderService){
=======
app.controller('orderController' ,function($scope,$controller,orderService){
>>>>>>> byy1
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
<<<<<<< HEAD
		orderService.findAll().success(
=======
        orderService.findAll().success(
>>>>>>> byy1
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
<<<<<<< HEAD
	$scope.findPage=function(page,rows){			
		orderService.findPage(page,rows).success(
=======
	$scope.findPage=function(page,rows){
        orderService.findPage(page,rows).success(
>>>>>>> byy1
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
<<<<<<< HEAD
	$scope.findOne=function(id){				
		orderService.findOne(id).success(
=======
	$scope.findOne=function(id){
        orderService.findOne(id).success(
>>>>>>> byy1
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
<<<<<<< HEAD
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=orderService.update( $scope.entity ); //修改
		}else{
			serviceObject=orderService.add( $scope.entity  );//增加
		}				
		serviceObject.success(
			function(response){
				if(response.flag){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 

	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){
        orderService.search(page,rows,$scope.searchEntity).success(
=======
	$scope.searchEntity={};//定义搜索对象

	$scope.searchEntity.creatTimeStr=$filter("date")($scope.searchEntity.creatTimeStr,"yyyy-MM-dd")
	
	//搜索
	$scope.search=function(page,rows){
        /*orderService.statistics(page,rows,$scope.createTime,$scope.searchEntity).success(
>>>>>>> byy1
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
<<<<<<< HEAD
		);
	}
    
	// 显示状态
	$scope.status = ["未支付","已支付","未发货","已发货"];
	

	
	// 审核的方法:
	$scope.updateStatus = function(status){
        orderService.updateStatus($scope.selectIds,status).success(function(response){
			if(response.flag){
				$scope.reloadList();//刷新列表
				$scope.selectIds = [];
			}else{
				alert(response.message);
			}
		});
	}
});	
=======
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
>>>>>>> byy1
