    var app = angular.module("myApp", ["ngRoute"]);
/*    .service('sharedId', function () {
        var id = '';

        return {
            getId: function () {
                return id;
            },
            setId: function(value) {
                id = value;
            }
        };
    });
*/
    app.controller("myController", function($scope ,$rootScope, $http, $window, $location) {
    	$scope.go = function ( path ) {
    		  $location.path( path );
    		};
    		
    	
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
            	$rootScope.assessmentdetails=response.data;
            	console.log($rootScope.assessmentdetails);
                //$rootScope.details = response.data;
            }, function errorCallback(response){
                console.log("Unable to perform get request");
            });
    		var assessmentquestion = function (id) {
    			$http.get ("http://localhost:8080/edugreat/service/assessment/questions/"+id)
    			    .then(function successCallback(response){
    			        $rootScope.quesdetails = response.data;
    			    }, function errorCallback(response){
    			        console.log("Unable to perform get request");
    			    });
    			     //console.log($rootScope.quesdetails);
    			}	
    		assessmentquestion(id);
    		$rootScope.assessment_id = id;

    };

        $scope.delete_q_from_a =function(q_id, a_id){
        	/*	$http.get ("http://localhost:8080/edugreat/service/question/"+id)
        		.then(function successCallback(response){
        			$rootScope.question= response.data;
        		});*/
        		$http({
        			method: 'DELETE',
        			url:" http://localhost:8080/edugreat/service/assessment/delete/question" , 
        			data:
                    {
                        question_id: q_id,
                        assessment_id: a_id
                    }
                })
        	//$http.delete ("http://localhost:8080/edugreat/service/assessment/delete/question" , $rootScope.question)
          .then(function successCallback(response){
             $window.alert("Question deleted");
         }, function errorCallback(response){
             $window.alert("Unable to delete question");
         });
      };

  /*    $scope.gotocreateassessment = function () {
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
    };*/
  /*  $scope.createassessment=function(newdetails){
       
       $http.post("http://localhost:8080/edugreat/service/assessment/add", newdetails)
       .then(function successCallback(response){
        $rootScope.newdetails = response.data;
    }, function errorCallback(response){
        console.log("Unable to perform get request");
    });
       $window.location.href="#!assessment"
    };*/

    $scope.update=function(assessmentdetails){
       
       $http.put("http://localhost:8080/edugreat/service/assessment/update", assessmentdetails)
       .then(function successCallback(response){
        $rootScope.assessmentdetails = response.data;
    }, function errorCallback(response){
        console.log("Unable to perform get request");
    });
       $window.location.href="#!assessment"
    };
