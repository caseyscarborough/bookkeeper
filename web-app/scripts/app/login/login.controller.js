angular.module('budget.login', [])
    .controller('LoginCtrl', LoginCtrl);

LoginCtrl.$inject('$scope');

function LoginCtrl($scope) {
    $scope.user = {};
}