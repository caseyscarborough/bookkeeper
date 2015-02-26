'use strict';

angular.module('budgetApp')
    .controller('LoginController', LoginController);

LoginController.$inject = ['$scope', 'sessionService'];

function LoginController($scope, sessionService) {
    console.log("In LoginController");
    $scope.user = {};
    $scope.login = function() {
        sessionService.login($scope.user);
    };
}