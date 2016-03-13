(function () {
  'use strict';

  angular
    .module('queryLib')
    .config(routersConfig);

  routersConfig.$inject = ['$stateProvider'];

  function routersConfig($stateProvider) {
    $stateProvider
      .state('query_executeQuery', {
        url: "/query/execute",
        templateUrl: "/app/modules/queryLib/templates/execute.html",
        controller: "QueryExecute",
        controllerAs: "queryExecute"
      })
  }
})();