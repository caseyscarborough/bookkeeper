'use strict';

angular.module('budgetApp')
    .factory('authService', function ($state, $rootScope, sessionService) {
        var service = {};

        service.authorize = function(event) {
            var requiredRoles = $rootScope.toState.data.roles;

            if (requiredRoles && requiredRoles.length > 0 && !sessionService.isInAnyRole(requiredRoles)) {
                event.preventDefault();

                if (sessionService.getIsLoggedIn()) {
                    $state.go('access-denied');
                } else {
                    $rootScope.returnToState = $rootScope.toState;
                    $rootScope.returnToStateParams = $rootScope.toStateParams;
                    $state.go('login');
                }
            }
        };

        return service;
    });