'use strict';

angular.module('budgetApp')

    .config(function ($stateProvider) {
        $stateProvider.state('home', {
            url: '/',
            parent: 'site',
            views: {
                'content@': {
                    templateUrl: 'scripts/app/home/home.html',
                    controller: 'HomeController'
                }
            }
        });
    });