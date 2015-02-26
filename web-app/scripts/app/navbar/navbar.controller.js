'use strict';

angular.module('budgetApp')

    .controller('NavbarController', NavbarController);

NavbarController.$inject = ['$scope'];

function NavbarController($scope) {
    console.log('In NavbarController');
}