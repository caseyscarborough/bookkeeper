grails.servlet.version = "3.0"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.7
grails.project.source.level = 1.7
grails.project.war.file = "target/${appName}.war"

grails.project.fork = [
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon: true],
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve: false],
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve: false],
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
  inherits("global") {}
  log "error"
  checksums true
  legacyResolve false

  repositories {
    inherits true

    grailsPlugins()
    grailsHome()
    mavenLocal()
    grailsCentral()
    mavenCentral()
  }

  dependencies {
    test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"
  }

  plugins {
    build ":tomcat:8.0.20"
    compile ":scaffolding:2.1.2"
    compile ':cache:1.1.8'
    compile ":asset-pipeline:2.1.4"
    runtime ":hibernate4:4.3.6.1"
    runtime ":database-migration:1.4.0"
    compile ":spring-security-core:2.0-RC4"
    compile ":csv:0.3.1"
    compile ":excel-export:0.2.1"
    compile ":less-asset-pipeline:2.0.8"
  }
}
