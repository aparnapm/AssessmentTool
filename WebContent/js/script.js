var testApp = angular.module('testApp', []);

testApp.controller('testController' , function ($scope, $http) {
    $scope.aparna = "This is the homepage";
    
    $scope.getRequest = function () {
        console.log("I've been pressed!");  

        $http.get("http://localhost:8080/edugreat/service/admin/2")
            .then(function successCallback(response){
                $scope.response = response.data;
            }, function errorCallback(response){
                console.log("Unable to perform get request");
            });
    };
    
});
 