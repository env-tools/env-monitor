(function ($) {
    'use strict';

    angular
        .module('queryLib')
        .controller('QueryModal', QueryModal);

    QueryModal.$injector = ['$scope', '$stomp', 'rfc4122', 'Stomp'];

    function QueryModal($scope, $stomp, rfc4122, Stomp) {
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
            $stomp.send(mesDestination, body, {});
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
                        type: 'UPDATE',
                        entity: 'LibQuery',
                        fields: query
                    }
                }
            };
            $stomp.send(mesDestination, body, {});
        }

        $scope.$on('queryModal::create', function() {
            $scope.title = 'Create category';
            $scope.query = {
                title: '',
                description: '',
                text: ''
            };

            $('#query').modal('show');
        });

        $scope.$on('queryModal::edit', function(event, data) {
            $scope.title = 'Edit category';
            $scope.query = {
                id: data.id,
                title: data.title,
                description: data.description,
                text: data.text
            };

            $('#query').modal('show');
        });

        function close() {
            $('#query').modal('hide');
        }
    }
})(window.jQuery);