/*    $scope.registerparent = function () {
       $window.location.href="#!Parentdetails/registerp"
       $rootScope.pdetails ={
         firstName:"",
         lastName:"",
         email:"",
         phone:"",
         passw:"",
         location:"",
         dob:""
     }	
    };
    $scope.registerp=function(pdetails){
       
       $http.post("http://localhost:8080/edugreat/service/parent/add", pdetails)
       .then(function successCallback(response){
        $rootScope.pdetails = response.data;
    }, function errorCallback(response){
        console.log("Unable to perform get request");
    });
       $window.location.href="#!Parentdetails"
    };
*/


    });
    app.controller("parentController",function($scope ,$http){
    	console.log("inside register");
    	$scope.cc= this;
    	$scope.cc.user={};

    	$scope.cc.create=function(){
    		console.log("submitted");
    	if($scope.cc.user.passw!==$scope.cc.user.confirmpassw){
    		alert("password mismatch")
    	}
    	else{
    		  $http({'method': 'POST',
    		         'url':"http://localhost:8080/edugreat/service/parent/add" , 
    		         'data': $scope.cc.user })
    		
    		   .then(function successCallback(response){
    		       alert("registered successfully");
    		    }, function errorCallback(response){
    		        console.log("Unable to add");
    		    });
    		}
    	}
    });
    app.controller("assessmentCntrl",function($scope ,$http ){
    	$scope.assessment= this;
    	$scope.assessment.test={};
    	$scope.assessment.create=function(){
        $http({'method': 'POST',
    		         'url':"http://localhost:8080/edugreat/service/assessment/add" , 
    		         'data': $scope.assessment.test })
    		
    		   .then(function successCallback(response){
    		       alert("assessment created successfully");
    		    }, function errorCallback(response){
    		        console.log("Unable to create assessment");
    		    });
    	}
    	$scope.deleteassessment = function (id) {
    	    	console.log(id);
    	      $http({
    	         'method': 'DELETE',
    	         'url':"http://localhost:8080/edugreat/service/assessment/delete/"+id  
    	    })
    	      .then(function successCallback(response){
    	            alert("Assessment deleted");
    	     }, function errorCallback(response){
    	         alert("Unable to delete assessment");
    	     });
    	    };
    	
    });
    app.controller("StudentCntrl",function($scope ,$http , $window ,$rootScope){
    	
    	$scope.deleteStudent =function(id){
        $http({'method': 'DELETE',
    		         'url':"http://localhost:8080/edugreat/service/student/delete/"+id , 
    		         })
    		   .then(function successCallback(response){
    		       alert("student deleted successfully");
    		    }, function errorCallback(response){
    		        console.log("Unable to delete student");
    		    });
    	}
    	$scope.viewStudent =function(id){
    		 $window.location.href="#!Studentdetails/viewstudent"
            $http({'method': 'GET',
        		         'url':"http://localhost:8080/edugreat/service/student/"+id , 
        		         })
        		   .then(function successCallback(response){
        		      $rootScope.studentdetails=response.data;
        		      console.log(studentdetails);
        		    }, function errorCallback(response1){
        		        console.log("Unable to get student basic details");
        		    });
            var getStudentEnrollment=function(id){
            $http({'method': 'GET',
		         'url':"http://localhost:8080/edugreat/service/enrollment/student_id/"+ id, 
		         })
		   .then(function successCallback(response){
		      $rootScope.studentenrollments=response.data;
		    }, function errorCallback(response){
		        console.log("Unable to get student enrollement details");
		    });
           
        	}
            getStudentEnrollment(id);
            }
    	$scope.unenrollStudent = function(id){
    		console.log(id);
  	      $http({
  	         'method': 'DELETE',
  	         'url':"http://localhost:8080/edugreat/service/enrollment/delete/"+id  
  	    })
  	      .then(function successCallback(response){
  	            alert("Student unenrolled");
  	     }, function errorCallback(response){
  	         alert("Unable to unenroll");
  	     });
  	    };
    	  $scope.updateStudent=function(studentdetails){
    	       
    	       $http.put("http://localhost:8080/edugreat/service/student/update", studentdetails)
    	       .then(function successCallback(response){
    	        $rootScope.studentdetails = response.data;
    	        alert("Updated")
    	    }, function errorCallback(response){
    	        console.log("Unable to perform get request");
    	    });
    	    };
    });
        app.config(function($routeProvider) {
            $routeProvider
            .when("/home", {
                templateUrl : "adminhome.html",
                controller:"myController"
            })
            .when("/assessment", {
                templateUrl : "assessments.html",
                	 controller:"myController"
            })
            .when("/questions", {
                templateUrl : "questions.html"
                	,
               	 controller:"myController"
            })
            
            .when("/requests", {
                templateUrl :"requests.html"
                	,
               	 controller:"myController"
            })
            .when("/Admindetails", {
                templateUrl :"Admindetails.html"
                	,
               	 controller:"myController"
            })
            .when("/Studentdetails", {
                templateUrl :"Studentdetails.html"
                	,
               	 controller:"myController"
            })
            .when("/Parentdetails", {
                templateUrl :"Parentdetails.html"
                	,
               	 controller:"myController"
            })
            .when("/profile", {
                templateUrl :"profile.html"
                	,
               	 controller:"myController"
            })	
              .when("/assessment/new", {
              templateUrl :"createassessment.html",
             	 controller:"assessmentCntrl"
            })
            .when("/assessment/view", {
                templateUrl :"viewassessment.html"
                	,
               	 controller:"myController"
            })
           .when("/Parentdetails/registerp", {
              templateUrl :"registerp.html"
            	  ,
             	 controller:"parentController"
            })
              .when("/question/new", {
              templateUrl :"createquestion.html",
             	 controller:"assessmentCntrl"
            })
              .when("/assessment/edit", {
              templateUrl :"editassessment.html",
             	 controller:"assessmentCntrl"
            })
             .when("/Studentdetails/viewstudent", {
              templateUrl :"viewstudent.html",
             	 controller:"StudentCntrl"
            })
          
        });

