(function ($) {
    'use strict';

    angular
        .module('queryLib')
        .controller('Tree', Tree);

    Tree.$injector = ['$scope', '$rootScope', 'ngstomp', 'rfc4122'];

    function Tree($scope, $rootScope, ngstomp, rfc4122) {
        var requestId = rfc4122.v4();
        var subOperationDestination = '/subscribe/modules/M_QUERY_LIBRARY/operation/' + requestId;
        var allQueries = {};
        var allCategories = {};
        var categoriesFormat = {};


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
            var subDestination = '/subscribe/modules/M_QUERY_LIBRARY/tree/sergey';
            ngstomp.subscribeTo(subDestination).callback(getMessage).withBodyInJson().connect();
            ngstomp.subscribeTo(subOperationDestination).callback(getOperationResult).withBodyInJson().connect();
        }

        function getOperationResult(message) {
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
        }

        function getMessage(message) {
            allCategories = {};
            var publicTree = [];
            angular.forEach(message.body.payload.jsonContent[0], function (category) {
                publicTree.push(createTree(category, 1));
            });
            categoriesFormat["Public categories"] = allCategories;

            //TODO: Переделать это убожество как только руки дойдут
            var tmp = allCategories;

            allCategories = {};
            var privateTree = [];
            angular.forEach(message.body.payload.jsonContent[1], function (category) {
                privateTree.push(createTree(category, 1));
            });
            categoriesFormat["Private categories (sergey)"] = allCategories;

            allCategories = $.extend({}, tmp, allCategories);

            $scope.$apply(function () {
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

        function createTree(category, depthLevel) {
            category['depthLevel'] = depthLevel;
            var categoryId = 'category_' + category.id;
            allCategories[categoryId] = category;

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
                    categories.push(createTree(category, depthLevel + 1));
                });
            }

            if (categories || queries) {
                result["items"] = categories.concat(queries);
            }

            return result;
        }

        function getQueryArray(queries) {
            var result = [];
            angular.forEach(queries, function (query) {
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
                $scope.$broadcast('queryModal::edit', {element: allQueries[$scope.itemSelect], categories: categoriesFormat});
            } else {
                $scope.$broadcast('categoryModal::edit', {element: allCategories[$scope.itemSelect], categories: categoriesFormat});
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
                destination: subOperationDestination,
                sessionId: requestId,
                username: 'unknown',
                targetModuleId: 'M_QUERY_LIBRARY',
                payload: {
                    payloadType: 'dataOperation',
                    content: {
                        id: fields['id'],
                        type: 'DELETE',
                        entity: entity
                    }
                }
            };
            ngstomp.send(mesDestination, body, {});
        }

        function categoryCreate() {
            $scope.$broadcast('categoryModal::create', {categories: allCategories})
        }

        function queryCreate() {
            $scope.$broadcast('queryModal::create', {categories: allCategories})
        }
    }
})(window.jQuery);

