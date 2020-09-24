var app = angular.module("studentApp", ["ngRoute", "ngCookies", "ui.bootstrap"]);

app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "studentPortal/home.html",
        controller : "studentController"
    })
    .when("/assessments", {
        templateUrl : "studentPortal/assessments.html",
        controller : "studentController"
    })
    .when("/quiz", {
        templateUrl : "studentPortal/quiz.html",
        controller : "studentController"
    })
    .when("/quiz/result", {
        templateUrl : "studentPortal/quiz_result.html",
        controller : "studentController"
    })
    .when("/results", {
    	templateUrl: "studentPortal/results.html",
    	controller: "studentController"
    })
    .when("/result/details", {
    	templateUrl: "studentPortal/result_details.html",
    	controller: "studentController"
    })
    .when("/scores", {
        templateUrl : "studentPortal/scores.html",
        controller : "studentController"
    })
    .when("/profile", {
        templateUrl : "studentPortal/profile.html",
        controller : "studentController"
    })
    .when("/logout", {
        templateUrl : "studentPortal/logout.html",
        controller : "studentController"
    })
    .when("/help", {
        templateUrl : "studentPortal/help.html",
        controller : "studentController"
    })
    .when("/logout_error", {
        templateUrl : "studentPortal/logout_error.html",
        controller : "studentController"
    })
    .when("/error/unknown", {
    	templateUrl: "studentPortal/unknown_error.html",
    	controller: "studentController"
    });
});

