angular.module("myApp").factory("Admin",Admin);

Admin.$inject=["$resource"];

function Admin($resource){
	var resourceUrl="api/admin/:id";
	return $resource(resourceUral, {},{
		'update':{
			method:'PUT'
		}
	});
}