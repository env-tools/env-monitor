(function (SockJS) {
    'use strict';

    angular
        .module('envMonitor')
        .config(stomp)
        .config(routing);

    routing.$inject = ['$urlRouterProvider', '$locationProvider'];

    function routing($urlRouterProvider, $locationProvider) {
        $locationProvider.html5Mode(false);
        $urlRouterProvider.otherwise('/');
    }

    stomp.$inject = ['ngstompProvider'];

    function stomp(ngstompProvider) {
        ngstompProvider.url('/monitor').class(SockJS);
    }

})(SockJS);