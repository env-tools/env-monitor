(function () {
    'use strict';

    angular
        .module('queryLib')
        .controller('Tree', Tree);

    Tree.$injector = ['$scope', '$stomp', 'Stomp'];

    function Tree($scope, $stomp, Stomp) {
        $scope.source = {};
        $scope.settings = {
            width: 300,
            source: $scope.source
        };

        var subscribes = {};

        init();

        function init() {
            Stomp.connect('/monitor');
            $scope.$on('stomp::connect', function (event, data) {
                if (data["endpoint"] == "/monitor") {
                    var subDestination = '/subscribe/modules/M_QUERY_LIBRARY/tree/sergey';
                    if (!subscribes[subDestination]) {
                        var sub = $stomp.subscribe(subDestination, function (message) {
                            getMessage(message);
                        });
                        subscribes[subDestination] = sub;
                    }
                }
            });
        }

        function getMessage (message) {
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
                        items: publicTree,
                        expanded: true,
                    },
                    {
                        label: "Private categories (sergey)",
                        icon: "/images/treeWidget/folder.png",
                        items: privateTree,
                    }
                ];
            });
        }

        function createTree(category) {
            var result = {
                html: '<div title="' + category.description + '" style="padding-right: 20px;">' + category.title + '</div>',
                icon: "/images/treeWidget/folder.png",
                expanded: true,
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
                    html: '<div data-text="' + query.text + '" id="query_' + query.id + '" title="' + query.description + '" style="padding-right: 20px;">' + query.title + '</div>',
                    icon: "/images/treeWidget/sql.png",
                };
                result.push(_query);
            });

            return result;
        }
    }
})();