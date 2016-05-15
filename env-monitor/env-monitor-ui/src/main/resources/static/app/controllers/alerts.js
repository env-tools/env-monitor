(function () {
    'use strict';

    angular
        .module('envMonitor')
        .controller('Alerts', Alerts);

    Alerts.$injector = ['$scope', '$rootScope'];

    function Alerts($scope, $rootScope) {
        $scope.type = "";
        $scope.message = "";

        $rootScope.$on('showAlert', function (event, data) {
            $scope.type = data['type'];
            $scope.message = data['message'];
        })
    }
})();