'use strict';

angular.module('budgetApp')
    .factory('accountsService', accountsService);

accountsService.$inject = ['$http'];

function accountsService($http) {
    var service = {};

    service.getAccounts = function(success, error) {
        $http.get('api/accounts')
            .success(function(data, status, headers, config) {
                if (success && typeof success === "function") {
                    success(data);
                }
            })
            .error(function(data, status, headers, config) {
                if (error && typeof error === "function") {
                    error(data);
                }
            });
    };

    return service;
}