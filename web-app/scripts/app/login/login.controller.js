'use strict';

angular.module('budgetApp')
    .controller('LoginController', LoginController);

LoginController.$inject = ['$scope'];

function LoginController($scope) {
    console.log("In LoginController");
    $scope.user = {};
}