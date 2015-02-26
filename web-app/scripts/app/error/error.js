'use strict';

angular.module('budgetApp')
  .config(['$stateProvider', function($stateProvider) {
    $stateProvider.state('access-denied', {
      url: '/access-denied',
      parent: 'site',
      data: {
        roles: [],
        pageTitle: 'Access Denied'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/app/error/access-denied.html'
        }
      }
    })
  }]);