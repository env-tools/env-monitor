(function() {
    'use strict';

    angular
        .module('queryLib')
        .service('QueryExecutorService', QueryExecutorService);

    QueryExecutorService.$inject = ['$resource'];

    function QueryExecutorService($resource) {
        return $resource('/query_execute_result.json')
    }
})();