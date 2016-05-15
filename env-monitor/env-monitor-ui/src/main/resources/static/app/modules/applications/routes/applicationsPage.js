(function () {
    'use strict';

    angular
        .module('applications')
        .config(routersConfig);

    routersConfig.$inject = ['$stateProvider'];

    function routersConfig($stateProvider) {
        $stateProvider
            .state('applications_applicationsPage', {
                url: "/applications",
                templateUrl: "/app/modules/applications/templates/applications.list.html",
                controller: 'ApplicationsPageController'
            })
    }
})();