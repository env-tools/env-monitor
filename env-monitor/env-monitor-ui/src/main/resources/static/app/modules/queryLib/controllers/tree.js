(function () {
    'use strict';

    angular
        .module('queryLib')
        .controller('Tree', Tree);

    Tree.$injector = ['$scope', '$rootScope', 'ngstomp', 'ModalService'];

    function Tree($scope, $rootScope, ngstomp, ModalService) {
        var allQueries = {};
        var allCategories = {};
        var categoriesFormat = {};


        $scope.createCategory = createCategory;
        $scope.createQuery = createQuery;
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
        }

        function getMessage(message) {

            var publicTree = [];
            var publicCategories = {};
            angular.forEach(message.body.payload.jsonContent[0], function (category) {
                publicTree.push(createTree(category, 1, publicCategories));
            });
            categoriesFormat["Public categories"] = publicCategories;

            var privateTree = [];
            var privateCategories = {};
            angular.forEach(message.body.payload.jsonContent[1], function (category) {
                privateTree.push(createTree(category, 1, privateCategories));
            });
            categoriesFormat["Private categories (user01)"] = privateCategories;

            $scope.source = [
                {
                    html: '<div class="tree-item" title="Public categories" style="padding-right: 20px;">Public categories</div>',
                    icon: "/images/treeWidget/folder.png",
                    items: publicTree,
                    expanded: true
                },
                {
                    html: '<div class="tree-item" title="Private categories (sergey)" style="padding-right: 20px;">Private categories (sergey)</div>',
                    icon: "/images/treeWidget/folder.png",
                    items: privateTree
                }
            ];

            allCategories = angular.extend({}, publicCategories, privateCategories);
        }

        function createTree(category, depthLevel, _categories) {
            var categoryId = 'category_' + category.id;

            category['depthLevel'] = depthLevel;
            _categories[categoryId] = category;

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
                    categories.push(createTree(category, depthLevel + 1, _categories));
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
                    icon: "/images/treeWidget/sql.png"
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
                editQuery();
            } else {
                editCategory();
            }
        }

        function getParentCategory() {
            if (~$scope.itemSelect.indexOf("query_")) {
                return allQueries[$scope.itemSelect]['category'];
            } else if (~$scope.itemSelect.indexOf("category_")) {
                return allCategories[$scope.itemSelect]['id'];
            } else {
                return "null";
            }
        }

        function editQuery() {
            var element = allQueries[$scope.itemSelect];
            var categoryId = element['category'] != null ? element['category'].toString() : "null";

            ModalService.showModal({
                templateUrl: "/app/modules/queryLib/templates/modals/query.html",
                controller: "QueryModal",
                inputs: {
                    categories: categoriesFormat,
                    query: {
                        title: element['title'],
                        description: element['description'],
                        text: element['text'],
                        category_id: categoryId
                    },
                    entity_id: element['id'],
                    title: 'Edit query'
                }
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(closeModal);
            });
        }

        function editCategory() {
            var element = allCategories[$scope.itemSelect];
            var parentCategoryId = element['parentCategory'] != null ? element['parentCategory'].toString() : "null";

            ModalService.showModal({
                templateUrl: "/app/modules/queryLib/templates/modals/category.html",
                controller: "CategoryModal",
                inputs: {
                    categories: categoriesFormat,
                    category: {
                        title: element['title'],
                        description: element['description'],
                        owner: null,
                        parentCategory_id: parentCategoryId
                    },
                    entity_id: element['id'],
                    title: 'Edit category'
                }
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(closeModal);
            });
        }

        function createCategory() {
            var parentCategory = getParentCategory();
            var parentCategoryId = (parentCategory != "null") ? parentCategory.toString() : "null";

            ModalService.showModal({
                templateUrl: "/app/modules/queryLib/templates/modals/category.html",
                controller: "CategoryModal",
                inputs: {
                    categories: categoriesFormat,
                    category: {
                        title: null,
                        description: null,
                        owner: null,
                        parentCategory_id: parentCategoryId
                    },
                    entity_id: null,
                    title: 'Create category'
                }
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(closeModal);
            });
        }

        function closeModal(result) {
            $rootScope.$emit('showAlert', result);
        }

        function createQuery() {
            var parentCategory = getParentCategory();
            var categoryId = (parentCategory != "null") ? parentCategory.toString() : "null";

            ModalService.showModal({
                templateUrl: "/app/modules/queryLib/templates/modals/query.html",
                controller: "QueryModal",
                inputs: {
                    categories: categoriesFormat,
                    query: {
                        title: '',
                        description: '',
                        text: '',
                        category_id: categoryId
                    },
                    entity_id: null,
                    title: 'Create query'
                }
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(closeModal);
            });
        }

        function remove(){
            var  fields, entity;
            if (~$scope.itemSelect.indexOf("query_")) {
                fields = allQueries[$scope.itemSelect];
                entity = "LibQuery";
            } else {
                fields = allCategories[$scope.itemSelect];
                entity = "Category";
            }

            ModalService.showModal({
                templateUrl: "/app/modules/queryLib/templates/modals/remove.html",
                controller: "RemoveModal",
                inputs: {
                    fields: fields,
                    entity: entity
                }
            }).then(function(modal) {
                modal.element.modal();
                modal.close.then(closeModal);
            });
        }
    }
})();

