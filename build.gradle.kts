plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.spring") version "2.1.20"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

val mysqlConnectorVersion = "8.0.33"
val caffeineVersion = "3.1.8"
val springDocVersion = "2.2.0"
val assertjVersion = "3.25.3"

group = "com.witek"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // ---------- Spring boot ------------------
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

    // ---------- Kotlin & Jackson ------------------
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // ---------- DB & CACHE ------------------
    implementation("mysql:mysql-connector-java:$mysqlConnectorVersion")
    implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")

    // ---------- docs ------------------
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    // ---------- tests ------------------
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.assertj:assertj-core:$assertjVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
