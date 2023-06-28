import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
}

group = "pl.emailservice"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.google.http-client:google-http-client-jackson2:1.43.2")
    implementation("com.google.api-client:google-api-client-jackson2:1.31.1")
    implementation("com.google.api-client:google-api-client:1.31.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.apache.camel.springboot:camel-direct-starter:4.0.0-M3")
    implementation("org.apache.camel.springboot:camel-spring-boot-starter:4.0.0-M3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation ("org.apache.camel:camel-core:4.0.0-M3")
    implementation ("org.apache.camel:camel-api:4.0.0-M3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.16.1")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.http-client:google-http-client-jackson2:1.43.2")
    implementation ("org.apache.camel:camel-attachments:4.0.0-M3")
    implementation("org.apache.camel.springboot:camel-google-mail-starter:4.0.0-M3")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation ("com.sun.mail:jakarta.mail:2.0.1")
    implementation("org.apache.camel:camel-mail:4.0.0-M3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("email-service.jar")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
