var app = angular.module("adminApp", ["ngRoute", "ngCookies"]);

// route provider
app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "adminPortal/home.html",
        controller : "adminController"
    })
    .when("/assessments", {
        templateUrl : "adminPortal/assessments.html",
        controller : "adminController"
    })
    .when("/assessment/details", {
        templateUrl : "adminPortal/assessment_details.html",
        controller : "adminController"
    })
    .when("/assessment/edit", {
        templateUrl : "adminPortal/assessment_edit.html",
        controller : "adminController"
    })
    .when("/assessment/create", {
        templateUrl : "adminPortal/assessment_create.html",
        controller : "adminController"
    })
    .when("/assessment/addquestion", {
        templateUrl : "adminPortal/assessment_addquestion.html",
        controller : "adminController"
    })
    .when("/assessment/enrollments", {
    	templateUrl : "adminPortal/assessment_enrollments.html",
    	controller : "adminController"
    })
    .when("/questions", {
        templateUrl : "adminPortal/questions.html",
        controller : "adminController"
    })
    .when("/question/edit", {
        templateUrl : "adminPortal/question_edit.html",
        controller : "adminController"
    })
    .when("/question/create", {
        templateUrl : "adminPortal/question_create.html",
        controller : "adminController"
    })
    .when("/enrollments", {
        templateUrl : "adminPortal/enrollments.html",
        controller : "adminController"
    })
    .when("/enrollment/details", {
        templateUrl : "adminPortal/enrollment_details.html",
        controller : "adminController"
    })
    .when("/enrollment/create", {
        templateUrl : "adminPortal/enrollment_create.html",
        controller : "adminController"
    })
    .when("/result/details", {
        templateUrl : "adminPortal/result_details.html",
        controller : "adminController"
    })
    .when("/admin", {
        templateUrl : "adminPortal/admins.html",
        controller : "adminController"
    })
    .when("/parent", {
        templateUrl : "adminPortal/parents.html",
        controller : "adminController"
    })
    .when("/parent/details", {
        templateUrl : "adminPortal/parent_details.html",
        controller : "adminController"
    })
    .when("/parent/register", {
        templateUrl : "adminPortal/register_parent.html",
        controller : "adminController"
    })
    .when("/student", {
        templateUrl : "adminPortal/students.html",
        controller : "adminController"
    })
    .when("/student/details", {
        templateUrl : "adminPortal/student_details.html",
        controller : "adminController"
    })
    .when("/student/enrollments", {
    	templateUrl : "adminPortal/student_enrollments.html",
    	controller : "adminController"
    })
    .when("/logout", {
        templateUrl : "adminPortal/logout.html",
        controller : "adminController"
    })
    .when("/profile", {
        templateUrl : "adminPortal/profile.html",
        controller : "adminController"
    })
    .when("/error/unknown", {
        templateUrl : "adminPortal/unknown_error.html",
        controller : "adminController"
    })
    .when("/help", {
        templateUrl : "adminPortal/help.html",
        controller : "adminController"
    })
    .when("/logout_error", {
        templateUrl : "adminPortal/logout_error.html",
        controller : "adminController"
    });
});

