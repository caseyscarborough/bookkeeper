'use strict';

angular.module('budgetApp')
    .factory('transactionService', function ($http) {
        var service = {};

        service.getTransactions = function (page, params, success) {
            params.max = params.max || 10;
            params.offset = (page - 1) * params.max;
            $http.get('api/transactions', {params: params})
                .success(function (data) {
                    success(data);
                })
                .error(function (data) {
                    console.log('error');
                });
        };

        return service;
    });