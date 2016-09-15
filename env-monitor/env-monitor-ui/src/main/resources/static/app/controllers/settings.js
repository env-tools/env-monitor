(function () {
    'use strict';

    angular
        .module('envMonitor')
        .controller('Settings', Settings);

    Settings.$injector = ['$scope', '$http', '$location'];

    function Settings($scope, $http, $location) {
        $http
            .get('/settings')
            .success(function(response){
                $scope.settings = response;
            })
            .error(function(response, status) {
               console.log('Could not load settings, response: ' + response + ' status:' + status);
                $scope.settings = {};
            });

    }
})();