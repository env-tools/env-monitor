var gulp = require('gulp');
var concat = require('gulp-concat');
var replace = require('gulp-replace-task');
var insert = require('gulp-insert');
var rename = require('gulp-rename');
//var debug = require('gulp-debug');

var yargs = require('yargs');
var propertiesReader = require('properties-reader');

var paths = {
    cssSrc: [
        'node_modules/bootflat/bootflat/css/bootflat.min.css',
        'node_modules/bootflat/css/bootstrap.min.css',
        'node_modules/bootflat/css/site.min.css',
        'node_modules/jqwidgets-framework/jqwidgets/styles/jqx.base.css',
        'node_modules/jqwidgets-framework/jqwidgets/styles/jqx.bootstrap.css',
        'node_modules/angular-ui-grid/ui-grid.min.css',

        'css/custom.css'
    ],
    jsSrc: [
        'node_modules/bootflat/js/jquery-1.10.1.min.js',
        'node_modules/angular/angular.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxcore.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxgrid.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxtree.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxpanel.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxdata.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxscrollbar.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxbuttons.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxgrid.selection.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxgrid.sort.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxgrid.columnsresize.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxmenu.js',
        'node_modules/jqwidgets-framework/jqwidgets/jqxangular.js',
        'node_modules/bootflat/js/bootstrap.min.js',
        'node_modules/bootflat/js/respond.min.js',
        'node_modules/bootflat/js/site.min.js',
        'node_modules/bootflat/js/html5shiv.js',
        'node_modules/bootflat/js/bootflat/*.js',

        'node_modules/angular-ui-router/release/angular-ui-router.js',
        'node_modules/angular-ui-grid/ui-grid.min.js',
        'node_modules/angular-modal-service/dst/angular-modal-service.js',
        'node_modules/angular-uuid-service/angular-uuid-service.js',

        'node_modules/stompjs/lib/stomp.js',
        'node_modules/AngularStompDK/dist/angular-stomp.js',

        'js/custom.js',
        'js/sockjs/sockjs.js',

        'app/app.module.js',
        'app/app.config.js',
        'app/app.constant.js',
        'app/controllers/*.js',
        'app/directives/*.js',

        'app/modules/**/module.js',
        'app/modules/**/services/*.js',
        'app/modules/**/routes/*.js',
        'app/modules/**/controllers/*.js',
        'app/modules/**/directives/*.js'
    ],
    htmlTemplateSrc: 'index-template.html'

};


gulp.task('stylesheets', function () {
    return gulp.src(paths.cssSrc)
        .pipe(concat('styles.css'))
        .pipe(gulp.dest('./'));
});

gulp.task('javascript', function () {
    return gulp.src(
            paths.jsSrc
        )
        .pipe(concat('scripts.js'))
        .pipe(gulp.dest('./'));
});

gulp.task('html', function () {
    return gulp.src(paths.htmlTemplateSrc
        )
//     .pipe(debug({title: 'stage 1'}))
        .pipe(replace({
            patterns: readReplacements()

        }))
        .pipe(insert.prepend('<!-- AUTO GENERATED FILE - DO NOT MODIFY ! -->\n'))
        .pipe(rename(renameTemplates))
        .pipe(gulp.dest('generated'))
        ;
});

gulp.task('dist', ['stylesheets', 'javascript', 'html'], function () {
    return true;
});

gulp.task('css:watch', function () {
    gulp.watch(paths.cssSrc, ['stylesheets'])
});

gulp.task('js:watch', function () {
    gulp.watch(paths.jsSrc, ['javascript'])
});

gulp.task('watch', ['css:watch', 'js:watch']);


// --- Functions ---

function readReplacements() {
    var profile = (yargs.argv.profile) ? yargs.argv.profile : 'default';
    var path = '../settings-' + profile + '.properties'

    console.log('Using settings file ' + path + ' for replacements');

    var settings = propertiesReader(path).path();

    var replacements = [];

    for (var setting in settings) {
        console.log('Processing setting: ' + setting);

        if (typeof settings[setting] !== 'string') {
            console.log('Skipping invalid setting: ' + setting + ' - expected String value but was: ' + JSON.stringify(settings[setting]));
            continue;
        }

        replacements.push({ "match": setting, "replacement": settings[setting] });
    }

    console.log(replacements.length + ' replacement(s) configured.');
    return replacements;

}

function renameTemplates(path) {
    path.basename = path.basename.replace('-template', '');
}