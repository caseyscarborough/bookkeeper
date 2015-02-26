'use strict';

angular.module('budgetApp')

    .controller('NavbarController', NavbarController);

NavbarController.$inject = ['$scope', 'sessionService', '$state'];

function NavbarController($scope, sessionService, $state) {
    $scope.isLoggedIn = sessionService.getIsLoggedIn;

    $scope.logout = function() {
        sessionService.logout();
        $state.go('login');
    };
}