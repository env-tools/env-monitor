(function () {
    'use strict';

    angular
        .module('queryLib')
        .controller('CategoryModal', CategoryModal);

    CategoryModal.$injector = ['$scope', 'ngstomp', 'rfc4122', 'utils', '$element', 'close', 'categories', 'category', 'entity_id', 'title'];

    function CategoryModal($scope, ngstomp, rfc4122, utils, $element, close, categories, category, entity_id, title) {
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
            $scope.category = category;

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

        function create(category) {
            var mesDestination = '/message/modulerequest';

            if (category['parentCategory_id'] == "null") {
                delete(category['parentCategory_id']);
            }

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
                        entity: 'Category',
                        fields: category
                    }
                }
            };
            ngstomp.send(mesDestination, body, {});
        }

        function update(category) {
            var mesDestination = '/message/modulerequest';

            if (category['parentCategory_id'] == "null") {
                delete(category['parentCategory_id']);
            }

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
                        entity: 'Category',
                        fields: category
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

