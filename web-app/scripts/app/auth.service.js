'use strict';

angular.module('budgetApp')
    .factory('authService', authService);

authService.$inject = ['$state', '$rootScope', '$q', 'sessionService'];

function authService($state, $rootScope, $q, sessionService) {
    var service = {};

    service.authorize = function(event) {
        var requiredRoles = $rootScope.toState.data.roles;

        if (requiredRoles && requiredRoles.length > 0 && !isInAnyRole(requiredRoles)) {
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

    function isInAnyRole(roles) {
        for (var i = 0; i < roles.length; i++) {
            console.log("Checking if has role " + roles[i]);
            if (isInRole(roles[i])) {
                return true;
            }
        }
        return false;
    }

    function isInRole(role) {
        var roles = sessionService.getRoles();
        return roles.indexOf(role) !== -1;
    }

    return service;
};