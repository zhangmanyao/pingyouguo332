app.controller("dataVo",function($scope){

	$scope.reloadList = function(){
		// $scope.findByPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
		$scope.search();
	}

    $scope.search=function(){
        goodsService.search().success(
            function(response){
                $scope.list=response.rows;
            }
        );
    }

    this.search=function(){
        return $http.post('../managerData/searchManagerDataVo.do');
    }

});