'use strict';

angular.module('budgetApp')

    .config(function ($stateProvider) {
        console.log('Setting login state...');
        $stateProvider.state('login', {
            url: '/login',
            parent: 'site',
            views: {
                'content': {
                    templateUrl: 'scripts/app/login/login.html',
                    controller: 'LoginController'
                }
            }
        });
    });