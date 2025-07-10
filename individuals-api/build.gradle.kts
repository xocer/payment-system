plugins {
    java
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.12.0"
}

group = "com.grishin"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.okio:okio:3.12.0")
    implementation("io.gsonfire:gson-fire:1.8.5")
    implementation("io.micrometer:micrometer-registry-prometheus")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("com.github.dasniko:testcontainers-keycloak:3.7.0")
    testImplementation("org.testcontainers:postgresql:1.21.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("io.swagger:swagger-annotations:1.6.16")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
}

openApiGenerate {
    generatorName.set("java")
    inputSpec.set("$rootDir/individuals-api/openapi/individuals-api.yaml")
//    inputSpec.set("openapi/individuals-api.yaml")
    outputDir.set("$buildDir/generated-sources/openapi")
    apiPackage.set("com.grishin.api")
    modelPackage.set("com.grishin.dto")
    configOptions.set(mapOf(
        "useJakartaEe" to "true",
        "library" to "webclient",
        "serializationLibrary" to "jackson",
        "nullableReferenceTypes" to "false",
        "jakartaNullable" to "false"
    ))
}

sourceSets {
    named("main") {
        java {
            srcDir("$buildDir/generated-sources/openapi/src/main/java")
        }
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    mainClass.set("com.grishin.individuals.api.IndividualsApiApplication")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named("compileJava") {
    dependsOn(tasks.named("openApiGenerate"))
}
