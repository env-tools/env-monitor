(function () {
    'use strict';

    angular
        .module('envMonitor', [
            'AngularStompDK',
            'ui.router',
            'ui.grid',
            'ui.grid.treeView',
            'queryLib',
            'applications'
        ]);
})();
