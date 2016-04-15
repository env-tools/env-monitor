(function ($) {
    'use strict';

    angular
        .module('queryLib')
        .controller('CategoryModal', CategoryModal);

    CategoryModal.$injector = ['$scope', '$stomp', 'rfc4122', 'Stomp'];

    function CategoryModal($scope, $stomp, rfc4122, Stomp) {
        var subscribes = {};
        var requestId = rfc4122.v4();
        var subDestination = '/subscribe/modules/M_QUERY_LIBRARY/operation/' + requestId;

        $scope.close = close;
        $scope.create = create;
        $scope.update = update;

        init();

        function init() {
            Stomp.connect('/monitor');

            $scope.$on('stomp::connect', function (event, data) {
                if (data["endpoint"] == "/monitor") {
                    if (!subscribes[subDestination]) {
                        var sub = $stomp.subscribe(subDestination, function (message) {
                            console.log(message)
                        });
                        subscribes[subDestination] = sub;
                    }
                }
            });
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
            $stomp.send(mesDestination, body, {});
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
                        type: 'UPDATE',
                        entity: 'Category',
                        fields: category
                    }
                }
            };
            $stomp.send(mesDestination, body, {});
        }

        $scope.$on('categoryModal::create', function () {
            $scope.title = 'Create category';
            $scope.category = {
                title: '',
                description: ''
            };

            $('#category').modal('show');
        });

        $scope.$on('categoryModal::edit', function (event, data) {
            $scope.title = 'Edit category';
            $scope.category = {
                id: data.id,
                title: data.title,
                description: data.description
            };

            $('#category').modal('show');
        });

        function close() {
            $('#category').modal('hide');
        }
    }
})(window.jQuery);

