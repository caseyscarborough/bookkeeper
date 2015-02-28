'use strict';

angular.module('budgetApp')
    .controller('TransactionController', function ($scope, transactionService, accountsService, categoriesService) {

        $scope.maxSize = 10;
        $scope.totalItems = 0;
        $scope.currentPage = 1;
        $scope.transactions = null;
        $scope.accounts = [];
        $scope.categories = [];
        $scope.pages = [];
        $scope.itemsPerPage = 30;
        $scope.newTransaction = {};

        setPage($scope.currentPage);
        $scope.pageChanged = function () {
            setPage($scope.currentPage);
        };

        $scope.$watch('itemsPerPage', function() {
            setPage($scope.currentPage);
        });

        function setPage(page) {
            transactionService.getTransactions(page, {max: $scope.itemsPerPage}, function (data) {
                $scope.pages = new Array(Math.ceil(data.total / data.max));

                var currentPage = (data.offset / data.max) + 1;
                if (currentPage <= $scope.pages.length) {
                    $scope.transactions = data.transactions;
                    $scope.totalItems = data.total;
                    $scope.currentPage = currentPage;
                }
            });
        }

        accountsService.getAccounts(function (data) {
            $scope.accounts = data.accounts;
        });

        categoriesService.getCategories(function (data) {
            $scope.categories = data.categories;
        });

        $scope.isLoaded = function () {
            return $scope.transactions !== null;
        };


    });