(function ($) {
    'use strict';

    angular
        .module('queryLib')
        .controller('QueryModal', QueryModal);

    QueryModal.$injector = ['$scope', 'ngstomp', 'rfc4122'];

    function QueryModal($scope, ngstomp, rfc4122) {
        var subscribes = {};
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

        $scope.$on('queryModal::create', function (event, data) {
            var categoryId = data['parentCategoryId'] != null ? data['parentCategoryId'].toString() : "null";
            $scope.title = 'Create query';
            $scope.entity_id = null;
            $scope.categories = data['categories'];
            $scope.query = {
                title: '',
                description: '',
                text: '',
                category_id: categoryId
            };

            $('#query').modal('show');
        });

        $scope.$on('queryModal::edit', function (event, data) {
            var element = data['element'];
            var categoryId = element['category'] != null ? element['category'].toString() : "null";
            $scope.title = 'Edit query';
            $scope.entity_id = element.id;
            $scope.categories = data['categories'];
            $scope.query = {
                title: element.title,
                description: element.description,
                text: element.text,
                category_id: categoryId
            };

            $('#query').modal('show');
        });

        function close() {
            $('#query').modal('hide');
        }
    }
})(window.jQuery);

