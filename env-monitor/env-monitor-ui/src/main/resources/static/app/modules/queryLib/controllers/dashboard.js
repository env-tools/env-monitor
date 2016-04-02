(function () {
    'use strict';

    angular
        .module('queryLib')
        .controller('Dashboard', Dashboard);

    Dashboard.$injector = ['$scope', '$stomp', '$resource'];

    function Dashboard($scope, $stomp, $resource) {

        init();

        function init() {
            $stomp.connect('/monitor', []).then(function () {
                var subDestination = '/subscribe/modules/M_QUERY_LIBRARY/tree/sergey';
                $stomp.subscribe(subDestination, function (message) {
                    console.log(message);
                });
            });
        }

        $scope.source = {};
        $scope.settings = {
            source: [
                {
                    label: "Private categories",
                    expanded: true,
                    items: [
                        {
                            id: 1,
                            label: "First private category",
                            items: [
                                {
                                    id: 2,
                                    label: "Second private category",
                                    items: [
                                        {
                                            id: 3,
                                            label: "Third private category"
                                        },
                                        {
                                            id: 1,
                                            label: "Query 1",
                                            html: '<div data-ng-click="executeQuery(1)"></div>'
                                        },
                                        {
                                            id: 2,
                                            label: "Query 2",
                                            html: '<div data-ng-click="executeQuery(2)"></div>'
                                        },
                                        {
                                            id: 3,
                                            html: '<div data-ng-click="executeQuery(3)">Query 3</div>'
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                {
                    label: "Public categories",
                    expanded: true,
                    items: [
                        {
                            id: 4,
                            label: "First public category",
                            items: [
                                {
                                    id: 5,
                                    label: "Second public category",
                                    items: [
                                        {
                                            id: 6,
                                            label: "Third public category"
                                        },
                                        {
                                            id: 4,
                                            label: "Query 1",
                                            html: '<div data-ng-click="executeQuery(4)"></div>'
                                        },
                                        {
                                            id: 5,
                                            label: "Query 2",
                                            html: '<div data-ng-click="executeQuery(5)"></div>'
                                        },
                                        {
                                            id: 6,
                                            label: "Query 3",
                                            html: '<div data-ng-click="executeQuery(6)"></div>'
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        };
    }
})();