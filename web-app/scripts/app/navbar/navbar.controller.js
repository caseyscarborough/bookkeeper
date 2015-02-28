'use strict';

angular.module('budgetApp')

    .controller('NavbarController', function ($scope, sessionService, $state) {
        $scope.isLoggedIn = sessionService.getIsLoggedIn;

        $scope.logout = function() {
            sessionService.logout();
            $state.go('login');
        };
    });