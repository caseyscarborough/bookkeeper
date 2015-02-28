'use strict';

angular.module('budgetApp')
    .controller('TransactionController', function ($scope, transactionService, accountsService, categoriesService) {
        $scope.transactions = null;
        $scope.accounts = [];
        $scope.categories = [];
        $scope.pages = [];
        $scope.currentPage = null;

        $scope.getPage = function (page) {
            // TODO: Develop a simpler implementation
            if (page > 0 && page !== $scope.currentPage && (($scope.pages.length === 0) || (page !== $scope.pages.length + 1))) {
                transactionService.getTransactions(page, {}, function (data) {
                    $scope.pages = new Array(Math.ceil(data.total / data.max));

                    var currentPage = (data.offset / data.max) + 1;
                    if (currentPage <= $scope.pages.length) {
                        $scope.transactions = data.transactions;
                        $scope.currentPage = currentPage;
                    }
                });
            }
        };

        accountsService.getAccounts(function (data) {
            $scope.accounts = data.accounts;
        });

        categoriesService.getCategories(function (data) {
            $scope.categories = data.categories;
        });

        $scope.isLoaded = function () {
            return $scope.transactions !== null;
        };

        $scope.getPage(1);
    });