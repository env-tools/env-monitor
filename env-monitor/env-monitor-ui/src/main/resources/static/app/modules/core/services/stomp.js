(function () {
    'use strict';

    angular
        .module('core')
        .service('Stomp', Stomp);

    Stomp.$injector = ['$rootScope', '$stomp'];

    function Stomp($rootScope, $stomp) {
        return {
            connect: connect
        };

        function connect(endpoint) {
            $stomp.connect(endpoint, []).then(function (frame) {
                $rootScope.$broadcast('stomp::connect', {
                    frame: frame,
                    endpoint: endpoint
                });
            });
        }
    }
})();