(function () {
    'use strict';

    angular
        .module('app.applications')
        .controller('ApplicationsController', ApplicationsController);

    ApplicationsController.$inject = ['$q', 'dataservice', 'logger'];
    /* @ngInject */
    function ApplicationsController($q, dataservice, logger) {
        var vm = this;

        vm.messageCount = 0;
        vm.people = [];
        vm.title = 'Applications';

        activate();

        function activate() {
            var promises = [getMessageCount(), getPeople()];
            return $q.all(promises).then(function() {
                logger.info('Activated Applications View');
            });
        }

        function getMessageCount() {
            return dataservice.getMessageCount().then(function (data) {
                vm.messageCount = data;
                return vm.messageCount;
            });
        }

        function getPeople() {
            return dataservice.getPeople().then(function (data) {
                vm.people = data;
                return vm.people;
            });
        }
    }
})();
