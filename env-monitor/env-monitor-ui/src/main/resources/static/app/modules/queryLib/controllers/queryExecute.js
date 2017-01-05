(function ($) {
    'use strict';

    angular
        .module('queryLib')
        .controller('QueryExecute', QueryExecute);

    QueryExecute.$injector = ['$scope', '$rootScope', 'ngstomp', 'rfc4122', 'QueryService'];

    function QueryExecute($scope, $rootScope, ngstomp, rfc4122, QueryService) {
        var requestId = rfc4122.v4();
        var subDestination = '/subscribe/modules/M_QUERY_LIBRARY/exec/' + requestId;
        var subOperationDestination = '/subscribe/modules/M_QUERY_LIBRARY/operation/' + requestId;

        $scope.execute = execute;
        $scope.addParam = addParam;
        $scope.saveQuery = saveQuery;

        $scope.params = {
            text: '',
            timeout: 1000,
            rows: 50,
            parameters: []
        };

        $scope.resultExecute = {};
        $scope.columns = {};
        $scope.error = false;
        $scope.errorMessage = '';

        init();

        function init() {
            ngstomp.subscribeTo(subDestination).callback(function (message) {
                $scope.$apply(getExecuteResult(message.body.payload.jsonContent))
            }).withBodyInJson().connect();
        }

        function addParam(newParam) {
            var param = {
                name: newParam.name,
                type: newParam.type,
                value: newParam.value
            };
            $scope.params.parameters.push(param);
        }

        function saveQuery(params) {
            var mesDestination = '/message/modulerequest';
            var fields = {
                text: params.text,
                queryParams: prepareToUpdate(params.parameters)
            };

            var body = QueryService.getUpdateBody(requestId, subOperationDestination, params.id, fields);
            ngstomp.send(mesDestination, body, {});
        }

        function prepareToUpdate(parameters) {
            var result = [];
            angular.forEach(parameters, function(values) {
                var obj = {
                    name: values.name,
                    type: values.type
                };

                result.push(obj);
            });

            return result;
        }

        function formatQueryParameters(parameters) {
            var result = {};
            angular.forEach(parameters, function(values) {
                result[values.name]  = values.value;
            });

            return result;
        }

        function execute(params) {
            var parameters = formatQueryParameters(params.parameters);
            var mesDestination = '/message/modulerequest';
            var body = {
                requestId: requestId,
                destination: subDestination,
                sessionId: rfc4122.v4(),
                username: 'unknown',
                targetModuleId: 'M_QUERY_LIBRARY',
                payload: {
                    payloadType: 'execute',
                    content: {
                        query: params.text,
                        queryType: "JDBC",
                        timeOutMs: params.timeout,
                        rowCount: params.rows,
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
            ngstomp.send(mesDestination, body, {});
        }

        function getExecuteResult(result) {
            if (result.status == "COMPLETED") {
                $scope.error = false;
                $scope.errorMessage = '';

                var columns = getColumns(result.result);

                $scope.settings =
                {
                    width: '100%',
                    height: '100%',
                    columnsresize: true,
                    sortable: true,
                    selectionmode: 'multiplecellsextended',
                    autowidth: true,
                    columnsautoresize: true,
                    source: result.result,
                    columns: columns
                };
            } else if (result.status == "ERROR") {
                $scope.error = true;
                $scope.errorMessage = result.message;
            }
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

        $rootScope.$on('setQuery', function (event, data) {
            $scope.params.id = data.id;
            $scope.params.text = data.text;
            $scope.params.parameters = data.parameters;
        })
    }
})(window.jQuery);