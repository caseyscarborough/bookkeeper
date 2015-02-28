'use strict';

angular.module('budgetApp')

    .config(function ($stateProvider) {
        $stateProvider.state('login', {
            url: '/login',
            parent: 'site',
            data: {
                roles: [],
                pageTitle: 'Login'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/login/login.html',
                    controller: 'LoginController'
                }
            }
        });
    });