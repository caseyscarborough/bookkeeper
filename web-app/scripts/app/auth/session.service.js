angular.module('budgetApp')
    .factory('sessionService', sessionService);

sessionService.$inject = ['$http', 'jwtHelper'];

function sessionService($http, jwtHelper) {
    var service = {};

    service.login = function (user, success, error) {
        $http.post('api/login', user)
            .success(function (data, status, headers, config) {
                if (user.rememberMe) {
                    localStorage.setItem('token', data.access_token);
                } else {
                    sessionStorage.setItem('token', data.access_token);
                }
                if (success && typeof success === "function") {
                    success();
                }
            })
            .error(function (data, status, headers, config) {
                localStorage.removeItem('token');
                sessionStorage.removeItem('token');
                var message;
                if (status === 401) {
                    message = "The username or password you've entered is incorrect.";
                } else {
                    message = "An error occurred while trying to log you in. Please try again."
                }
                if (error && typeof error === "function") {
                    error(message);
                }
            });
    };

    service.getRoles = function() {
        var token = service.getToken();
        if (token !== null) {
            var tokenPayload = jwtHelper.decodeToken(token);
            return tokenPayload.roles;
        }
        return [];
    };

    service.getUsername = function() {
        var token = service.getToken();
        if (token !== null) {
            var tokenPayload = jwtHelper.decodeToken(token);
            return tokenPayload.sub;
        }
        return null;
    };

    service.getIsLoggedIn = function() {
        var token = service.getToken();
        var isLoggedIn = token !== null && !jwtHelper.isTokenExpired(token);
        if (!isLoggedIn) {
            service.logout();
        }
        return isLoggedIn;
    };

    service.getToken = function() {
        if (localStorage.getItem('token') !== null) {
            return localStorage.getItem('token');
        }

        if (sessionStorage.getItem('token') !== null) {
            return sessionStorage.getItem('token');
        }

        return null;
    };

    service.logout = function() {
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');
    };

    service.isInAnyRole = function(roles) {
        for (var i = 0; i < roles.length; i++) {
            if (service.isInRole(roles[i])) {
                return true;
            }
        }
        return false;
    };

    service.isInRole = function(role) {
        var roles = service.getRoles();
        return roles.indexOf(role) !== -1;
    };

    return service;
}