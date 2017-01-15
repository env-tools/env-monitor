(function ($) {
    'use strict';

    angular
        .module('queryLib')
        .controller('QueryExecute', QueryExecute);

    QueryExecute.$injector = ['$scope', '$rootScope', 'ngstomp', 'rfc4122', '$window', '$timeout'];

    function QueryExecute($scope, $rootScope, ngstomp, rfc4122, $window, $timeout) {
        var requestId = rfc4122.v4();
        //subscription for results
        var subDestination = '/subscribe/modules/M_QUERY_LIBRARY/exec/' + requestId;
        //destination to send requests
        var mesDestination = '/message/modulerequest';

        $scope.execute = execute;

        $scope.cancel = cancel;

        $scope.applyState = applyState;

        $scope.clearResult = clearResult;

        $scope.currentQueryContext = {
            queryId: '',
            query: '',
            timeout: 1000,
            rows: 50,
            parameters: {}
        };

        $scope.applyState('INITIAL');

        $scope.resultExecute = [];
        $scope.columns = [];
        $scope.error = false;
        $scope.errorMessage = '';

        init();

        function init() {
            ngstomp.subscribeTo(subDestination).callback(function (message) {
                $scope.$apply(getExecuteResult(message.body.payload.jsonContent))
            }).withBodyInJson().connect();
        }

        function formatQueryParameters(parameters) {
            var result = {};
            angular.forEach(parameters, function(values) {
                result[values.name]  = values.value;
            });

            return result;
        }

        function execute(currentQueryContext) {
            if (!currentQueryContext.dataSource) {
                $window.alert('Data Source not selected');
                return;
            }

            var body = {};
            $scope.infoMessage = '';

            if ($scope.state == 'SELECTED') {
                //Update request id for new query
//                requestId = rfc4122.v4();
//                subDestination = '/subscribe/modules/M_QUERY_LIBRARY/exec/' + requestId;
                body = createNewQueryRequest(currentQueryContext);

                clearResult();
            } else if ($scope.state == 'HAS_MORE_DATA') {
                body = createNextRowsRequest(currentQueryContext);
            } else {
                console.log('Unexpected state in execute(), ignoring: ' + $scope.state);
                return;
            }

            ngstomp.send(mesDestination, body, {});
        }

        function createNewQueryRequest(currentQueryContext) {
            var parameters = formatQueryParameters(currentQueryContext.parameters);
            var body = {
                requestId: requestId,
                destination: subDestination,
                sessionId: rfc4122.v4(),
                username: 'unknown',
                targetModuleId: 'M_QUERY_LIBRARY',
                payload: {
                    payloadType: 'execute',
                    content: {
                        operationId: requestId,
                        query: currentQueryContext.query,
                        queryType: "JDBC",
                        timeOutMs: 10000, /* currentQueryContext.timeout */  //will support it later
                        rowCount: currentQueryContext.rows,
                        queryParameters: parameters,
                        dataSourceProperties: {
                            user: "sa",
                            password: "sa",
                            url: "jdbc:h2:file:./h2_data",
                            driverClassName: "org.h2.Driver"
                        }
                    }
                }
            };

            return body;
        }

        function createNextRowsRequest(currentQueryContext) {
            var body = {
                requestId: requestId,
                destination: subDestination,
                sessionId: rfc4122.v4(),
                username: 'unknown',
                targetModuleId: 'M_QUERY_LIBRARY',
                payload: {
                    payloadType: 'executeNext',
                    content: {
                        operationId: requestId,
                        timeOutMs: 10000, /* currentQueryContext.timeout */  //will support it later
                        rowCount: currentQueryContext.rows
                    }
                }
            };

            return body;
        }

        function cancel(currentQueryContext) {
            var mesDestination = '/message/modulerequest';
            var body = {
                requestId: requestId,
                destination: subDestination,
                sessionId: rfc4122.v4(),
                username: 'unknown',
                targetModuleId: 'M_QUERY_LIBRARY',
                payload: {
                    payloadType: 'cancel',
                    content: {
                        operationId: requestId
                    }
                }
            };

            ngstomp.send(mesDestination, body, {});
        }

        function clearResult() {
            if ($scope.settings) {
                $scope.settings.columns = [];
                $scope.settings.source = [];
            }
        }

        function getExecuteResult(result) {

            $scope.currentQueryContext.queryInProgress = false;

            if (result.status == "COMPLETED") {
                $scope.applyState('SELECTED');

                $scope.error = false;
                $scope.errorMessage = '';

                appendToGrid(result.columns, result.result);

            } else if (result.status == "ERROR") {
                $scope.applyState('SELECTED');

                $scope.error = true;
                $scope.errorMessage = result.message;

            } else if (result.status == "HAS_MORE_DATA") {
                $scope.applyState('HAS_MORE_DATA');

                $scope.error = false;
                $scope.errorMessage = '';

                appendToGrid(result.columns, result.result);
            } else if (result.status == "CANCELLED") {
                $scope.applyState('SELECTED');

                $scope.error = false;
                $scope.errorMessage = '';
                $scope.infoMessage = 'Query cancelled';
                $timeout(function() {$scope.infoMessage = ''}, 2000);

            }
        }

        function appendToGrid(columns, result) {
            var previousGridResult = $scope.settings ? $scope.settings.source : [];
            if (!previousGridResult) {
                previousGridResult = [];
            }

            var newGridResult = [];
            for (i=0; i< previousGridResult.length; i++) {
               newGridResult.push(previousGridResult[i]);
            }
            for (i=0; i<result.length; i++) {
                newGridResult.push(result[i]);
            }

            //angular.extend(newGridResult, previousGridResult, result);

            $scope.settings =
            {
                width: '100%',
                height: '100%',
                columnsresize: true,
                sortable: true,
                selectionmode: 'multiplecellsextended',
                autowidth: true,
                columnsautoresize: true,
                source: newGridResult,
                columns: columns
            };
        }

        function getColumns(result) {
            var columns = [];
            angular.forEach(result[0], function (value, columnName) {
                var column = {
                    text: columnName,
                    dataField: columnName
                };

                columns.push(column)
            });

            return columns;
        }

        function applyState(state) {

            console.log('Query state: ' + $scope.state + ' -> ' + state);
            $scope.state = state;

            if (state == 'INITIAL') {
                $scope.currentQueryContext.executeButtonEnabled = false;
                $scope.currentQueryContext.executeButtonTitle = 'Execute Query';
                $scope.currentQueryContext.queryInProgress = false;
                $scope.currentQueryContext.cancelButtonVisible = false;
                $scope.currentQueryContext.pendingQuery = false;
            } else if (state == 'SELECTED') {
                $scope.currentQueryContext.executeButtonEnabled = true;
                $scope.currentQueryContext.executeButtonTitle = 'Execute Query';
                $scope.currentQueryContext.queryInProgress = false;
                $scope.currentQueryContext.cancelButtonVisible = false;
                $scope.currentQueryContext.pendingQuery = false;
            } else if (state == 'EXECUTING') {
                $scope.currentQueryContext.executeButtonEnabled = false;
                $scope.currentQueryContext.queryInProgress = true;
                $scope.currentQueryContext.cancelButtonVisible = true;
                $scope.currentQueryContext.pendingQuery = true;
            } else if (state == 'HAS_MORE_DATA') {
                $scope.currentQueryContext.executeButtonEnabled = true;
                $scope.currentQueryContext.executeButtonTitle = 'Next Rows';
                $scope.currentQueryContext.queryInProgress = false;
                $scope.currentQueryContext.cancelButtonVisible = true;
                $scope.currentQueryContext.pendingQuery = true;
            }
        }

        $rootScope.$on('setQuery', function (event, data) {
            $scope.clearResult();

            if ($scope.currentQueryContext.pendingQuery) {
                $scope.cancel($scope.currentQueryContext);
            }

            $scope.error = false;
            $scope.errorMessage = '';

            $scope.currentQueryContext.queryId = data.id;
            $scope.currentQueryContext.query = data.text;
            $scope.currentQueryContext.parameters = data.parameters;
            $scope.currentQueryContext.dataSources = data.dataSources;

            $scope.applyState('SELECTED');

        })
    }
})(window.jQuery);