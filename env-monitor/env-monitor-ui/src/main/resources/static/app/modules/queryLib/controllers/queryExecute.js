(function() {
    'use strict';

    angular
        .module('queryLib')
        .controller('QueryExecute', QueryExecute);

    QueryExecute.$injector = ['$scope', 'QueryExecutorService'];

    function QueryExecute($scope, QueryExecutorService) {
        $scope.execute = execute;

        $scope.resultExecute = {};
        $scope.columns = {};

        function execute(){
            QueryExecutorService.get([], getExecuteResult);
        }

        function getExecuteResult(data) {
            $scope.settings =
            {
                source: data.result,
                columns: data.columns,
                altrows: true,
                sortable: true,
                selectionmode: 'multiplecellsextended'
            };
        }
    }
})();