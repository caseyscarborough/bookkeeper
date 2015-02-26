// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [html         : ['text/html', 'application/xhtml+xml'],
                     xml          : ['text/xml', 'application/xml'],
                     text         : 'text-plain',
                     js           : 'text/javascript',
                     rss          : 'application/rss+xml',
                     atom         : 'application/atom+xml',
                     css          : 'text/css',
                     csv          : 'text/csv',
                     pdf          : 'application/pdf',
                     rtf          : 'application/rtf',
                     excel        : 'application/vnd.ms-excel',
                     ods          : 'application/vnd.oasis.opendocument.spreadsheet',
                     all          : '*/*',
                     json         : ['application/json', 'text/json'],
                     form         : 'application/x-www-form-urlencoded',
                     multipartForm: 'multipart/form-data'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
  views {
    gsp {
      encoding = 'UTF-8'
      htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
      codecs {
        expression = 'html' // escapes values inside ${}
        scriptlet = 'html' // escapes output from scriptlets in GSPs
        taglib = 'none' // escapes output from taglibs
        staticparts = 'none' // escapes output from static template parts
      }
    }
    // escapes all not-encoded output at final stage of outputting
    // filteringCodecForContentType.'text/html' = 'html'
  }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart = false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {
  development {
    grails.logging.jul.usebridge = true
  }
  production {
    grails.logging.jul.usebridge = false
    // TODO: grails.serverURL = "http://www.changeme.com"
  }
}

// log4j configuration
log4j.main = {
  // Example of changing the log pattern for the default console appender:

  appenders {
    console name: 'stdout', layout: pattern(conversionPattern: '%c{2} %m%n')
  }

  debug 'com.caseyscarborough.budget',
      'grails.app'
  error 'org.codehaus.groovy.grails.web.servlet',        // controllers
      'org.codehaus.groovy.grails.web.pages',          // GSP
      'org.codehaus.groovy.grails.web.sitemesh',       // layouts
      'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
      'org.codehaus.groovy.grails.web.mapping',        // URL mapping
      'org.codehaus.groovy.grails.commons',            // core / classloading
      'org.codehaus.groovy.grails.plugins',            // plugins
      'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
      'org.springframework',
      'org.hibernate',
      'net.sf.ehcache.hibernate'
}

grails.assets.excludes = [
    "bootstrap/less/*.less",
    "bootstrap/less/mixins/*.less",
    "bootstrap-datepicker/less/*.less",
    "flat-ui/less/*.less",
    "flat-ui/less/modules/*.less",
    "flat-ui/less/mixins/*.less"
]

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.caseyscarborough.budget.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.caseyscarborough.budget.security.UserRole'
grails.plugin.springsecurity.authority.className = 'com.caseyscarborough.budget.security.Role'

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
    '/'              : ['permitAll'],
    '/index.html'    : ['permitAll'],
    '/index.gsp'     : ['permitAll'],
    '/assets/**'     : ['permitAll'],
    '/**/js/**'      : ['permitAll'],
    '/**/css/**'     : ['permitAll'],
    '/**/images/**'  : ['permitAll'],
    '/**/favicon.ico': ['permitAll'],
    '/api/**'        : ['permitAll'],
    '/**'            : ['permitAll']
]

grails.plugin.springsecurity.filterChain.chainMap = [
    '/api/**' : 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter', // Stateless chain
    '/**'     : 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter'   // Traditional chain
]

grails.plugin.springsecurity.rememberMe.persistent = true