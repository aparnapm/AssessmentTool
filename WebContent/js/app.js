var app = angular.module("mainApp", ["ngRoute", "ngCookies"]);

app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "index/home.html",
        controller : "indexController"
    })
    .when("/adminPortal", {
        templateUrl : "index/adminLogin.html",
        controller : "indexController"
    })
    .when("/parentPortal", {
        templateUrl : "index/parentLogin.html",
        controller : "indexController"
    })
    .when("/studentPortal", {
        templateUrl : "index/studentLogin.html",
        controller : "indexController"
    })
    .when("/register", {
        templateUrl : "index/parentRegister.html",
        controller : "indexController"
    })
    .when("/about", {
        templateUrl : "index/about.html",
        controller : "indexController"
    })
    .when("/contact", {
        templateUrl : "index/contact.html",
        controller : "indexController"
    })
    .when("/confirm/:usertype/email=:emailID/auth=:authID", {
        templateUrl : "index/confirm.html",
        controller : "indexController"
    })
    .when("/error/unknown", {
        templateUrl : "index/unknown_error.html",
        controller : "indexController"
    })
    .when("/logout_success", {
        templateUrl : "index/logout_success.html",
        controller : "indexController"
    })
    .when("/logout_error", {
        templateUrl : "index/logout_error.html",
        controller : "indexController"
    })
    .when("/no_auth", {
        templateUrl : "index/no_auth.html",
        controller : "indexController"
    });
});

