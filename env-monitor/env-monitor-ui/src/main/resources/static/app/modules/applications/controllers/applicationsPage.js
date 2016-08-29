(function () {
    'use strict';

    angular
        .module('applications')
        .controller('ApplicationsPageController', ApplicationsPageController);

    ApplicationsPageController.$inject = ['$scope', 'ngstomp', 'uiGridConstants'];

    function ApplicationsPageController($scope, ngstomp, uiGridConstants) {

        $scope.applications = [];
        $scope.platforms = [];
        $scope.environments = [];
        $scope.filters = {
            selectedPlatform: null,
            selectedEnvironment: null
        };

        init();

        function init() {

            $scope.gridOptions =
            {
                enableColumnResizing: true,
                enableAutoResizing: true,

                columnDefs: [
                    {name: "Name", field: "name", width: "150", resizable: true},
                    {name: "Type", field: "applicationType", width: "75", resizable: true},
                    {name: "Host", field: "host", width: "150", resizable: true},
                    {name: "Port", field: "port", width: "75", resizable: true},
                    {
                        name: "Url",
                        field: "url",
                        width: "200",
                        cellTemplate: "<a href='{{COL_FIELD}}'>{{COL_FIELD}}</a>", autoResize: true
                    },
                    {name: "Version", field: "version", width: "75", resizable: true},
                    {name: "Component Name", field: "componentName", width: "150", resizable: true},
                    {name: "Memory (Mb)", field: "processMemory", width: "150", resizable: true},
                    {
                        name: "Status", field: "status", width: "150", resizable: true,
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            return grid.getCellValue(row, col) === 'RUNNING' ? 'green' : 'red';
                        }
                    }
                ],

                showTreeExpandNoChildren: false,

                onRegisterApi: function (gridApi) {
                    $scope.gridApi = gridApi;
                }
            };

            ngstomp.subscribeTo('/call/modules/M_APPLICATIONS/data/platforms').callback(
                function (msg) {
                    $scope.platforms = msg.body.payload.jsonContent;
                    $scope.filters.selectedPlatform = $scope.platforms[0];
                    console.log('Call result for platforms: ' + JSON.stringify(msg.body.payload.jsonContent));


                    var pattern = '/call/modules/M_APPLICATIONS/data/platforms/{platformId}/environments';
                    ngstomp.subscribeTo(pattern.replace('{platformId}', $scope.filters.selectedPlatform.id))
                        .callback(
                        function (msg) {
                            $scope.environments = msg.body.payload.jsonContent;
                            $scope.filters.selectedEnvironment = $scope.environments[0];
                            console.log('Call result for environments: ' + JSON.stringify(msg.body.payload.jsonContent));

                            $scope.selectedEnvironmentChanged();

                        }).withBodyInJson().connect();
                }).withBodyInJson().connect();
        }

        $scope.selectedPlatformChanged = function () {
            var pattern = '/call/modules/M_APPLICATIONS/data/platforms/{platform}/environments';

            ngstomp.subscribeTo(pattern.replace("{platform}", $scope.filters.selectedPlatform.id))
                .callback(function (msg) {
                    $scope.environments = msg.body.payload.jsonContent;
                    $scope.filters.selectedEnvironment = $scope.environments[0];

                    $scope.selectedEnvironmentChanged();

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
                    $scope.applications = msg.body.payload.jsonContent;
                    $scope.gridOptions.data = convertToDataForGrid($scope.applications);

//                                [
//                                    {"name":"app1_1-1","applicationType":"applicationType1","host":"host1","port":7000 + 1000 * Math.random(),"url":"http://host1:7000/app/login","version":"1.12_Q20-SNAPSHOT","componentName":"component-1","processMemory":3398,"status":"RUNNING", $$treeLevel : 0},
//                                    {"name":"app2_1-1","applicationType":"applicationType2","host":"host2","port":7001,"url":"http://host1:7001/app/login","version":"1.12_E20","componentName":"component-2-1-1","processMemory":11335,"status":"RUNNING", $$treeLevel : 1}
//                                ],

                    //$scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.ALL);
                    function convertToDataForGrid(applications) {
                        var dataForGrid = [];

                        //Currently we support only one level of nesting
                        angular.forEach(applications, function (application, index) {

                            var collector = this;
                            addApplicationForGrid(collector, application, 0);

                            //Go into depth and process hostees (nested applications)
                            if (application.hostees && application.hostees.length > 0) {
                                angular.forEach(application.hostees, function (hostee, index) {
                                    addApplicationForGrid(collector, hostee, 1);
                                }, collector);
                            }

                        }, dataForGrid);

                        console.log('dataForGrid=' + JSON.stringify(dataForGrid));

                        function addApplicationForGrid(applicationsForGrid, application, level) {
                            var applicationForGrid = {
                                name: application.name,
                                applicationType: application.applicationType,
                                host: application.host,
                                port: application.port,
                                url: application.url,
                                version: application.version,
                                componentName: application.componentName,
                                processMemory: application.processMemory,
                                status: application.status,
                                //,

                                $$treeLevel: level
                            };

                            applicationsForGrid.push(applicationForGrid);
                        }

                        return dataForGrid;
                    }

                    console.log('New data for subscription! ' + JSON.stringify(/* $scope.applications */ msg.body.payload.jsonContent));
                }).withBodyInJson().connect();
        }
    }
})();