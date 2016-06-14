(function () {
    'use strict';

    angular
        .module('queryLib')
        .service('QueryService', QueryService);

    QueryService.$injector = [];

    function QueryService() {
        return {
            getUpdateBody: getUpdateBody,
            create: create
        };

        function getUpdateBody(requestId, subDestination, id, fields) {
            console.log(fields);
            var body = {
                requestId: requestId,
                destination: subDestination,
                sessionId: requestId,
                username: 'unknown',
                targetModuleId: 'M_QUERY_LIBRARY',
                payload: {
                    payloadType: 'dataOperation',
                    content: {
                        id: id,
                        type: 'UPDATE',
                        entity: 'LibQuery',
                        fields: fields
                    }
                }
            };

            return body
        }

        function create() {

        }
    }
})();