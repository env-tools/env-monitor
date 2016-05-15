(function () {
    'use strict';

    angular
        .module('queryLib')
        .controller('QueryModal', QueryModal);

    QueryModal.$injector = ['$scope', 'ngstomp', 'rfc4122', 'utils', '$element', 'close', 'categories', 'category', 'entity_id', 'title'];

    function QueryModal($scope, ngstomp, rfc4122, utils, $element, close, categories, query, entity_id, title) {
        var requestId = rfc4122.v4();
        var subDestination = '/subscribe/modules/M_QUERY_LIBRARY/operation/' + requestId;

        $scope.cancel = cancel;
        $scope.create = create;
        $scope.update = update;
        $scope.depth = utils.depth;

        init();

        function init() {
            $scope.categories = categories;
            $scope.title = title;
            $scope.entity_id = entity_id;
            $scope.query = query;

            ngstomp.subscribeTo(subDestination).callback(operationResult).withBodyInJson().connect();
        }

        function operationResult(message) {
            var result;
            var content = message['body']['payload']['jsonContent'];

            if (content != null && !content['error']['present']) {
                result = {
                    type: "success",
                    message: "Changes was success applied!"
                };
            } else {
                result = {
                    type: "danger",
                    message: "Changes was not success applied!"
                };
            }

            closeWithResult(result);
        }

        function create(query) {
            var mesDestination = '/message/modulerequest';
            var body = {
                requestId: requestId,
                destination: subDestination,
                sessionId: requestId,
                username: 'unknown',
                targetModuleId: 'M_QUERY_LIBRARY',
                payload: {
                    payloadType: 'dataOperation',
                    content: {
                        type: 'CREATE',
                        entity: 'LibQuery',
                        fields: query
                    }
                }
            };
            ngstomp.send(mesDestination, body, {});
        }

        function update(query) {
            var mesDestination = '/message/modulerequest';
            var body = {
                requestId: requestId,
                destination: subDestination,
                sessionId: requestId,
                username: 'unknown',
                targetModuleId: 'M_QUERY_LIBRARY',
                payload: {
                    payloadType: 'dataOperation',
                    content: {
                        id: $scope.entity_id,
                        type: 'UPDATE',
                        entity: 'LibQuery',
                        fields: query
                    }
                }
            };
            ngstomp.send(mesDestination, body, {});
        }

        function closeWithResult(result) {
            $element.modal('hide');
            close(result, 500);
        }

        function cancel() {
            $element.modal('hide');
            close({}, 500);
        }
    }
})();

