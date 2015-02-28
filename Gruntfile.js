// Custom usemin step (from jhipster)
var useminAutoprefixer = {
    name: 'autoprefixer',
    createConfig: require('grunt-usemin/lib/config/cssmin').createConfig
};

module.exports = function (grunt) {

    grunt.initConfig({
        config: {
            dist: 'web-app/dist'
        },
        clean: {
            dist: {
                files: [{
                    dot: true,
                    src: [
                        '.tmp/',
                        '<%= config.dist %>/'
                    ]
                }]
            }
        },
        less: {
            dist: {
                files: {
                    'web-app/assets/stylesheets/application.css': 'web-app/assets/stylesheets/application.less'
                }
            }
        },
        rev: {
            dist: {
                files: [{
                    src: [
                        '<%= config.dist %>/scripts/*.js',
                        '<%= config.dist %>/assets/stylesheets/*.css',
                        '<%= config.dist %>/assets/fonts/*'
                    ]
                }]
            }
        },
        useminPrepare: {
            html: 'web-app/index.html',
            options: {
                dest: '<%= config.dist %>/'
            }
        },
        usemin: {
            html: ['<%= config.dist %>/*.html'],
            css: ['<%= config.dist %>/assets/stylesheets/**/*.css'],
            js: ['<%= config.dist %>/scripts/**/*.js']
        },
        copy: {
            dist: {
                files: [{
                    expand: true,
                    cwd: 'web-app',
                    dest: '<%= config.dist %>/',
                    src: [
                        '*.html',
                        'scripts/**/*.html',
                        'assets/images/**/*.{png,jpg,gif}'
                    ]
                }]
            }
        }
    });

    grunt.registerTask('build', [
        'clean:dist',
        'less:dist',
        'useminPrepare',
        'concat:generated',
        'uglify:generated',
        'cssmin:generated',
        'rev:dist',
        'copy:dist',
        'usemin'
    ]);

    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-usemin');
    grunt.loadNpmTasks('grunt-rev');
};