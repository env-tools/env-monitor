var gulp = require('gulp');
var concat = require('gulp-concat');

gulp.task('stylesheets', function () {
  return gulp.src([
    'vendor/Bootflat/bootflat/css/bootflat.min.css',
    'vendor/Bootflat/css/bootstrap.min.css',
    'vendor/Bootflat/css/site.min.css',

    'css/custom.css',
  ])
    .pipe(concat('styles.css'))
    .pipe(gulp.dest('./'));
});

gulp.task('javascript', function () {
   return gulp.src([
    'vendor/Bootflat/js/jquery-1.10.1.min.js',
    'vendor/Bootflat/js/bootstrap.min.js',
    'vendor/Bootflat/js/respond.min.js',
    'vendor/Bootflat/js/site.min.js',
    'vendor/Bootflat/js/html5shiv.js',
    'vendor/Bootflat/js/bootflat/*.js',
    'vendor/angular/angular.js',
    'vendor/angular-ui-router/release/angular-ui-router.js',
    'vendor/angular-resource/angular-resource.js',
    'vendor/sockjs/sockjs.js',
    'vendor/stomp-websocket/lib/stomp.js',
    'vendor/ng-stomp/ng-stomp.js',

    'js/custom.js',

    'app/app.module.js',
    'app/app.config.js',
    'app/app.constant.js',

    'app/modules/**/module.js',
    'app/modules/**/routes/*.js',
    'app/modules/**/controllers/*.js',
    'app/modules/**/services/*.js',
    'app/modules/**/directives/*.js',
   ])
    .pipe(concat('scripts.js'))
    .pipe(gulp.dest('./'));
});

gulp.task('dist', ['stylesheets', 'javascript'], function () {
    return true
})