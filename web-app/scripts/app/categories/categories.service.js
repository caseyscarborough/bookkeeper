'use strict';

angular.module('budgetApp')
    .factory('categoriesService', categoriesService);

categoriesService.$inject = ['$http'];

function categoriesService($http) {
    var service = {};

    service.getCategories = function(success, error) {
        $http.get('api/categories')
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