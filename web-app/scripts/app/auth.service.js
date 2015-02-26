'use strict';

angular.module('budgetApp')
    .factory('authService', authService);

authService.$inject = ['$state', '$rootScope', '$q', 'sessionService'];

function authService($state, $rootScope, $q, sessionService) {
    var service = {};

    service.authorize = function(event) {
        var requiredRoles = $rootScope.toState.data.roles;

        if (requiredRoles && requiredRoles.length > 0 && !sessionService.isInAnyRole(requiredRoles)) {
            event.preventDefault();

            if (sessionService.getIsLoggedIn()) {
                // TODO: redirect to access denied
            } else {
                $rootScope.returnToState = $rootScope.toState;
                $rootScope.returnToStateParams = $rootScope.toStateParams;
                $state.go('login');
            }
        }
    };

    return service;
};