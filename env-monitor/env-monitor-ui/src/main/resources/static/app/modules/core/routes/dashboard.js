(function () {
  'use strict';

  angular
    .module('core')
    .config(routersConfig);

  routersConfig.$inject = ['$stateProvider'];

  function routersConfig($stateProvider) {
    $stateProvider
      .state('core_dashboard', {
        url: "/",
        templateUrl: "/app/modules/core/templates/dashboard.html"
      })
  }
})();