(function () {
    'use strict';

    angular
        .module('queryLib')
        .service('utils', utils);

    utils.$injector = [];

    function utils() {
        return {
            depth: depth
        };

        function depth(level) {
            var string = '';
            for (var i = 0; i < level; i++) {
                string += '.';
            }

            return string + ' ';
        }
    }
})();