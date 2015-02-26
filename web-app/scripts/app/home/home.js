'use strict';

angular.module('budgetApp')

    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider.state('home', {
            url: '/',
            parent: 'site',
            data: {
                roles: [],
                pageTitle: 'Home'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/home/home.html',
                    controller: 'HomeController'
                }
            }
        });
    }]);