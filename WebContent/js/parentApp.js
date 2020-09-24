var app = angular.module("parentApp", ["ngRoute", "ngCookies"]);

app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "parentPortal/home.html",
        controller : "parentController"
    })
    .when("/assessments", {
        templateUrl : "parentPortal/assessments.html",
        controller : "parentController"
    })
    .when("/enrollments", {
        templateUrl : "parentPortal/enrollments.html",
        controller : "parentController"
    })
    .when("/student/enrollments", {
        templateUrl : "parentPortal/student_enrollments.html",
        controller : "parentController"
    })
    .when("/enroll", {
        templateUrl : "parentPortal/enrollment_create.html",
        controller : "parentController"
    })
    .when("/results", {
        templateUrl : "parentPortal/results.html",
        controller : "parentController"
    })
    .when("/result/details", {
        templateUrl : "parentPortal/result_details.html",
        controller : "parentController"
    })
    .when("/student", {
        templateUrl : "parentPortal/students.html",
        controller : "parentController"
    })
    .when("/student/details", {
        templateUrl : "parentPortal/student_details.html",
        controller : "parentController"
    })
    .when("/register", {
        templateUrl : "parentPortal/register_student.html",
        controller : "parentController"
    })
    .when("/profile", {
        templateUrl : "parentPortal/profile.html",
        controller : "parentController"
    })
    .when("/help", {
        templateUrl : "parentPortal/help.html",
        controller : "parentController"
    })
    .when("/logout", {
        templateUrl : "parentPortal/logout.html",
        controller : "parentController"
    })
    .when("/logout_error", {
        templateUrl : "parentPortal/logout_error.html",
        controller : "parentController"
    })
    .when("/error/unknown", {
    	templateUrl: "parentPortal/unknown_error.html",
    	controller: "parentController"
    });
});

