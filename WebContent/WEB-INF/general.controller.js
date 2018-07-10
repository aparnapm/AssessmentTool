angular.module("myApp").controller("GeneralController",GeneralController);

GeneralConrtroller.inject=['$scope','Admin'];

function GeneralController($scope,Admin){
	$scope.admins=Admin.query();
	$scope.admin={};
	$scope.buttonText="Sumbit;"
		$scope.saceAdmin=function(){
		
		if($scope.admin.id!==undefined){
			Admin.update($scope,admin,function(){
				$scope.admins=Admin.query();
				$scope.admin={};
				$scope.buttonText="Sumbit;"
			});
		}else
			{
			Admin.save($scope,admin,function(){
				$scope.admins=Admin.query();
				$scope.admin={};
			});
			}
	}
	$scope.updateAminInit=function(admin){
		$scope.buttonText="Update";
		$scope.admin=admin;
	}
	$scope.deleteAdmin=function(admin){
		admin.$delete({id:admin.id},function(){
			$scope.admins=Admin.query();
		});
	}
}