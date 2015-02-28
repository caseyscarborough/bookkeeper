'use strict';

angular.module('budgetApp')
    .controller('TransactionController', function ($scope, transactionService, accountsService, categoriesService) {

        $scope.errorShown = false;
        $scope.error = "";

        $scope.maxSize = 10;
        $scope.totalItems = 0;
        $scope.currentPage = 1;
        $scope.transactions = null;
        $scope.accounts = [];
        $scope.categories = [];
        $scope.pages = [];
        $scope.itemsPerPage = 30;
        $scope.newTransaction = {
            date: new Date()
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };


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
            $scope.newTransaction.fromAccount = data.accounts[0].id;
        });

        categoriesService.getCategories(function (data) {
            $scope.categories = data.categories;
            $scope.newTransaction.subCategory = data.categories[0].subcategories[0].id;
        });

        $scope.isLoaded = function () {
            return $scope.transactions !== null;
        };

        $scope.createTransaction = function () {
            console.log("Checking for valid");
            if ($scope.form.$valid) {
                transactionService.createTransaction($scope.newTransaction, function (data) {
                    setPage(1);
                    $scope.form.$setPristine();
                    $scope.newTransaction.description = "";
                    $scope.newTransaction.amount = "";
                    $scope.newTransaction.fromAccount = data.accounts[0].id;
                    $scope.newTransaction.subCategory = data.categories[0].subcategories[0].id;
                }, function (data) {
                    $scope.errorShown = true;
                    $scope.errorMessage = data.message;
                });
            }
        };


    });