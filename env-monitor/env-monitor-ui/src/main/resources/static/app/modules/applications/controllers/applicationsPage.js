(function () {
    'use strict';

    angular
        .module('applications')
        .controller('ApplicationsPageController', ApplicationsPageController);

    ApplicationsPageController.$inject = ['$scope', 'ngstomp'];

    function ApplicationsPageController($scope, ngstomp) {

        $scope.applications = [];
        $scope.platforms = [];
        $scope.environments = [];
        $scope.filters = {
            selectedPlatform: null,
            selectedEnvironment: null
        };

        init();

        function init() {
            ngstomp.subscribeTo('/call/modules/M_APPLICATIONS/data/platforms').callback(
                function (msg) {
                    $scope.$apply(function () {
                        $scope.platforms = msg.body.payload.jsonContent;
                        $scope.filters.selectedPlatform = $scope.platforms[0];
                    });
                    console.log('Call result for platforms: ' + JSON.stringify(msg.body.payload.jsonContent));


                    var pattern = '/call/modules/M_APPLICATIONS/data/platforms/{platformId}/environments';
                    ngstomp.subscribeTo(pattern.replace('{platformId}', $scope.filters.selectedPlatform.id))
                        .callback(
                            function (msg) {
                                $scope.$apply(function () {
                                    $scope.environments = msg.body.payload.jsonContent;
                                    $scope.filters.selectedEnvironment = $scope.environments[0];
                                });

                                console.log('Call result for environments: ' + JSON.stringify(msg.body.payload.jsonContent));

                                $scope.selectedEnvironmentChanged();

                            }).withBodyInJson().connect();
            }).withBodyInJson().connect();
        }

        $scope.selectedPlatformChanged = function () {
            var pattern = '/call/modules/M_APPLICATIONS/data/platforms/{platform}/environments';

            ngstomp.subscribeTo(pattern.replace("{platform}", $scope.filters.selectedPlatform.id))
                .callback(function (msg) {
                    $scope.$apply(function () {
                        $scope.environments = msg.body.payload.jsonContent;
                        $scope.filters.selectedEnvironment = $scope.environments[0];

                        $scope.selectedEnvironmentChanged();

                    });

                    console.log('Call result for environments: ' + JSON.stringify(msg.body.payload.jsonContent));
                }).withBodyInJson().connect();
        };

        $scope.selectedEnvironmentChanged = function () {

            var pattern = '/subscribe/modules/M_APPLICATIONS/data/platforms/{platformId}/environments/{environmentId}/applications';

            if (!$scope.filters.selectedPlatform) {
                return;
            }

            if ($scope.currentApplicationsSubscribeId) {
                //TODO: check order subsc/unsubscr
                $scope.currentApplicationsSubscribeId.unSubscribeAll();
            }

            $scope.currentApplicationsSubscribeId = ngstomp.subscribeTo(
                pattern
                    .replace("{platformId}",
                        $scope.filters.selectedPlatform.id)
                    .replace("{environmentId}",
                        $scope.filters.selectedEnvironment.id))
                .callback(function (msg) {
                    $scope.$apply(function () {
                        $scope.applications = msg.body.payload.jsonContent;
                    });

                    console.log('New data for subscription! ' + JSON.stringify(/* $scope.applications */ msg.body.payload.jsonContent));
                }).withBodyInJson().connect();
        }
    }
})();