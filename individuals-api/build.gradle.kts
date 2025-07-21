// OkHttp
val okhttpVersion: String by project
val okhttpLoggingInterceptorVersion: String by project
val okioVersion: String by project

// GsonFire
val gsonFireVersion: String by project

// Testcontainers
val testcontainersKeycloakVersion: String by project
val testcontainersPostgresqlVersion: String by project

// JWT
val jjwtApiVersion: String by project
val jjwtImplVersion: String by project
val jjwtJacksonVersion: String by project

// Lombok
val lombokVersion: String by project

// OpenAPI
val jacksonDatabindNullableVersion: String by project
val swaggerAnnotationsVersion: String by project
val springdocOpenapiStarterWebmvcUiVersion: String by project

// Jakarta
val jakartaValidationApiVersion: String by project
val jakartaServletApiVersion: String by project

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
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpLoggingInterceptorVersion")
    implementation("com.squareup.okio:okio:$okioVersion")
    implementation("io.gsonfire:gson-fire:$gsonFireVersion")
    implementation("io.micrometer:micrometer-registry-prometheus")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("com.github.dasniko:testcontainers-keycloak:$testcontainersKeycloakVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersPostgresqlVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("io.jsonwebtoken:jjwt-api:$jjwtApiVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtImplVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtJacksonVersion")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("org.openapitools:jackson-databind-nullable:$jacksonDatabindNullableVersion")
    implementation("jakarta.validation:jakarta.validation-api:$jakartaValidationApiVersion")
    implementation("io.swagger:swagger-annotations:$swaggerAnnotationsVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenapiStarterWebmvcUiVersion")
    compileOnly("jakarta.servlet:jakarta.servlet-api:$jakartaServletApiVersion")
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
