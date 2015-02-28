'use strict';

angular.module('budgetApp')
    .config(function ($stateProvider) {
        $stateProvider.state('transactions', {
            url: '/transactions',
            parent: 'site',
            data: {
                roles: ['ROLE_USER'],
                pageTitle: 'Transactions'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/transaction/transaction.html',
                    controller: 'TransactionController'
                }
            }
        });
    });