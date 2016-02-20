/* jshint -W117, -W030 */
describe('applications routes', function () {
    describe('state', function () {
        var view = 'app/applications/applications.html';

        beforeEach(function() {
            module('app.applications', bard.fakeToastr);
            bard.inject('$httpBackend', '$location', '$rootScope', '$state', '$templateCache');
        });

        beforeEach(function() {
            $templateCache.put(view, '');
        });

        bard.verifyNoOutstandingHttpRequests();

        it('should map state dashboard to url / ', function() {
            expect($state.href('applications', {})).to.equal('/');
        });

        it('should map /applications route to dashboard View template', function () {
            expect($state.get('applications').templateUrl).to.equal(view);
        });

        it('of dashboard should work with $state.go', function () {
            $state.go('applications');
            $rootScope.$apply();
            expect($state.is('applications'));
        });
    });
});
