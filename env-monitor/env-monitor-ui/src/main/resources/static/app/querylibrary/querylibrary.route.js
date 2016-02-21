(function() {
    'use strict';

    angular
        .module('app.querylibrary')
        .run(queryLibRun);

    queryLibRun.$inject = ['routerHelper'];
    /* @ngInject */
    function queryLibRun(routerHelper) {
        routerHelper.configureStates(getStates());
    }

    function getStates() {
        return [
            {
                state: 'querylibrary',
                config: {
                    url: '/',
                    templateUrl: 'app/querylibrary/querylibrary.html',
                    controller: 'QuerylibraryController',
                    controllerAs: 'vm',
                    title: 'Query Library',
                    settings: {
                        nav: 1,
                        content: '<i class="fa fa-dashboard"></i> Query Library'
                    }
                }
            }
        ];
    }
})();
