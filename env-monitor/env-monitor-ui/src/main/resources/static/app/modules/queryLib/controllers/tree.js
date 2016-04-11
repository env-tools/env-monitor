(function () {
    'use strict';

    angular
        .module('queryLib')
        .controller('Tree', Tree);

    Tree.$injector = ['$scope', '$rootScope', '$stomp', 'rfc4122', 'Stomp'];

    function Tree($scope, $rootScope, $stomp, rfc4122, Stomp) {
        var subscribes = {};
        var requestId = rfc4122.v4();
        var subDestination = '/subscribe/modules/M_QUERY_LIBRARY/operation/' + requestId;
        var allQueries = {};
        var allCategories = {};

        $scope.categoryCreate = categoryCreate;
        $scope.queryCreate = queryCreate;
        $scope.edit = edit;
        $scope.remove = remove;

        $scope.itemSelect = '';
        $scope.source = {};
        $scope.settings = {
            width: 300,
            source: $scope.source
        };

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
            var categoryId = 'category_' + category.id;
            var result = {
                html: '<div class="tree-item" id="' + categoryId + '" title="' + category.description + '" style="padding-right: 20px;">' + category.title + '</div>',
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

            allCategories[categoryId] = category;

            return result;
        }

        function getQueryArray(queries) {
            var result = [];
            angular.forEach(queries, function(query) {
                var queryId = 'query_' + query.id;
                var _query = {
                    html: '<div class="tree-item" id="' + queryId + '" title="' + query.description + '" style="padding-right: 20px;">' + query.title + '</div>',
                    icon: "/images/treeWidget/sql.png",
                };
                result.push(_query);
                allQueries[queryId] = query;
            });

            return result;
        }

        $scope.$on('itemSelect', function (event, data) {
            var element = data.args.element;
            var treeItem = angular.element(element).find('.tree-item');
            var treeItemId = $scope.itemSelect = treeItem[0].id;
            if (~treeItemId.indexOf("query_")) {
                $rootScope.$emit('setQuery', allQueries[treeItemId]);
            }
        });

        function edit() {
            if (~$scope.itemSelect.indexOf("query_")) {
                $scope.$broadcast('queryModal::edit', allQueries[$scope.itemSelect]);
            } else {
                $scope.$broadcast('categoryModal::edit', allCategories[$scope.itemSelect]);
            }
        }

        function remove() {
            var fields = {};
            var entity = "";
            if (~$scope.itemSelect.indexOf("query_")) {
                fields = allQueries[$scope.itemSelect];
                entity = "LibQuery";
            } else {
                fields = allCategories[$scope.itemSelect];
                entity = "Category";
            }
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
                        type: 'DELETE',
                        entity: entity,
                        fields: {
                            id: fields['id']
                        }
                    }
                }
            };
            $stomp.send(mesDestination, body, {});
        }

        function categoryCreate() {
            $scope.$broadcast('categoryModal::create')
        }

        function queryCreate() {
            $scope.$broadcast('queryModal::create')
        }
    }
})();

