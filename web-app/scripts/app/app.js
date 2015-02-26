'use strict';

angular.module('budgetApp', ['ui.router'])
    .run(function () {
        console.log('App started');
    })
    .config(function ($stateProvider, $urlRouterProvider) {
        console.log('Setting otherwise route...');
        $urlRouterProvider.otherwise('/');

        console.log('Setting the site state...');
        $stateProvider.state('site', {
            'abstract': true,
            views: {
                'navbar@': {
                    templateUrl: 'scripts/app/navbar/navbar.html',
                    controller: 'NavbarController'
                }
            }
        });
    });