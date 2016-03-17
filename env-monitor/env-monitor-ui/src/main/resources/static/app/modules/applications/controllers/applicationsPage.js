(function() {
  'use strict';

  angular
    .module('applications')
    .controller('ApplicationsPageController', ApplicationsPageController);

  ApplicationsPageController.$inject = ['$scope', '$stomp'];

  function ApplicationsPageController($scope, $stomp) {

    $scope.applications = [];
    $scope.platforms = [];
    $scope.environments = [];
    $scope.filters = {
      selectedPlatform: null,
      selectedEnvironment: null
    };

    $scope.selectedPlatformChanged = function () {
      var pattern = '/call/modules/M_APPLICATIONS/data/platforms/{platform}/environments';

      $stomp.subscribe(pattern.replace("{platform}", $scope.filters.selectedPlatform.id),
        function (msg, headers, res) {
          $scope.$apply(function () {
            $scope.environments = msg.payload.jsonContent;
            $scope.filters.selectedEnvironment = $scope.environments[0];

            $scope.selectedEnvironmentChanged();

          });

          console.log('Call result for environments: ' + JSON.stringify(msg.payload.jsonContent));
        }, {});

    };

    $scope.selectedEnvironmentChanged = function () {

      var pattern = '/subscribe/modules/M_APPLICATIONS/data/platforms/{platformId}/environments/{environmentId}/applications';

      if (!$scope.filters.selectedPlatform) {
        return;
      }

      if ($scope.currentApplicationsSubscribeId) {
        //TODO: check order subsc/unsubscr
        $stomp.unsubscribe($scope.currentApplicationsSubscribeId);
      }

      $scope.currentApplicationsSubscribeId = $stomp.subscribe(
        pattern
          .replace("{platformId}",
            $scope.filters.selectedPlatform.id)
          .replace("{environmentId}",
            $scope.filters.selectedEnvironment.id),
        function (msg, headers, res) {
          $scope.$apply(function () {
            $scope.applications = msg.payload.jsonContent;
          });

          console.log('New data for subscription! ' + JSON.stringify(/* $scope.applications */ msg.payload.jsonContent));
        }, {});

    }


    $stomp.connect('/monitor', [])
      // frame = CONNECTED headers
      .then(function (frame) {
        console.log('Connected!');

        //TODO create wrapper around $stomp to clearly distinguish CALL and SUBSCRIBE

        //TODO configure TomCat to catch hot code updates immediately

        $stomp.subscribe('/call/modules/M_APPLICATIONS/data/platforms', function (msg, headers, res) {
          $scope.$apply(function () {
            $scope.platforms = msg.payload.jsonContent;
            $scope.filters.selectedPlatform = $scope.platforms[0];
          });
          console.log('Call result for platforms: ' + JSON.stringify(msg.payload.jsonContent));


          var pattern = '/call/modules/M_APPLICATIONS/data/platforms/{platformId}/environments'
          $stomp.subscribe(pattern.replace('{platformId}', $scope.filters.selectedPlatform.id),
            function (msg, headers, res) {
              $scope.$apply(function () {
                $scope.environments = msg.payload.jsonContent;
                $scope.filters.selectedEnvironment = $scope.environments[0];
              });

              console.log('Call result for environments: ' + JSON.stringify(msg.payload.jsonContent));

              $scope.selectedEnvironmentChanged();

            }, {});

        }, {});

//                            $stomp.subscribe('/subscribe/modules/M_APPLICATIONS/data/platforms/PLAT1/environments/PL1-ENV1/applications',
//                                    function (msg, headers, res) {
//                                        $scope.$apply(function () {
//                                            $scope.applications = msg.payload.jsonContent;
//                                        });
//
//                                        console.log('New data for subscription! ' + JSON.stringify(/* $scope.applications */ msg.payload.jsonContent));
//                                    }, { });

      });
  }
})();