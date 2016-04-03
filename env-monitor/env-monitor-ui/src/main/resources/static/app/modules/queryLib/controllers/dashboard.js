(function () {
    'use strict';

    angular
        .module('queryLib')
        .controller('Dashboard', Dashboard);

    Dashboard.$injector = ['$scope', '$stomp'];

    function Dashboard($scope, $stomp) {
        $scope.source = {};
        $scope.settings = {
            source: $scope.source
        };

        init();

        function init() {
            $stomp.connect('/monitor', []).then(function () {
                var subDestination = '/subscribe/modules/M_QUERY_LIBRARY/tree/sergey';
                $stomp.subscribe(subDestination, function (message) {
                    var publicTree = [];
                    angular.forEach(message.payload.jsonContent[0], function (category) {
                        publicTree.push(createTree(category));
                    });

                    var privateTree = [];
                    angular.forEach(message.payload.jsonContent[1], function (category) {
                        privateTree.push(createTree(category));
                    });

                    $scope.$apply(function() {
                        $scope.source = [
                            {
                                label: "Public categories",
                                icon: "/images/treeWidget/folder.png",
                                items: publicTree
                            },
                            {
                                label: "Private categories (sergey)",
                                icon: "/images/treeWidget/folder.png",
                                items: privateTree
                            }
                        ];
                    });
                });
            });
        }

        function createTree(category) {
            var result = {
                html: '<div title="' + category.description + '" style="padding-right: 20px;">' + category.title + '</div>',
                icon: "/images/treeWidget/folder.png",
                items: []
            };
            var categories = [];
            var queries = getQueryArray(category.queries);

            if (category.hasOwnProperty("childCategories") && category["childCategories"].length > 0) {
                angular.forEach(category["childCategories"], function (category) {
                    categories.push(createTree(category));
                });
            }

            if (categories || queries) {
                result["items"] = categories.concat(queries);
            }

            return result;
        }

        function getQueryArray(queries) {
            var result = [];
            angular.forEach(queries, function(query) {
                var _query = {
                    html: '<div id="query_' + query.id + '" title="' + query.description + '" style="padding-right: 20px;">' + query.title + '</div>',
                    icon: "/images/treeWidget/sql.png",
                };
                result.push(_query);
            });

            return result;
        }
    }
})();