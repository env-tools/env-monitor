(function ($) {
    'use strict';

    angular
        .module('queryLib')
        .controller('CategoryModal', CategoryModal);

    CategoryModal.$injector = ['$scope', 'ngstomp', 'rfc4122'];

    function CategoryModal($scope, ngstomp, rfc4122) {
        var requestId = rfc4122.v4();
        var subDestination = '/subscribe/modules/M_QUERY_LIBRARY/operation/' + requestId;

        $scope.close = close;
        $scope.create = create;
        $scope.update = update;
        $scope.depth = depth;

        init();

        function init() {
            ngstomp.subscribeTo(subDestination).callback(function (message) {
                var content = message['body']['payload']['jsonContent'];
                if (content != null && !content['error']['present']) {
                    close();
                    $('.alert-main.alert-success').removeClass('hide');
                    $('.alert-main.alert-danger').addClass('hide');
                } else {
                    close();
                    $('.alert-main.alert-success').addClass('hide');
                    $('.alert-main.alert-danger').removeClass('hide');
                }
            }).withBodyInJson().connect();
        }

        function depth(level) {
            var string = '';
            for (var i = 0; i < level; i++) {
                string += '.';
            }

            return string + ' ';
        }

        function create(category) {
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
                        entity: 'Category',
                        fields: category
                    }
                }
            };
            ngstomp.send(mesDestination, body, {});
        }

        function update(category) {
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
                        entity: 'Category',
                        fields: category
                    }
                }
            };
            ngstomp.send(mesDestination, body, {});
        }

        $scope.$on('categoryModal::create', function (event, data) {
            $scope.categories = data['categories'];
            $scope.title = 'Create category';
            $scope.entity_id = null;
            $scope.category = {
                title: null,
                description: null,
                owner: null,
                parentCategory_id: "null"
            };

            $('#category').modal('show');
        });

        $scope.$on('categoryModal::edit', function (event, data) {
            var element = data['element'];
            var parentCategoryId = element['parentCategory'] != null ? element['parentCategory'].toString() : "null";

            $scope.title = 'Edit category';
            $scope.categories = data['categories'];
            $scope.entity_id = element['id'];
            $scope.category = {
                title: element['title'],
                description: element['description'],
                owner: null,
                parentCategory_id: parentCategoryId
            };

            $('#category').modal('show');
        });

        function close() {
            $('#category').modal('hide');
        }
    }
})(window.jQuery);

