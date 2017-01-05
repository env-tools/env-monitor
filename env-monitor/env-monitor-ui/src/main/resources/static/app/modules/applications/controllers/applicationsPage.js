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

        $scope.refreshGrid = function () {
            $scope.gridApi.core.refresh();
        }

        init();

        function calculateStatusCellClass(cellValue) {
            if (cellValue == 'RUNNING') {
                return 'green';
            } else {
                return 'red';
            }
        }

        function init() {


            $scope.gridOptions =
            {
                enableColumnResizing: true,
                enableAutoResizing: true,
                autoResize: true,

                columnDefs: [
                    {name: "Name", field: "name", width: "10%", resizable: true, enableColumnResizing: true,
                        //The below cell class is responsible for styling the whole row (using a trick)
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            if (row.treeLevel > 0)
                                return 'SUBROW';
                        }
                    },
                    {name: "Type", field: "applicationType", width: "10%", resizable: true, enableColumnResizing: true},
                    {name: "Host", field: "host", width: "7%", resizable: true, enableColumnResizing: true},
                    {name: "Port", field: "port", width: "5%", resizable: true, enableColumnResizing: true},
                    {
                        name: "Url",
                        field: "url",
                        width: "25%",
                        enableColumnResizing: true,
                        cellTemplate: "<a title='{{COL_FIELD}}' target=_blank href='{{COL_FIELD}}'>{{COL_FIELD}}</a>",
                        autoResize: true
                    },

                    {name: "Version", field: "version", width: "18%", resizable: true, enableColumnResizing: true,
                        cellTooltip: function (row, col) {
                            return row.entity.version;
                        }
                    },
                    {name: "Component Name", field: "componentName", width: "9%", resizable: true, enableColumnResizing: true},
                    {name: "Memory (Mb)", field: "processMemory", width: "8%", resizable: true, enableColumnResizing: true},
                    {
                        name: "Status", field: "status", width: "8%", resizable: true,
                        enableColumnResizing: true,
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            return calculateStatusCellClass( grid.getCellValue(row, col) );
                        }
                    }
                ],

                showTreeExpandNoChildren: false,

                onRegisterApi: function (gridApi) {
                    $scope.gridApi = gridApi;

                    //Have UI Grid expanded initially
                    // http://stackoverflow.com/questions/30893210/how-to-make-angular-ui-grid-expand-all-rows-initially

                    //I think this means that every time gridOptions.data changes completely,
                    //we do the expansion
                    gridApi.grid.registerDataChangeCallback(function () {
                        if ($scope.gridApi.grid.treeBase.tree instanceof Array) {
                            $scope.gridApi.treeBase.expandAllRows();
                        }
                    });
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

                    var newData = convertToDataForGrid($scope.applications);
                    //If data arrives for the first time, set it
                    if (!$scope.gridOptions.data || $scope.gridOptions.data.length == 0) {
                        $scope.gridOptions.data = newData;
                    } else {
                        //If data is already present,
                        // to avoid losing grid row collapse/expand state,
                        // we merge data by individual elements
                        var mergeResult = mergeChanges(newData, $scope.gridOptions.data);
                        if (!mergeResult) {
                            //Smart merge failed, we have to just copy data as a whole
                            $scope.gridOptions.data = newData;
                            console.log('Rewritten gridOptions.data')
                        }
                    }

                    //Tries to merge changes from newData to old data
                    //returns False if it fails to do so
                    function mergeChanges(newData, oldData) {
                        if (!(newData instanceof Array) || !(oldData instanceof Array) ||
                            oldData.length != newData.length) {
                            return false;
                        }
                        for (var index in newData) {
                            for (var field in newData[index]) {
                                if (newData[index][field] !== oldData[index][field]) {
                                    //console.log('[' + index + '] ' + field + ': ' + oldData[index][field] + '->' + newData[index][field]);
                                    oldData[index][field] = newData[index][field];
                                }
                            }
                        }
                        return true;
                    }

                    //Converts data from server to UI-Grid compliant format
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

                        console.log('convertToDataForGrid : conversion result=' + JSON.stringify(dataForGrid));

                        function addApplicationForGrid(applicationsForGrid, application, level) {
                            var applicationForGrid = {
                                name: generateIndent(level) + application.name,
                                applicationType: application.applicationType,
                                host: application.host,
                                port: application.port,
                                url: application.url,
                                version: application.version,
                                componentName: application.componentName,
                                processMemory: application.processMemory,
                                status: application.status,

                                $$treeLevel: level
                            };

                            applicationsForGrid.push(applicationForGrid);
                        }

                        function generateIndent(level) {
                            return Array(1 + (level * 6)).join('\xa0');
                        }

                        return dataForGrid;
                    }

                    console.log('Processed new data for subscription: ' +
                        JSON.stringify(msg.body.payload.jsonContent));

                }).withBodyInJson().connect();
        }
    }
})();