app.controller('parentController' , function ($scope, $rootScope, $http, $location, $window, $filter, $cookies, $anchorScroll) {
	
	$scope.errorMessage=false;
	$scope.errorMessage2=false;
	$scope.successMessage=false;
	
	// checks if cookie is valid. if invalid, directs to main index error.html
	$scope.checkCookie = function() {
		console.log("Checking for cookie auth");
		try {
			var cookie = JSON.parse($cookies.get("auth"));
			var token = {
					"auth": cookie.auth,
					"userType": "parent",
					"email": cookie.email
				};
			console.log(token);
			$http.post("http://localhost:8080/edugreat/service/authorization/check_token", token)
				.then(function successCallback(response) {
					$rootScope.validation = response.data;
					if ($rootScope.validation.status == "INVALID") {
						$cookies.remove("auth");
						$window.location.href = '/edugreat/main_index.html#!/logout_error'
					}
					else {
						console.log($rootScope.validation);
					}
				}, function errorCallback(response) {
					console.log("Something went wrong.");
					$cookies.remove("auth");
					$window.location.href = '/edugreat/main_index.html#!/logout_error'
				});
		} catch (Exception) {
			$window.location.href = '/edugreat/main_index.html#!/no_auth'			
		}
	};
	
	// logs out user
	$scope.logout = function() {
		console.log("Attempting to log out.");
		var cookie = JSON.parse($cookies.get("auth"));
		var token = {
			"auth": cookie.auth,
			"userType": "parent",
			"email": cookie.email
		};
		console.log(token);
		$http.post("http://localhost:8080/edugreat/service/authorization/log_out", token)
			.then(function successCallback(response) {
				$scope.logout = response.data;
				console.log($scope.logout);
				if ($scope.logout.status == "VALID") {
					$cookies.remove("auth");
					$window.location.href = '/edugreat/main_index.html#!/logout_success'
				}
				else {
					$window.location.href='#!logout_error'
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href='#!logout_error'
			});
	};
	
	// stores cookie with users id in "profile"
	$scope.profile = function() {
		var cookie = JSON.parse($cookies.get("auth"));
		token = {
				"auth": cookie.auth,
				"userType": "parent",
				"email": cookie.email
			};
		console.log("Getting current Parent profile.");
		try {
			$http.post("http://localhost:8080/edugreat/service/parent/get/email", token)
				.then(function successCallback(response) {
					if (response.data.status == "INVALID") {
						console.log(response.data);
						$window.location.href = '#!error/unknown'
					}
					else {
						$rootScope.myParent = response.data.object;
						$rootScope.myParent.dob = new Date(response.data.object.dob);
						var prof = {
							"id": $rootScope.myParent.id
						};
						$cookies.put("profile", JSON.stringify(prof));
					}
				}, function errorCallback(response) {
					console.log("Something went wrong.");
					$window.location.href = '#!error/unknown'
				});
		} catch (Exception) {
			$window.location.href = '/edugreat/main_index.html#!/no_auth'
		}
	};
	
	// navigate function
	$scope.navigate = function(newPage) {
		$window.location.href = newPage
	};
	
	$scope.myOrder = 'id';
	$scope.orderByMe = function(header) {
		$scope.myOrder = header;
	};
	
	$scope.timeToStrShort = function(time) {
		var date = new Date(0,0,0,0,0,time);
		var hours = date.getHours();
		var minutes = date.getMinutes();
		var seconds = date.getSeconds();
		var timeStr = "";
		
		if (hours < 10) {
			timeStr = timeStr + "0";
		}
		timeStr = timeStr + hours + ":";
		if (minutes < 10) {
			timeStr = timeStr + "0";
		}
		timeStr = timeStr + minutes + ":";
		if (seconds < 10) {
			timeStr = timeStr + "0";
		}
		timeStr = timeStr + seconds;
		return timeStr;
	};
	
	$scope.timeToStr = function(time) {
		var date = new Date(0, 0, 0, 0, 0, time);
		var hours = date.getHours();
		var minutes = date.getMinutes();
		var seconds = date.getSeconds();
		var timeStr = hours+"hr "+minutes+"min "+seconds+"sec";
		return timeStr;
	};
	
	// gets all assessments when assessments tab is opened (ng init)
	$scope.getAllAssessments = function() {
		console.log("All assessment details requested");
		$http.get("http://localhost:8080/edugreat/service/assessment/all")
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Could not convert object to JSON.") {
						$scope.errorMessage = "No assessments.";
					} else  {
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$rootScope.assessmentdetails = response.data.object;
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	//get all enrollments for students
	$scope.getAllEnrollments = function() {
		console.log("Getting enrollments for all students.");
		var cookie = JSON.parse($cookies.get("profile"));
		$http.get("http://localhost:8080/edugreat/service/parent/"+cookie.id+"/student/enrollments")
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					if (response.data.message == "Could not convert object to JSON.") {
						$scope.errorMessage = "No enrollments.";
					} else if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Parent user does not exist.";
					} else {
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$rootScope.enrollmentdetails = response.data.object;
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	$scope.viewStudentEnrollments = function(id) {
		console.log("Getting student enrollments");
		$http.get("http://localhost:8080/edugreat/service/enrollment/student/"+id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					$scope.errorMessage = "Error loading student enrollments";
				} else {
					$rootScope.thisstudent = response.data.object;
					$rootScope.enrollmentdetails = response.data.object.enrollments;
					$window.location.href = "#!student/enrollments"
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	$scope.viewResult = function(id) {
		console.log("Loading results");
		$http.get("http://localhost:8080/edugreat/service/result/enrollment/"+id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log("Could not get assessment info.");
				} else {
					$rootScope.thisenrollment = response.data.object;
					console.log($rootScope.thisenrollment);
					$rootScope.enrollmentResults = response.data.object.results;
					$window.location.href = "#!results"
				}
			}, function errorCallback(response) {
				console.log("Could not get assessment info.");
			});
	};
	
	$scope.getResult = function(id) {
		console.log("Getting result data");
		$http.get("http://localhost:8080/edugreat/service/result/"+id)
			.then(function successCallback(response){
				if (response.data.status == "INVALID") {
					$scope.errorMessage = "Could not get result info.";
				} else {
					$rootScope.thisresult = response.data.object;
					$rootScope.percentage = ($rootScope.thisresult.score/$rootScope.thisresult.enrollment.assessment.qcount)*100;
					$rootScope.timeStr = $scope.timeToStr($rootScope.thisresult.timeTaken);
					$window.location.href = "#!result/details"
				}
			}, function errorCallback(response) {
				
			});
	};
	
	//initialize enrollment data
	$scope.initEnrollment = function() {
		console.log("Getting enrollment data");
		$http.get("http://localhost:8080/edugreat/service/assessment/all")
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Could not convert object to JSON.") {
						$scope.errorMessage = "No assessments.";
					} else  {
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$rootScope.assessmentdetails = response.data.object;					
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
		var cookie = JSON.parse($cookies.get("profile"));
        $http.get("http://localhost:8080/edugreat/service/student/pid/"+cookie.id)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		if (response.data.message == "Could not convert object to JSON.") {
            			$scope.errorMessage = "You don't have any students.";
            		} else if (response.data.message == "Parent object not found.") {
            			$scope.errorMessage = "Parent user does not exist.";
            		} else {
            			$scope.errorMessage = "A database error occured."
            		}
            	} else {
            		$rootScope.studentdetails = response.data.object;
            	}
            }, function errorCallback(response){
                console.log("Something went wrong.");
                $window.location.href = '#!error/unknown'
            });
        $window.location.href = '#!enroll'
	};
	
	//new enrollment (add student id if its is from that page)
	$scope.addEnrollment = function(details) {
		console.log("Posting new enrollment details");
		var data = {
				"assessmentId": details.assessment.id,
				"studentId": details.student.id,
				"validFrom": details.validFrom,
				"validUntil": details.validUntil,
				"stat": "requested",
			};
		$http.post("http://localhost:8080/edugreat/service/enrollment/add", data)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					$anchorScroll("parent_enrollmentcreate_createForm");
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Either Assessment or Student does not exist.";
					} else {
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$window.location.href = '#!enrollments'
					$scope.successMessage = "Enrollment request sent.";
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// gets all student on opening student home (ng-init)
    $scope.getAllStudents = function() {
    	console.log("Getting parent's students.");
		var cookie = JSON.parse($cookies.get("profile"));
        $http.get("http://localhost:8080/edugreat/service/student/pid/"+cookie.id)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		if (response.data.message == "Could not convert object to JSON.") {
            			$scope.errorMessage = "You don't have any students.";
            		} else if (response.data.message == "Parent object not found.") {
            			$scope.errorMessage = "Parent user does not exist.";
            		} else {
            			$scope.errorMessage = "A database error occured."
            		}
            	} else {
            		$rootScope.studentdetails = response.data.object;
            	}
            }, function errorCallback(response){
                console.log("Something went wrong.");
                $window.location.href = '#!error/unknown'
            });        
    };
    
    // gets specific student details
    $scope.editStudent = function(id) {
        console.log("Getting student " + id);
        $http.get("http://localhost:8080/edugreat/service/student/"+id)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		$anchorScroll("parent_students_allTable");
            		if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Student does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured.";
					}
            	}
            	else {
            		$window.location.href = '#!student/details'
            		$rootScope.thisstudent = response.data.object;
            		$rootScope.thisstudent.dob = new Date($rootScope.thisstudent.dob);
            	}
            }, function errorCallback(response) {
                console.log("Something went wrong");
                $window.location.href = '#!error/unknown'
            });
    };
    
    // puts new student details to database and redirects to student home
    $scope.updateStudent = function(details) {
        console.log("Putting new student " + details.id + " info.");
        $http.put("http://localhost:8080/edugreat/service/student/update", details)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		$anchorScroll("parent_studentedit_editForm");
            		if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Student does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured. Student was not updated.";
					}
            	}
            	else {
            		$window.location.href = '#!student'
            		$scope.successMessage = "Student updated.";
            	}
            }, function errorCallback(response) {
                console.log("Something went wrong");
                $window.location.href = '#!error/unknown'
            });
    };
    
    // registers/adds new parent and redirects to parent home
    $scope.addStudent = function(details) {
    	console.log("Creating student");
    	var cookie = JSON.parse($cookies.get("profile"));
    	details.userType = "student";
    	details.p_id = cookie.id;
    	details.generate = true;
        $http.post("http://localhost:8080/edugreat/service/student/add", details)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
    				console.log(response.data);
    				$anchorScroll("parent_studentregister_createForm");
    				$scope.errorMessage = "A database error occured. Student could not be registered.";
    			}
    			else {
    				$window.location.href='#!student'
    				$scope.successMessage = "Student registered.";
    				console.log(response.data);
    				var userdata = response.data;
    				var emailInfo = {
    					"email": userdata.object.email,
    					"subject": "New Student Account",
    					"message": "To "+userdata.object.firstName+" "+userdata.object.lastName+"\n\nYour parent has " +
						"signed you up for an Edugreat account!" + "\nYour account has been given a default " +
								"password. \nPassword is: \n\n"+userdata.passw+"\n\nPlease click the confirmation" +
										"link to confirm your email:" +
										"\n\nhttp://localhost:8080/edugreat/main_index.html#!/confirm/student/email="+
								userdata.object.email+"/auth="+userdata.auth+"\n After confirming your email" +
										"you can login into your account on our website to change your password." +
										"\n\n	Sincerely,\n\n	Edugreat"
    				};
    				$http.post("http://localhost:8080/edugreat/service/authorization/send", emailInfo)
    					.then(function successCallback(response) {
    						if (response.data.status == "INVALID") {
    							console.log("Failed to send message to student.");
    						} else {
    							console.log("Email sent to student!");
    						}
    					}, function errorCallback(response) {
    						console.log("Failed to send message to student.");
    					});
    			}
            }, function errorCallback(response) {
                console.log("Something went wrong");
                $window.location.href = '#!error/unknown'
            });
    };
    
    //view profile
	$scope.updateProfile = function(details) {
		console.log("Putting new parent " + details.id + " info.");
        $http.put("http://localhost:8080/edugreat/service/parent/update", details)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		$anchorScroll("main_profile");
            		if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Parent does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured. Profile was not updated.";
					}
            	}
            	else {
            		$scope.successMessage = "Profile updated.";
            		$rootScope.myParent = response.data.object;
            		$rootScope.myParent.dob = new Date(response.data.object.dob);
            	}
            }, function errorCallback(response) {
                console.log("Something went wrong");
                $window.location.href = '#!error/unknown'
            });
	};
	
	$scope.changePassword = function(details) {
		console.log("Posting to change password");
		var cookie = JSON.parse($cookies.get("auth"));
		var data = {
			"email": cookie.email,
			"oldPassw": details.oldPassw,
			"passw": details.newPassw
		};
		$http.post("http://localhost:8080/edugreat/service/parent/change/password", data)
			.then(function successCallback(response) {
				$scope.errorMessage = false;
				$scope.successMessage = false;
				if (response.data.status == "INVALID") {
					console.log(response.data);
					$anchorScroll("edit_pass");
					$scope.errorMessage = "Either the old password was incorrect or a database error occured.";
				}
				else {
					$anchorScroll("edit_pass");
					$scope.successMessage = "Password updated.";
				}
			}, function errorCallback(response) {
				console.log("Could not change password.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// refresh cookies on every page to make sure user is still authorized
    $scope.checkCookie();
});