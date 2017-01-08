(function () {
    'use strict';

    angular
        .module('queryLib')
        .controller('RemoveModal', RemoveModal);

    RemoveModal.$injector = ['$scope', 'ngstomp', 'rfc4122', '$element', 'close', 'entity', 'fields'];

    function RemoveModal($scope, ngstomp, rfc4122, $element, close, entity, fields) {
        var requestId = rfc4122.v4();
        var subOperationDestination = '/subscribe/modules/M_QUERY_LIBRARY/operation/' + requestId;

        $scope.cancel = cancel;
        $scope.remove = remove;

        init();

        function init() {
            ngstomp.subscribeTo(subOperationDestination).callback(operationResult).withBodyInJson().connect();
        }

        function remove() {
            var mesDestination = '/message/modulerequest';
            var body = {
                requestId: requestId,
                destination: subOperationDestination,
                sessionId: requestId,
                username: 'unknown',
                targetModuleId: 'M_QUERY_LIBRARY',
                payload: {
                    payloadType: 'dataOperation',
                    content: {
                        id: fields['id'],
                        type: 'DELETE',
                        entity: entity
                    }
                }
            };
            ngstomp.send(mesDestination, body, {});
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
                    message: "At first you must to delete child categories and queries in them."
                };
            }

            closeWithResult(result);
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

