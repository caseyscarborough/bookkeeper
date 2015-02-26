angular.module('budgetApp')
    .factory('sessionService', sessionService);

sessionService.$inject = ['$http'];

function sessionService($http) {
    var service = {};

    service.login = function (user) {
        $http.post('http://localhost:8080/budget/api/login', user)
            .success(function (data) {
                console.log("Success!");
            })
            .error(function (data) {
                console.log("Error!");
            });
    };

    return service;
}