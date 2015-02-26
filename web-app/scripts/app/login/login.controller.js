'use strict';

angular.module('budgetApp')
    .controller('LoginController', LoginController);

LoginController.$inject = ['$rootScope', '$scope', 'sessionService', '$state'];

function LoginController($rootScope, $scope, sessionService, $state) {
    $scope.user = {};
    $scope.error = "";

    $scope.login = function() {
        if ($scope.form.$valid) {
            sessionService.login($scope.user, function() {
                if ($rootScope.returnToState) {
                    $state.go($rootScope.returnToState.name, $rootScope.returnToStateParams);
                    $rootScope.returnToState = null;
                    $rootScope.returnToStateParams = null;
                } else {
                    $state.go('home');
                }
            }, function(error) {
                $scope.error = error;
            });
        } else {
            if ($scope.form.username.$error.required) {
                document.getElementById("username").focus();
            } else if ($scope.form.password.$error.required) {
                document.getElementById("password").focus();
            }
        }
    };
}