module.exports = function (grunt) {
    'use strict';

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
        },
        watch: {
            styles: {
                files: ['web-app/assets/stylesheets/*.less'],
                tasks: ['less:dist']
            }
        },
        jshint: {
            options: {
                jshintrc: '.jshintrc'
            },
            all: [
                'Gruntfile.js',
                'web-app/scripts/app/app.js',
                'web-app/scripts/app/**/*.js'
            ]
        },
        ngAnnotate: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '.tmp/concat/scripts',
                    src: '*.js',
                    dest: '.tmp/concat/scripts'
                }]
            }
        }
    });

    grunt.registerTask('build', [
        'clean:dist',
        'less:dist',
        'useminPrepare',
        'concat:generated',
        'ngAnnotate',
        'uglify:generated',
        'cssmin:generated',
        'rev:dist',
        'copy:dist',
        'usemin'
    ]);

    grunt.registerTask('default', [
        'less:dist',
        'watch'
    ]);

    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-rev');
    grunt.loadNpmTasks('grunt-usemin');
};