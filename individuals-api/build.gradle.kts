import java.util.Properties

val versionProps = Properties().apply {
    file("$rootDir/versions.properties").inputStream().use {
        load(it)
    }
}

fun version(key: String) = versionProps.getProperty(key)

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
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.squareup.okhttp3:okhttp:${version("okhttpVersion")}")
    implementation("com.squareup.okhttp3:logging-interceptor:${version("okhttpLoggingInterceptorVersion")}")
    implementation("com.squareup.okio:okio:${version("okioVersion")}")
    implementation("io.gsonfire:gson-fire:${version("gsonFireVersion")}")
    implementation("io.micrometer:micrometer-registry-prometheus")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("com.github.dasniko:testcontainers-keycloak:${version("testcontainersKeycloakVersion")}")
    testImplementation("org.testcontainers:postgresql:${version("testcontainersPostgresqlVersion")}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("io.jsonwebtoken:jjwt-api:${version("jjwtApiVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${version("jjwtImplVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${version("jjwtJacksonVersion")}")

    compileOnly("org.projectlombok:lombok:${version("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${version("lombokVersion")}")

    testCompileOnly("org.projectlombok:lombok:${version("lombokVersion")}")
    testAnnotationProcessor("org.projectlombok:lombok:${version("lombokVersion")}")

    implementation("org.openapitools:jackson-databind-nullable:${version("jacksonDatabindNullableVersion")}")
    implementation("jakarta.validation:jakarta.validation-api:${version("jakartaValidationApiVersion")}")
    implementation("io.swagger:swagger-annotations:${version("swaggerAnnotationsVersion")}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${version("springdocOpenapiStarterWebmvcUiVersion")}")
    compileOnly("jakarta.servlet:jakarta.servlet-api:${version("jakartaServletApiVersion")}")
}

openApiGenerate {
    generatorName.set("java")
    inputSpec.set("$rootDir/individuals-api/openapi/individuals-api.yaml") // need for local start
//    inputSpec.set("openapi/individuals-api.yaml") // need for docker start
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