app.controller('indexController' , function ($scope, $rootScope, $http, $location, $window, $filter, $cookies, $anchorScroll, $routeParams) {
	
	$scope.errorMessage=false;
	$scope.successMessage=false;
	
	$scope.initialize = function() {
		console.log("Initializing hibernate connection");
		$http.get("http://localhost:8080/edugreat/service/authorization/initialize")
			.then(function successCallback(response) {
				console.log("Initialized.");
				$scope.for_reg = response.data;
			}, function errorCallback(response) {
				console.log("Error in database initialization.");
			});
	};
	
	$scope.getEmails = function() {
		$http.get("http://localhost:8080/edugreat/service/authorization/getemails")
			.then(function successCallback(response) {
	            $rootScope.allemails = response.data.object;
	        }, function errorCallback(response) {
	            console.log("Something went wrong");
	        });
	};
	
	$scope.registerParent = function(details) {
		console.log("Registering new parent.");
		details["userType"]="parent";
		details.generate=false;
		$http.post("http://localhost:8080/edugreat/service/parent/add", details)
			.then(function successCallback(response) {
				if (response.data.status == "VALID") {
					$scope.successMessage = "Registered user! A confirmation email has been sent.";
					var userdata = response.data;
    				var emailInfo = {
    					"email": userdata.object.email,
    					"subject": "Confirm New Account",
    					"message": "To "+userdata.object.firstName+" "+userdata.object.lastName+"\n\nCongratulations, " +
    							"you have signed up for an Edugreat account! \nClick the following link to confirm your " +
    									"email: \n\nhttp://localhost:8080/edugreat/main_index.html#!/confirm/parent/email="+
    									userdata.object.email+"/auth="+userdata.auth+"\n\nPlease click the confirmation" +
    											"link and then you can log into your account.\n\n	Sincerely, " +
    											"\n\n	Edugreat"
    				};
    				$http.post("http://localhost:8080/edugreat/service/authorization/send", emailInfo)
    					.then(function successCallback(response) {
    						if (response.data.status == "INVALID") {
    							console.log("Failed to send message to parent.");
    						} else {
    							console.log("Email sent to parent!");
    						}
    					}, function errorCallback(response) {
    						console.log("Failed to send message to parent.");
    					});
					
				}
				else {
					console.log(response.data);
					$anchorScroll("parentregister");
					if (response.data.message == "JSONObject has insufficient information.") {
						$scope.errorMessage = "The email entered is already associated with another user.";
					}
					else {
						$scope.errorMessage = "A database error occured.";
					}
				}
	        }, function errorCallback(response) {
	            console.log("Something went wrong.");
	            $window.location.href='#!error/unknown'
	        });
	};
	
	$scope.confirmEmail = function() {
		console.log("Confirming email.");
		$http.get("http://localhost:8080/edugreat/service/authorization/confirm/email="+$routeParams.emailID+"/auth="+$routeParams.authID)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					$window.location.href='#!error/unknown'
				} else {
					console.log("Email confirmed! Redirecting to login...");
					if ($routeParams.usertype=="admin") {
						$window.location.href="#!adminPortal"
					} else if($routeParams.usertype=="parent"){
						$window.location.href="#!parentPortal"
					} else if($routeParams.usertype=="student") {
						$window.location.href="#!studentPortal"
					} else {
						$window.location.href="#!error/unknown"
						console.log("User type invalid.");
					}
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
	            $window.location.href='#!error/unknown'
			});
	};
	
	$scope.adminLogin = function(admindetails) {
		console.log("Logging in (admin).");
		var data = {
			"email": admindetails.email,
			"passw": admindetails.passw,
			"userType": "admin"
		};
		$http.post("http://localhost:8080/edugreat/service/authorization/log_in", data)
			.then(function successCallback(response) {
				$rootScope.validation = response.data;
				if ($rootScope.validation.status == "VALID") {
					$cookies.put("auth", JSON.stringify($rootScope.validation));
					$window.location.href = '/edugreat/admin_index.html'
				}
				else {
					$anchorScroll("adminlogin");
					console.log($rootScope.validation);
					if ($rootScope.validation.message == "User type is wrong.") {
						$scope.errorMessage = "You are trying to log into the wrong portal. Try a different user.";
					}
					else if ($rootScope.validation.message == "Session Factory uninitialized.") {
						$scope.errorMessage = "A database error occured.";
					}
					else if ($rootScope.validation.message == "Object not found.") {
						$scope.errorMessage = "This email is not in our records. Check for spelling mistakes and try again.";
					}
					else if ($rootScope.validation.message == "Password is incorrect.") {
						$scope.errorMessage = "The password you entered is incorrect.";
					} 
					else if ($rootScope.validation.message == "User is not active.") {
						$scope.errorMessage = "This account is not activated.";
					}
					else {
						$scope.errorMessage = "Something went wrong and we're not sure what. Please try again.";
					}
				}
			}, function errorCallback(response) {
				$anchorScroll("adminlogin");
				$scope.errorMessage = "Something went wrong and we're not sure what. Please try again.";
				console.log(response.data);
			});
	};
	
	$scope.parentLogin = function(parentdetails) {
		console.log("Logging in (parent).");
		var data = {
			"email": parentdetails.email,
			"passw": parentdetails.passw,
			"userType": "parent"
		};
		$http.post("http://localhost:8080/edugreat/service/authorization/log_in", data)
			.then(function successCallback(response) {
				$rootScope.validation = response.data;
				if ($rootScope.validation.status == "VALID") {
					$cookies.put("auth", JSON.stringify($rootScope.validation));
					$window.location.href = '/edugreat/parent_index.html'
				}
				else {
					$anchorScroll("parentlogin");
					console.log($rootScope.validation);
					if ($rootScope.validation.message == "User type is wrong.") {
						$scope.errorMessage = "You are trying to log into the wrong portal. Try a different user.";
					}
					else if ($rootScope.validation.message == "Session Factory uninitialized.") {
						$scope.errorMessage = "A database error occured.";
					}
					else if ($rootScope.validation.message == "Object not found.") {
						$scope.errorMessage = "This email is not in our records. Check for spelling mistakes and try again.";
					}
					else if ($rootScope.validation.message == "Password is incorrect.") {
						$scope.errorMessage = "The password you entered is incorrect.";
					} 
					else if ($rootScope.validation.message == "User is not active.") {
						$scope.errorMessage = "This account is not activated.";
					}
					else {
						$scope.errorMessage = "Something went wrong and we're not sure what. Please try again.";
					}
				}
			}, function errorCallback(response) {
				$anchorScroll("parentlogin");
				$scope.errorMessage = "Something went wrong and we're not sure what. Please try again.";
				console.log(response.data);
			});
	};
	
	$scope.studentLogin = function(studentdetails) {
		console.log("Logging in (student).");
		var data = {
			"email": studentdetails.email,
			"passw": studentdetails.passw,
			"userType": "student"
		};
		$http.post("http://localhost:8080/edugreat/service/authorization/log_in", data)
			.then(function successCallback(response) {
				$rootScope.validation = response.data;
				if ($rootScope.validation.status == "VALID") {
					$cookies.put("auth", JSON.stringify($rootScope.validation));
					$window.location.href = '/edugreat/student_index.html'
				}
				else {
					$anchorScroll("studentlogin");
					console.log($rootScope.validation);
					if ($rootScope.validation.message == "User type is wrong.") {
						$scope.errorMessage = "You are trying to log into the wrong portal. Try a different user.";
					}
					else if ($rootScope.validation.message == "Session Factory uninitialized.") {
						$scope.errorMessage = "A database error occured.";
					}
					else if ($rootScope.validation.message == "Object not found.") {
						$scope.errorMessage = "The email you entered is not valid.";
					}
					else if ($rootScope.validation.message == "Password is incorrect.") {
						$scope.errorMessage = "The password you entered is incorrect.";
					}
					else if ($rootScope.validation.message == "User is not active.") {
						$scope.errorMessage = "This account is not activated.";
					}
					else {
						$scope.errorMessage = "Something went wrong and we're not sure what. Please try again.";
					}
				}
			}, function errorCallback(response) {
				$anchorScroll("studentlogin");
				$scope.errorMessage = "Something went wrong and we're not sure what. Please try again.";
				console.log(response.data);
			});
	};
	
	$scope.contactUs = function(contact) {
		console.log("Sending info!");
		var emailInfo = {
				"email": "bhadra.chembukave@gmail.com",
				"subject": "New Website Message",
				"message": "From "+contact.name+"\n\nMessage: \n"+contact.desc+"\n\nReply email: "+contact.email
			};
			$http.post("http://localhost:8080/edugreat/service/authorization/send", emailInfo)
				.then(function successCallback(response) {
					if (response.data.status == "INVALID") {
						console.log(response.data);
						$scope.errorMessage = "Sorry! Something went wrong. Failed to send message.";
					} else {
						$window.location.href = "#!"
						$scope.successMessage = "Message sent!";
					}
				}, function errorCallback(response) {
					console.log("Something went wrong.");
		            $window.location.href='#!error/unknown'
				});
	};
	
	$scope.getLogin = function(usertype) {
		console.log("Checking for cookie auth");
		var cookieExists = $cookies.get("auth");
		
		if (cookieExists) {
			var cookie = JSON.parse($cookies.get("auth"));
			var token = {
					"auth": cookie.auth,
					"userType": usertype,
					"email": cookie.email
				};
			console.log(token);
			$http.post("http://localhost:8080/edugreat/service/authorization/check_token", token)
				.then(function successCallback(response) {
					$rootScope.validation = response.data;
					if ($rootScope.validation.status == "INVALID") {
						$scope.clearCookies();
					}
					else {
						console.log("Auth verified");
						if (usertype=="admin") {
							$window.location.href = "/edugreat/admin_index.html"
						} else if (usertype=="parent") {
							$window.location.href = "/edugreat/parent_index.html"
						} else {
							$window.location.href = "/edugreat/student_index.html"
						}
					}
				}, function errorCallback(response) {
					console.log("Something went wrong.");
					$window.location.href='#!error/unknown'
				});
		}
	};

    $scope.clearCookies = function () {
    	var authExist = $cookies.get("auth");
    	if (authExist) {
    		var cookie = JSON.parse($cookies.get("auth"));
			var token = {
					"auth": cookie.auth,
					"email": cookie.email
				};
			$http.post("http://localhost:8080/edugreat/service/authorization/log_out", token)
				.then(function successCallback(response) {
					$rootScope.validation = response.data;
					console.log($rootScope.validation);
				}, function errorCallback(response) {
					console.log("Something went wrong.");
					$window.location.href='#!error/unknown'
				});
	        $cookies.remove("auth");
    	}
    	var profExist = $cookies.get("profile");
    	if (profExist) {
    		$cookies.remove("profile");
    	}
    };
});
