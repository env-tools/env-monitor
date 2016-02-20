(function() {
    'use strict';

    angular
        .module('app.applications')
        .run(appRun);

    appRun.$inject = ['routerHelper'];
    /* @ngInject */
    function appRun(routerHelper) {
        routerHelper.configureStates(getStates());
    }

    function getStates() {
        return [
            {
                state: 'applications',
                config: {
                    url: '/',
                    templateUrl: 'app/applications/applications.html',
                    controller: 'ApplicationsController',
                    controllerAs: 'vm',
                    title: 'Applications',
                    settings: {
                        nav: 1,
                        content: '<i class="fa fa-dashboard"></i> Applications'
                    }
                }
            }
        ];
    }
})();