app.controller('studentController' , function ($scope, $rootScope, $http, $location, $window, $filter, $cookies, $interval, $uibModal) {
	
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
					"userType": "student",
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
			"userType": "student",
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
				"userType": "student",
				"email": cookie.email
			};
		console.log("Getting current Student profile.");
		try {
			$http.post("http://localhost:8080/edugreat/service/student/get/email", token)
				.then(function successCallback(response) {
					if (response.data.status == "INVALID") {
						console.log(response.data);
						$window.location.href = '#!error/unknown'
					}
					else {
						$rootScope.myStudent = response.data.object;
						$rootScope.myStudent.dob = new Date(response.data.object.dob);
						var prof = {
							"id": $rootScope.myStudent.id
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
	
	$scope.tableOneOrder = 'assessment.id';
	$scope.tableTwoOrder = 'assessment.id';
	$scope.orderByMe = function(order, table) {
		if (table == 1) {
			$scope.tableOneOrder = order;
		} else if (table == 2) {
			$scope.tableTwoOrder = order;
		} else {
			$scope.myOrder = order;
		}
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
	
	//view profile
	$scope.updateProfile = function(details) {
		console.log("Putting new student " + details.id + " info.");
        $http.put("http://localhost:8080/edugreat/service/student/update", details)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		$anchorScroll("main_profile");
            		if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Student does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured. Profile was not updated.";
					}
            	}
            	else {
            		$scope.successMessage = "Profile updated.";
            		$rootScope.myStudent = response.data.object;
            		$rootScope.myStudent.dob = new Date(response.data.object.dob);
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
		$http.post("http://localhost:8080/edugreat/service/student/change/password", data)
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
	
	$scope.getStudentAssessments = function() {
		console.log("Getting students assessments.");
		var cookie = JSON.parse($cookies.get("profile"));
		$http.get("http://localhost:8080/edugreat/service/enrollment/student/available/"+cookie.id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Student user does not exist.";
					} else if (response.data.message == "Could not convert object to JSON.") {
						$scope.errorMessage = "No available assessments.";
					} else {
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$rootScope.availableAssessments = response.data.object;
				}
			}, function errorCallback(response) {
				console.log("Could not get available assessments.");
				$window.location.href = '#!error/unknown'
			});
		$http.get("http://localhost:8080/edugreat/service/enrollment/student/unavailable/"+cookie.id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Object not found.") {
						$scope.errorMessage2 = "Student user does not exist.";
					} else if (response.data.message == "Could not convert object to JSON.") {
						$scope.errorMessage2 = "No completed assessments.";
					} else {
						$scope.errorMessage2 = "A database error occured.";
					}					
				} else {
					$rootScope.unavailableAssessments = response.data.object;
				}
			}, function errorCallback(response) {
				console.log("Could not get unavailable assessments.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	$scope.prepareQuiz = function(assessment_id, enrollment_id) {
		$window.location.href = '#!quiz'
		
		console.log("Loading assessment and questions.");
		$http.get("http://localhost:8080/edugreat/service/assessment/questions/"+assessment_id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log("Could not get assessment info.");
				} else {
					$rootScope.currentAssessment = response.data.object;
					$rootScope.currentQuestions = response.data.object.questions;
					$rootScope.myAnswers = new Array($rootScope.currentAssessment.qcount);
					$rootScope.thisQuestion = $rootScope.currentQuestions[0];
					
					$rootScope.totalTime = $rootScope.currentAssessment.timeLimit;
					$rootScope.timeStrDisplay = $scope.timeToStr($rootScope.totalTime);
					$rootScope.timeStr = $scope.timeToStrShort($rootScope.totalTime);
					$rootScope.quizStopped = false;
					console.log("Quiz info initialized.");
				}
			}, function errorCallback(response) {
				console.log("Could not get assessment info.");
			});
		
		console.log("Loading enrollment.");
		$http.get("http://localhost:8080/edugreat/service/enrollment/"+enrollment_id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log("Could not get enrollment info.");
				} else {
					$rootScope.currentEnrollment = response.data.object;
					console.log("Loaded enrollment");
				}
			}, function errorCallback(response) {
				console.log("Could not get enrollment info.");
			});
		
		$scope.result = "";
		$rootScope.question_index = 0;
		$rootScope.pageNum = 1;
	};
	
	$scope.modalOpen = function () {
	    $rootScope.modalInstance = $uibModal.open({
	      animation: false,
	      backdrop: 'static',
	      keyboard: false,
	      ariaLabelledBy: 'modal-title',
	      ariaDescribedBy: 'student_quizmodal_quiz',
	      templateUrl: 'studentPortal/quiz_modal.html',
	      controller: 'studentController',
	    });
	    $scope.startTimer();
	};
	
	var timer;
	$scope.startTimer = function() {
		timer = $interval(function() {
			if ($rootScope.quizStopped) {
				$interval.cancel(timer);
				console.log("Timer stopped.");
			} else {
				if ($rootScope.totalTime<=0) {
					$interval.cancel(timer);
					console.log("Timer stopped.");
					$scope.submitAssessment($rootScope.result);
				} else {
					$rootScope.totalTime = $rootScope.totalTime-1;
					$rootScope.timeStr = $scope.timeToStrShort($rootScope.totalTime);
					var percentage = ($rootScope.totalTime/$rootScope.currentAssessment.timeLimit)*100;
					$rootScope.percent = "width: "+percentage+"%;";
				}
			}
		}, 1000);
	};
	
	$scope.quizAborted = function () {
		$rootScope.quizStopped=true;
		$rootScope.modalInstance.close();
		$window.location.href = "#!assessments"
		$scope.errorMessage = "Quiz was closed before it could be finished.";
	};
	
	$scope.changePage = function(result, page) {
		$rootScope.pageNum=page;
		var data = {
				"qIndex": $rootScope.question_index,
				"answer": result
		};
		$rootScope.myAnswers[$rootScope.question_index] = data;
		$rootScope.question_index = $rootScope.pageNum-1;
		$rootScope.thisQuestion = $rootScope.currentQuestions[$rootScope.question_index];
		if ($rootScope.myAnswers[$rootScope.question_index]!=null) {
			$scope.result = $rootScope.myAnswers[$rootScope.question_index].answer;
		} 
		else {
			$scope.result = false;
		}
	};
	
	$scope.isActivePage = function(page) {
		if ($rootScope.pageNum==page) {
			return true;
		} else {
			return false;
		}
	};
	
	$scope.submitAssessment = function(result) {
		$rootScope.quizStopped=true;
		var data = {
				"qIndex": $rootScope.question_index,
				"answer": result
		};
		$rootScope.modalInstance.close();
		$rootScope.myAnswers[$rootScope.question_index] = data;
		
		$rootScope.newScore = 0;
		for (var i=0; i<$rootScope.currentAssessment.qcount; i++) {
			if ($rootScope.myAnswers[i]!==undefined) {
				if ($rootScope.myAnswers[i].answer == $rootScope.currentQuestions[i].answer) {
					$rootScope.newScore = $rootScope.newScore + 1;
				}
			}
		}
		$rootScope.timeTaken = $rootScope.currentAssessment.timeLimit - $rootScope.totalTime;
		$scope.calculateResult($rootScope.newScore, $rootScope.timeTaken);
		$window.location.href="#!quiz/result"
	};
	
	$scope.calculateResult = function(newScore, timeTaken) {
		
		$rootScope.percentage = (newScore/$rootScope.currentAssessment.qcount)*100;
		if ($rootScope.currentEnrollment.score < newScore) {
			$rootScope.currentEnrollment.score = newScore;
		}
		$rootScope.highestPercentage = ($rootScope.currentEnrollment.score/$rootScope.currentAssessment.qcount)*100;
		
		$rootScope.currentEnrollment.attempts = $rootScope.currentEnrollment.attempts - 1;
		if ($rootScope.currentEnrollment.attempts == 0) {
			var data = {
					"id": $rootScope.currentEnrollment.id,
					"score":$rootScope.currentEnrollment.score,
					"attempts": $rootScope.currentEnrollment.attempts,
					"stat": "complete"
			};
		} else {
			var data = {
					"id": $rootScope.currentEnrollment.id,
					"score":$rootScope.currentEnrollment.score,
					"attempts": $rootScope.currentEnrollment.attempts	
			};
		}
		$http.put("http://localhost:8080/edugreat/service/enrollment/update", data)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Enrollment does not exist.";
					} else {
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$rootScope.currentEnrollment = response.data.object;
					$scope.successMessage = "Latest result saved.";
				}
			}, function errorCallback(response){
				console.log("Error. Quiz result was not recorded.");
			});
		
		var resultData = {
			"timeTaken": timeTaken,
			"score": newScore,
			"enrollmentId": $rootScope.currentEnrollment.id
		};
		$http.post("http://localhost:8080/edugreat/service/result/add", resultData)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					$scope.errorMessage = "Something went wrong. Result wasn't saved.";
				} else {
					$rootScope.thisresult = response.data.object;
					$rootScope.timeStr = $scope.timeToStr($rootScope.thisresult.timeTaken);
				}
			}, function errorCallback(response) {
				console.log("Result not saved.");
			});
	};
	
	$scope.getEnrollmentResult = function(enrollment_id, score, questionCount) {
		console.log("Loading enrollment.");
		$http.get("http://localhost:8080/edugreat/service/result/enrollment/"+enrollment_id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log("Could not get enrollment info.");
				} else {
					$scope.myOrder = 'score';
					$rootScope.currentEnrollment = response.data.object;
					$rootScope.currentAssessment = response.data.object.assessment;
					$rootScope.enrollmentResults = response.data.object.results;
					$window.location.href = "#!results"
					console.log("Loaded enrollment");
				}
			}, function errorCallback(response) {
				console.log("Could not get enrollment info.");
			});
	};
	
	$scope.getResult = function(id) {
		console.log("Loading result.");
		$http.get("http://localhost:8080/edugreat/service/result/"+id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID"){
					console.log("Could not get result.");
				} else {
					$rootScope.thisresult = response.data.object;
					$rootScope.percentage = ($rootScope.thisresult.score*100)/$rootScope.currentAssessment.qcount;
					$rootScope.timeStr = $scope.timeToStr($rootScope.thisresult.timeTaken);
					$window.location.href = "#!result/details"
				}
			}, function errorCallback(response) {
				console.log("Could not get result info.");
			});
	};
	
	$scope.checkCookie();
});