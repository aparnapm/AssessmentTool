var app = angular.module("myApp", ["ngRoute"]);

app.controller("myController", function($scope ,$rootScope, $http, $window, $location) {
	$scope.getAssessment = function () {

        $http.get("http://localhost:8080/edugreat/service/assessment/all")
            .then(function successCallback(response){
                $scope.response = response.data;
            }, function errorCallback(response){
                console.log("Unable to perform get request");
            });
    };
   
    $scope.getAdmin = function () {

        $http.get("http://localhost:8080/edugreat/service/admin/all")
            .then(function successCallback(response){
                $scope.response = response.data;
            }, function errorCallback(response){
                console.log("Unable to perform get request");
            });
    };
    
    $scope.getParent= function () {

        $http.get("http://localhost:8080/edugreat/service/parent/all")
            .then(function successCallback(response){
                $scope.response = response.data;
            }, function errorCallback(response){
                console.log("Unable to perform get request");
            });
    };
    $scope.getStudent = function () {

        $http.get("http://localhost:8080/edugreat/service/student/all")
            .then(function successCallback(response){
                $scope.response = response.data;
            }, function errorCallback(response){
                console.log("Unable to perform get request");
            });
    };
    $scope.getQuestions= function () {

        $http.get("http://localhost:8080/edugreat/service/question/all")
            .then(function successCallback(response){
                $scope.response = response.data;
            }, function errorCallback(response){
                console.log("Unable to perform get request");
            });
    };

	$scope.viewassessment = function (id) {
		$window.location.href="#!assessment/view"
    $http.get ("http://localhost:8080/edugreat/service/assessment/"+id)
        .then(function successCallback(response){
            $rootScope.details = response.data;
        }, function errorCallback(response){
            console.log("Unable to perform get request");
        });
};
$scope.gotocreateassessment = function () {
	$window.location.href="#!assessment/create"
	$rootScope.newdetails ={
			id:0,
			assessmentName:"",
			description:"",
			category:"",
			qcount:"",
			attempts:"",
			createdby:"",
			difficulty:"",
	}	
};
$scope.createassessment=function(newdetails){
	
	$http.post("http://localhost:8080/edugreat/service/assessment/add", newdetails)
    .then(function successCallback(response){
        $rootScope.newdetails = response.data;
    }, function errorCallback(response){
        console.log("Unable to perform get request");
    });
	$window.location.href="#!assessment"
};
    $scope.update=function(details){
    	
    	$http.put("http://localhost:8080/edugreat/service/assessment/update", details)
        .then(function successCallback(response){
            $rootScope.details = response.data;
        }, function errorCallback(response){
            console.log("Unable to perform get request");
        });
    	$window.location.href="#!assessment"
    };
});

app.config(function($routeProvider) {
    $routeProvider
    .when("/home", {
        templateUrl : "adminhome.html"
    })
    .when("/assessment", {
        templateUrl : "assessments.html"
    })
    .when("/questions", {
        templateUrl : "questions.html"
    })
    .when("/registerp", {
        templateUrl :"registerp.html"
    })
    .when("/requests", {
        templateUrl :"requests.html"
    })
     .when("/Admindetails", {
        templateUrl :"Admindetails.html"
    })
    .when("/Studentdetails", {
        templateUrl :"Studentdetails.html"
    })
    .when("/Parentdetails", {
        templateUrl :"Parentdetails.html"
    })
    .when("/profile", {
        templateUrl :"profile.html"
    })	
    .when("/assessment/view", {
        templateUrl :"viewassessment.html"
    })
    .when("/assessment/create", {
        templateUrl :"createAssessment.html"
    })
    });

