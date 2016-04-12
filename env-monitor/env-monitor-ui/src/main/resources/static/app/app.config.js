(function () {
    'use strict';

    angular
        .module('envMonitor')
        .config(routing);

    routing.$inject = ['$urlRouterProvider', '$locationProvider'];

    function routing($urlRouterProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $urlRouterProvider.otherwise('/');
    }

})();