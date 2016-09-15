var gulp = require('gulp');
var concat = require('gulp-concat');

gulp.task('stylesheets', function () {
    return gulp.src([
            'vendor/Bootflat/bootflat/css/bootflat.min.css',
            'vendor/Bootflat/css/bootstrap.min.css',
            'vendor/Bootflat/css/site.min.css',
            'vendor/jqwidgets/jqwidgets/styles/jqx.base.css',
            'vendor/jqwidgets/jqwidgets/styles/jqx.bootstrap.css',
            'vendor/angular-ui-grid/ui-grid.min.css',

            'css/custom.css'
        ])
        .pipe(concat('styles.css'))
        .pipe(gulp.dest('./'));
});

gulp.task('javascript', function () {
    return gulp.src([
            'vendor/Bootflat/js/jquery-1.10.1.min.js',
            'vendor/angular/angular.js',
            'vendor/jqwidgets/jqwidgets/jqxcore.js',
            'vendor/jqwidgets/jqwidgets/jqxgrid.js',
            'vendor/jqwidgets/jqwidgets/jqxtree.js',
            'vendor/jqwidgets/jqwidgets/jqxpanel.js',
            'vendor/jqwidgets/jqwidgets/jqxdata.js',
            'vendor/jqwidgets/jqwidgets/jqxscrollbar.js',
            'vendor/jqwidgets/jqwidgets/jqxbuttons.js',
            'vendor/jqwidgets/jqwidgets/jqxgrid.selection.js',
            'vendor/jqwidgets/jqwidgets/jqxgrid.sort.js',
            'vendor/jqwidgets/jqwidgets/jqxgrid.columnsresize.js',
            'vendor/jqwidgets/jqwidgets/jqxmenu.js',
            'vendor/jqwidgets/jqwidgets/jqxangular.js',
            'vendor/Bootflat/js/bootstrap.min.js',
            'vendor/Bootflat/js/respond.min.js',
            'vendor/Bootflat/js/site.min.js',
            'vendor/Bootflat/js/html5shiv.js',
            'vendor/Bootflat/js/bootflat/*.js',

            'vendor/angular-ui-router/release/angular-ui-router.js',
            'vendor/angular-ui-grid/ui-grid.min.js',
            'vendor/angular-modal-service/dst/angular-modal-service.js',
            'vendor/angular-uuid-service/angular-uuid-service.js',

            'vendor/sockjs/sockjs.js',
            'vendor/stomp-websocket/lib/stomp.js',
            'vendor/AngularStompDK/dist/angular-stomp.js',

            'js/custom.js',

            'app/app.module.js',
            'app/app.config.js',
            'app/app.constant.js',
            'app/controllers/*.js',
            'app/directives/*.js',

            'app/modules/**/module.js',
            'app/modules/**/services/*.js',
            'app/modules/**/routes/*.js',
            'app/modules/**/controllers/*.js',
            'app/modules/**/directives/*.js',
        ])
        .pipe(concat('scripts.js'))
        .pipe(gulp.dest('./'));
});

gulp.task('dist', ['stylesheets', 'javascript'], function () {
    return true
})