// controller adminController
app.controller('adminController' , function ($scope, $rootScope, $http, $location, $window, $filter, $cookies, $anchorScroll) {
	
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
					"userType": "admin",
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
			"userType": "admin",
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
				"userType": "admin",
				"email": cookie.email
			};
		console.log("Getting current Admin profile.");
		try {
			$http.post("http://localhost:8080/edugreat/service/admin/get/email", token)
				.then(function successCallback(response) {
					if (response.data.status == "INVALID") {
						console.log(response.data);
						$window.location.href = '#!error/unknown'
					}
					else {
						$rootScope.myAdmin = response.data.object;
						$rootScope.myAdmin.dob = new Date(response.data.object.dob);
						var prof = {
							"id": $rootScope.myAdmin.id
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
	
	// gets details for specific assessment by id and stores in thisassessment then opens details page
	$scope.viewAssessment = function(id) {
		console.log("Getting details for Assessment "+id);
		$http.get("http://localhost:8080/edugreat/service/assessment/questions/"+id)
			.then(function successCallback(response) {
				$window.location.href='#!assessment/details'
				if (response.data.status == "INVALID") {
					console.log(response.data);
					$anchorScroll("admin_assessmentdetails_detailTable");
					if (response.data.message == "Error in generating questions.") {
						$scope.errorMessage2 = "Questions could not be found.";
					} else if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Assessment does not exist.";
					} else {
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$rootScope.thisassessment = response.data.object;
					$rootScope.assessmentquestions = response.data.object.questions;
					$rootScope.timeStr = $scope.timeToStr($rootScope.thisassessment.timeLimit);
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// gets details for specific assessment by id and then opens edit page
	$scope.editAssessment = function(id) {
		console.log("Getting details for Assessment "+id);
		$http.get("http://localhost:8080/edugreat/service/assessment/"+id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					$anchorScroll("admin_assessments_allTable");
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Assessment does not exist.";
					} else {
						$scope.errorMessage = "A database error occured.";						
					}
				} else {
					$window.location.href='#!assessment/edit'
					$rootScope.thisassessment = response.data.object;	
					$rootScope.thisassessment.createdDate = new Date($rootScope.thisassessment.createdDate);
					var date = new Date(0,0,0,0,0,$rootScope.thisassessment.timeLimit);
					$rootScope.thisassessment.timeHours = date.getHours();
					$rootScope.thisassessment.timeMinutes = date.getMinutes();
					$rootScope.thisassessment.timeSeconds = date.getSeconds();
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// puts the new details of assessment to database and then redirects to assessments home
	$scope.updateAssessment = function(details) {
		console.log("Putting new assessment " + details.id + " info.");
		var newTime = (details["timeHours"]*3600)+(details["timeMinutes"]*60)+details["timeSeconds"];
		details["timeLimit"] = newTime;
        $http.put("http://localhost:8080/edugreat/service/assessment/update", details)
            .then(function successCallback(response) {
            	$anchorScroll("admin_assessmentedit_editForm");
                if (response.data.status == "INVALID") {
                	console.log(response.data);
                	if (response.data.message == "Object not found.") {
                		$scope.errorMessage = "Assessment does not exist.";
					} else {
						$scope.errorMessage = "A database error occured. Could not update Assessment.";						
					}
                } else {
                	$scope.successMessage = "Assessment updated.";
                	$rootScope.thisassessment = response.data.object;
                	var date = new Date(0,0,0,0,0,$rootScope.thisassessment.timeLimit);
					$rootScope.thisassessment.timeHours = date.getHours();
					$rootScope.thisassessment.timeMinutes = date.getMinutes();
					$rootScope.thisassessment.timeSeconds = date.getSeconds();
                }
            }, function errorCallback(response) {
                console.log("Something went wrong");
                $window.location.href = '#!error/unknown'
            });
	};
	
	// deletes assessment and redirects to assessments home
	$scope.deleteAssessment = function(assessment_id) {
		console.log("Deleting assessment " + assessment_id);
    	$http({
    	    method: 'DELETE',
    	    url: 'http://localhost:8080/edugreat/service/assessment/delete',
    	    data: {
    	        id: assessment_id
    	    }
    	})
    		.then(function successCallback(response) {
    			if (response.data.status == "INVALID") {
                	console.log(response.data);
                	$anchorScroll("admin_assessmentedit_editForm");
                	if (response.data.message == "Object not found.") {
                		$scope.errorMessage = "Assessment does not exist.";
					} else {
						$scope.errorMessage = "A database error occured. Could not delete Assessment.";						
					}
                } else {
                	$window.location.href='#!assessments'
                	$scope.successMessage = "Assessment was successfully deleted!";
                }
    		}, function errorCallback(response) {
    			console.log("Something went wrong");
    			$window.location.href = '#!error/unknown'
    		});
	};
	
	// posts new assessment info to database and redirects to assessments home
	$scope.addAssessment = function(details) {
		console.log("Creating new assessment");
		var prof = JSON.parse($cookies.get("profile"));
		details["createdby"] = prof.id;
		var newTime = (details["timeHours"]*3600)+(details["timeMinutes"]*60)+details["timeSeconds"];
		details["timeLimit"] = newTime;
		$http.post("http://localhost:8080/edugreat/service/assessment/add", details)
			.then(function successCallback(response) {
				console.log(response.data);
				$anchorScroll("admin_assessmentcreate_createForm");
				if (response.data.status == "INVALID") {
					if (response.data.message == "Incorrect data.") {
						$scope.errorMessage = "The information entered is invalid.";
					} else {
						$scope.errorMessage = "A database error occured. Could not create Assessment.";
					}
				} else {
					$window.location.href='#!assessments'
					$scope.successMessage = "Assessment was added.";
				}
			}, function errorCallback(response) {
				console.log("Something went wrong");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// deletes question from assessment (in details page)
	$scope.deleteQuestionFromAssessment = function(assessment_id, question_id, question) {
		console.log("Deleing question "+question_id+" from assessment "+assessment_id);
		$http({
			method: 'DELETE',
			url: 'http://localhost:8080/edugreat/service/assessment/delete/question',
			data: {
				assessmentId: assessment_id,
				questionId: question_id
			}
		})
			.then(function successCallback(response) {
				$window.location.href='#!assessment/details'
				console.log(response.data);
				$anchorScroll("admin_assessmentdetails_questionsTable");
				if (response.data.status == "INVALID") {
					if (response.data.message == "Object not found.") {
						$scope.errorMessage2 = "Assessment or Question does not exist.";
					} else {
						$scope.errorMessage2 = "A database error occured. Could not delete question from this assessment.";
					}
				} else {
					$scope.successMessage = "Question was deleted.";
					$rootScope.assessmentquestions.splice( $rootScope.assessmentquestions.indexOf(question), 1 );
					$rootScope.assessmentquestions = response.data.object.questions;
					$rootScope.thisassessment = response.data.object;
					$rootScope.timeStr = $scope.timeToStr($rootScope.thisassessment.timeLimit);
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// gets all questions on open of questions home
	$scope.getAllQuestions = function() {
		console.log("Getting all questions");
		$http.get("http://localhost:8080/edugreat/service/question/all")
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Could not convert object to JSON.") {
						$scope.errorMessage = "No questions.";
					} else{
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$rootScope.questiondetails = response.data.object;
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// adds question to assessment by putting info to database
	$scope.addQuestionToAssessment = function(assessment_id, question_id) {
		console.log("Adding question "+question_id+" to assessment "+assessment_id);
		var data = {
				"assessmentId": assessment_id,
				"questionId": question_id
		};
		console.log(data);
		$http.put("http://localhost:8080/edugreat/service/assessment/add/question", data)
			.then(function successCallback(response) {
				$window.location.href = '#!assessment/addquestion'
				if (response.data.status == "INVALID") {
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Assessment or Question does not exist.";
					} else if (response.data.message == "Could not convert object to JSON.") {
						$scope.errorMessage = "No questions.";
					} else {
						$scope.errorMessage = "A database error occured. Could not add question to this assessment.";
					}
				} else {
					$scope.successMessage = "Question added to Assessment.";
					$rootScope.assessmentquestions = response.data.object.questions;
					$rootScope.thisassessment = response.data.object;
					$rootScope.timeStr = $scope.timeToStr($rootScope.thisassessment.timeLimit);
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	$scope.checkIfAdded = function(object) {
		var ifExist = false;
        angular.forEach($rootScope.assessmentquestions, function (question) {
            if (question.id == object.id) {
            	ifExist = true;
            }
        });
        return ifExist;
	}
	
	// gets all enrollments for specific assessment and goes to assessment_enrollments page
	$scope.viewAssessmentEnrollments = function(id) {
		console.log("Getting enrollments for Assessment "+id);
		$http.get("http://localhost:8080/edugreat/service/enrollment/assessment/"+id)
			.then(function successCallback(response) {
				$window.location.href='#!assessment/enrollments'
				$anchorScroll("admin_assessmentenrollments_allTable");
				if (response.data.status == "INVALID") {
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Assessment does not exist.";
					} else if (response.data.message == "Could not convert object to JSON.") {
						$scope.errorMessage = "No enrollments for this assessment.";
					} else {
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$rootScope.enrollmentdetails = response.data.object.enrollments;
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// gets all student enrollments
	$scope.viewStudentEnrollments = function(id) {
		console.log("Getting enrollments for Student "+id);
		$http.get("http://localhost:8080/edugreat/service/enrollment/student/"+id)
			.then(function successCallback(response) {
				$window.location.href='#!student/enrollments'
				$anchorScroll("admin_studentenrollments_allTable");
				if (response.data.status == "INVALID") {
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Student does not exist.";
					} else if (response.data.message == "Could not convert object to JSON.") {
						$scope.errorMessage = "No enrollments for this student.";
					} else {
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$rootScope.enrollmentdetails = response.data.object.enrollments;
				}
			}, function errorCallback(response) {
				console.log("Something went wrong.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// gets specific question by id and goes to edit page
	$scope.editQuestion = function(id) {
		console.log("Getting Question "+id+" details");
		$http.get("http://localhost:8080/edugreat/service/question/"+id)
			.then(function successCallback(response) {
				$anchorScroll("admin_questions_allTable");
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Question does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured.";
					}
				}
				else {
					$rootScope.thisquestion = response.data.object;
					$window.location.href='#!question/edit'
				}
			}, function errorCallback(response) {
				console.log("Something went wrong");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// puts new question details to database and redirects to question home
	$scope.updateQuestion = function(details) {
		console.log("Updating question");
		$http.put("http://localhost:8080/edugreat/service/question/update/", details)
			.then(function successCallback(response) {
				$anchorScroll("admin_questionedit_editForm");
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Question does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured. Question was not updated.";
					}
				}
				else {
					$scope.successMessage = "Question updated.";
					$rootScope.thisquestion = response.data.object;
				}
			}, function errorCallback(response) {
				console.log("Something went wrong");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// deletes question from database and redirects to question home
	$scope.deleteQuestion = function(question_id, question) {
		console.log("Deleting question " + question_id);
    	$http({
    	    method: 'DELETE',
    	    url: 'http://localhost:8080/edugreat/service/question/delete',
    	    data: {
    	        id: question_id
    	    }
    	})
    		.then(function successCallback(response) {
    			if (response.data.status == "INVALID") {
    				console.log(response.data);
    				$anchorScroll("admin_questionedit_editForm");
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Question does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured. Question was not deleted.";
					}
    			}
    			else {
    				$window.location.href='#!questions'
    				$scope.successMessage = "Question deleted."
    			}
    		}, function errorCallback(response) {
    			console.log("Something went wrong");
    			$window.location.href = '#!error/unknown'
    		});		
	};
	
	// posts new question details to database and redirects to questions home
	$scope.addQuestion = function(details) {
		console.log("Creating new question");
		var prof = JSON.parse($cookies.get("profile"));
		details["createdby"] = prof.id;
		$http.post("http://localhost:8080/edugreat/service/question/add", details)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
    				console.log(response.data);
    				$anchorScroll("admin_questioncreate_createForm");
    				$scope.errorMessage = "A database error occured. Question could not be created.";
    			}
    			else {
    				$window.location.href='#!questions'
    				$scope.successMessage = "Question created."
    			}
			}, function errorCallback(response) {
				console.log("Something went wrong");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// gets all enrollments on opening enrollment page (ng-init)
	$scope.getAllEnrollments = function()  {
		console.log("Getting all enrollments.");
		$http.get("http://localhost:8080/edugreat/service/enrollment/all")
			.then(function successCallback(response) {
				$anchorScroll("admin_enrollments_allTable");
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Could not convert object to JSON.") {
						$scope.errorMessage = "No enrollments";
					} else  {
						$scope.errorMessage = "A database error occured.";
					}
				} else {
					$rootScope.enrollmentdetails = response.data.object;					
				}
			}, function errorCallback(response) {
				console.log("Couldn't get questions");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// gets details for specific enrollment by id
	$scope.viewEnrollment = function(id) {
		console.log("Getting details for Enrollment "+id);
		$http.get("http://localhost:8080/edugreat/service/enrollment/"+id)
			.then(function successCallback(response) {
				$anchorScroll("admin_enrollmentdetails_detailTable");
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Enrollment does not exist.";
					} else {
						$scope.errorMessage = "A database error occured.";						
					}
				} else {
					$rootScope.thisenrollment = response.data.object;
					$window.location.href = '#!enrollment/details'
				}
			}, function errorCallback(response) {
				console.log("Couldn't get enrollment.");
				$window.location.href = '#!error/unknown'
			});
		
		$http.get("http://localhost:8080/edugreat/service/result/enrollment/"+id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Enrollment does not exist.";
					} else {
						$scope.errorMessage = "A database error occured.";						
					}
				} else {
					$rootScope.enrollmentresults = response.data.object.results;
				}
			}, function errorCallback(response) {
				console.log("Couldn't get enrollment.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// changes status of enrollment from requested to approved
	$scope.approveEnrollment = function(enrollment_id, enrollment) {
		console.log("Approving Enrollment "+enrollment_id);
		var data = {
				"id": enrollment_id,
				"stat": "approved"
		};
		$http.put("http://localhost:8080/edugreat/service/enrollment/update", data)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Enrollment does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured.";
					}
				}
				else {
					enrollment.stat = "approved";
					$window.location.href = '#!enrollments'
					$scope.successMessage = "Enrollment approved.";
				}
			}, function errorCallback(response) {
				console.log("Couldn't update enrollment.");
				$window.location.href = '#!error/unknown'
			});
	};
	
	// changes status of enrollment from requested to rejected
	$scope.rejectEnrollment = function(enrollment_id, enrollment) {
		console.log("Rejecting Enrollment "+enrollment_id);
		var data = {
				"id": enrollment_id,
				"stat": "rejected"
		};
		$http.put("http://localhost:8080/edugreat/service/enrollment/update", data)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Enrollment does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured.";
					}
				}
				else {
					enrollment.stat = "rejected";
					$window.location.href = '#!enrollments'
					$scope.successMessage = "Enrollment rejected.";
				}				
			}, function errorCallback(response) {
				console.log("Couldn't get questions");
				$window.location.href = '#!error/unknown'
			});	
	};
	
	// deletes enrollment from database and redirects to enrollment home
	$scope.deleteEnrollment = function(enrollment_id) {
		console.log("Deleting enrollment "+enrollment_id);
		$http({
    	    method: 'DELETE',
    	    url: 'http://localhost:8080/edugreat/service/enrollment/delete',
    	    data: {
    	        id: enrollment_id
    	    }
    	})
    		.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					$anchorScroll("admin_enrollmentdetails_detailTable");
					if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Enrollment does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured. Enrollment was not deleted.";
					}
				}
				else {
					$window.location.href = '#!enrollments'
					$scope.successMessage = "Enrollment deleted.";
				}
    		}, function errorCallback(response) {
    			console.log("Something went wrong");
    			$window.location.href = '#!error/unknown'
    		});
	};
	
	// opens create enrollment page
	$scope.initEnrollment = function(type) {
		console.log("Go to create enrollment page");
		var empty = {};
		$rootScope.newenrollment = empty;
		if (type==0) {
			$rootScope.newenrollment.assessment = $rootScope.thisassessment;
		}
		if (type==1) {
			$rootScope.newenrollment.student = $rootScope.thisstudent;
		}
		$rootScope.newenrollment.stat = "approved";
		$http.get("http://localhost:8080/edugreat/service/assessment/all")
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					$scope.errorMessage = "Assessments were not loaded.";
				}
				else {
					console.log("Got all assessments.");
					$rootScope.assessmentdetails = response.data.object;
				}
			}, function errorCallback(response) {
				console.log("Error getting assessments");
			});
		$http.get("http://localhost:8080/edugreat/service/student/all")
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log(response.data);
					$scope.errorMessage = "Students were not loaded.";
				}
				else {
					console.log("Got all students.");
					$rootScope.studentdetails = response.data.object;
				}
				console.log("Got all students.");
			}, function errorCallback(response) {
				console.log("Error getting students");
			});
		$window.location.href = '#!enrollment/create'
	};
	
	// add enrollment by posting enrollment details
	$scope.addEnrollment = function(details) {
		var data = {
			"assessmentId": details.assessment.id,
			"studentId": details.student.id,
			"validFrom": details.validFrom,
			"validUntil": details.validUntil,
			"stat": details.stat
		};
		$http.post("http://localhost:8080/edugreat/service/enrollment/add", data)
		.then(function successCallback(response) {
			if (response.data.status == "INVALID") {
				console.log(response.data);
				$anchorScroll("admin_enrollmentcreate_createForm");
				$scope.errorMessage = "A database error occured. Enrollment could not be created.";
			}
			else {
				$window.location.href='#!enrollments'
				$scope.successMessage = "Enrollment created."
			}
		}, function errorCallback(response) {
			console.log("Couldn't add enrollment");
			$window.location.href = '#!error/unknown'
		});
	};
	
	$scope.viewResult = function(id) {
		console.log("Getting result info.");
		$http.get("http://localhost:8080/edugreat/service/result/"+id)
			.then(function successCallback(response) {
				if (response.data.status == "INVALID") {
					console.log("Couldn't find result.");
					$scope.errorMessage2 = "Result did not load";
				} else {
					$rootScope.thisresult = response.data.object;
					$rootScope.timeStr = $scope.timeToStr($rootScope.thisresult.timeTaken);
					$window.location.href = "#!result/details"
				}
			}, function errorCallback(response) {
				console.log("Couldn't find result.");
				$window.location.href = '#!error/unknown'
			});
	};

	// gets all admin on opening admin home (ng-init)
    $scope.getAllAdmins = function() {
        console.log("All admin details requested.");
        $http.get("http://localhost:8080/edugreat/service/admin/all")
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		if (response.data.message == "Could not convert object to JSON.") {
            			$scope.errorMessage = "No admin users.";
            		} else {
            			$scope.errorMessage = "A database error occured."
            		}
            	}
            	else {
            		$rootScope.admindetails = response.data.object;
            	}
            }, function errorCallback(response){
                console.log("Something went wrong.");
                $window.location.href = '#!error/unknown'
            });
    };
    
    // gets all student on opening student home (ng-init)
    $scope.getAllStudents = function() {
        console.log("All student details requested.");
        $http.get("http://localhost:8080/edugreat/service/student/all")
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		if (response.data.message == "Could not convert object to JSON.") {
            			$scope.errorMessage = "No student users.";
            		} else {
            			$scope.errorMessage = "A database error occured."
            		}
            	}
            	else {
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
            		$anchorScroll("admin_students_allTable");
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
            		$anchorScroll("admin_studentedit_editForm");
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
    
    // deletes student from database and redirects to student home
    $scope.deleteStudent = function(student_id) {
    	console.log("Deleting student " + student_id);
    	console.log(student_id);
    	$http({
    	    method: 'DELETE',
    	    url: 'http://localhost:8080/edugreat/service/student/delete',
    	    data: {
    	        id: student_id
    	    }
    	})
    		.then(function successCallback(response) {
    			if (response.data.status == "INVALID") {
            		console.log(response.data);
            		$anchorScroll("admin_studentedit_editForm");
            		if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Student does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured. Student was not deleted.";
					}
            	}
            	else {
            		$window.location.href = '#!student'
            		$scope.successMessage = "Student deleted.";
            	}
    		}, function errorCallback(response) {
    			console.log("Something went wrong");
    			$window.location.href = '#!error/unknown'
    		});
    };

    // gets all parent info on opening parent home (ng init)
    $scope.getAllParents = function() {
        console.log("All parent details requested.");
        $http.get("http://localhost:8080/edugreat/service/parent/all")
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		if (response.data.message == "Could not convert object to JSON.") {
            			$scope.errorMessage = "No parent users.";
            		} else {
            			$scope.errorMessage = "A database error occured."
            		}
            	}
            	else {
            		$rootScope.parentdetails = response.data.object;
            	}
            }, function errorCallback(response){
                console.log("Something went wrong.");
                $window.location.href = '#!error/unknown'
            });
    };
    
    // gets specific parent details by id
    $scope.editParent = function(id) {
        console.log("Getting parent " + id);
        $http.get("http://localhost:8080/edugreat/service/parent/"+id)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Parent does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured.";
					}
            	}
            	else {
            		$window.location.href = '#!parent/details'
            		$rootScope.thisparent = response.data.object;
            		$rootScope.thisparent.dob = new Date($rootScope.thisparent.dob);
            	}
            }, function errorCallback(response) {
                console.log("Something went wrong");
                $window.location.href = '#!error/unknown'
            });
    };
    
    // puts new parent details to database and redirects to parent home
    $scope.updateParent = function(details) {
        console.log("Putting new parent " + details.id + " info.");
        console.log(details);
        $http.put("http://localhost:8080/edugreat/service/parent/update", details)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		$anchorScroll("admin_parentedit_editForm");
            		if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Parent does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured. Parent was not updated.";
					}
            	}
            	else {
            		$window.location.href = '#!parent'
            		$scope.successMessage = "Parent updated.";
            	}
            }, function errorCallback(response) {
                console.log("Something went wrong");
                $window.location.href = '#!error/unknown'
            });
    };
    
    // deletes parent from database and redirects to parent home
    $scope.deleteParent = function(parent_id) {
    	console.log("Deleting parent " + parent_id);
    	$http({
    	    method: 'DELETE',
    	    url: 'http://localhost:8080/edugreat/service/parent/delete',
    	    data: {
    	        id: parent_id
    	    }
    	})
    		.then(function successCallback(response) {
    			if (response.data.status == "INVALID") {
            		console.log(response.data);
            		$anchorScroll("admin_parentedit_editForm");
            		if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Parent does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured. Parent was not deleted.";
					}
            	}
            	else {
            		$window.location.href = '#!parent'
            		$scope.successMessage = "Parent deleted.";
            	}
    		}, function errorCallback(response) {
    			console.log("Something went wrong");
    			$window.location.href = '#!error/unknown'
    		});
    };
    
    // registers/adds new parent and redirects to parent home
    $scope.addParent = function(details) {
    	console.log("Creating parent");
    	details.userType = "parent";
    	details.generate = true;
        $http.post("http://localhost:8080/edugreat/service/parent/add", details)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
    				console.log(response.data);
    				$anchorScroll("admin_parentregister_createForm");
    				$scope.errorMessage = "A database error occured. Parent could not be registered.";
    			}
    			else {
    				$window.location.href='#!parent'
    				$scope.successMessage = "Parent registered.";
    				console.log(response.data);
    				var userdata = response.data;
    				var emailInfo = {
    					"email": userdata.object.email,
    					"subject": "New Parent Account",
    					"message": "To "+userdata.object.firstName+" "+userdata.object.lastName+"\n\nAn admin has " +
    							"signed you up for an Edugreat account!" + "\nYour account has been given a default " +
    									"password. \nPassword is: \n\n"+userdata.passw+"\n\nPlease click the confirmation" +
    											"link to confirm your email:" +
    											"\n\nhttp://localhost:8080/edugreat/main_index.html#!/confirm/parent/email="+
    									userdata.object.email+"/auth="+userdata.auth+"\n After confirming your email" +
    											"you can login into your account on our website to change your password." +
    											"\n\n	Sincerely,\n\n	Edugreat"
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
            }, function errorCallback(response) {
                console.log("Something went wrong");
                $window.location.href = '#!error/unknown'
            });
    };
    
    //view profile
	$scope.updateProfile = function(details) {
		console.log("Putting new admin " + details.id + " info.");
        $http.put("http://localhost:8080/edugreat/service/admin/update", details)
            .then(function successCallback(response) {
            	if (response.data.status == "INVALID") {
            		console.log(response.data);
            		$anchorScroll("main_profile");
            		if (response.data.message == "Object not found.") {
						$scope.errorMessage = "Admin does not exist.";
					}
					else {
						$scope.errorMessage = "A database error occured. Profile was not updated.";
					}
            	}
            	else {
            		$scope.successMessage = "Profile updated.";
            		$rootScope.myAdmin = response.data.object;
            		$rootScope.myAdmin.dob = new Date(response.data.object.dob);
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
		$http.post("http://localhost:8080/edugreat/service/admin/change/password", data)
			.then(function successCallback(response) {
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