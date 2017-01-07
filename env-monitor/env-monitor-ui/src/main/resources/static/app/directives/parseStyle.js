(function () {
    'use strict';

    angular
        .module('envMonitor')
        .directive('parseStyle', parseStyle);

    parseStyle.$injector = ['$interpolate'];

    function parseStyle($interpolate) {
        return function (scope, elem) {
            var exp = $interpolate(elem.html()),
                watchFunc = function () {
                    return exp(scope);
                };

            scope.$watch(watchFunc, function (html) {
                elem.html(html);
            });
        }
    }
})();