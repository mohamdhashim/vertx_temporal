import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "org.example"
version "1.0-SNAPSHOT"
repositories {
  mavenCentral()
  google()

  maven{
    url = uri("https://mvnrepository.com/artifact/com.fasterxml.jackson/jackson-bom")
  }
}

val vertxVersion = "4.2.3"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "com.example.hello.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}
dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

  implementation("com.fasterxml.jackson:jackson-bom:2.13.1")
  testImplementation ("io.temporal:temporal-testing:1.4.0")
  implementation ("io.temporal:temporal-sdk:1.4.0")
  testImplementation ("org.junit.jupiter:junit-jupiter-api:5.8.2")
  testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.8.2")
  implementation("ch.qos.logback:logback-classic:1.2.10")
  implementation("commons-configuration:commons-configuration:1.10")
  implementation ("org.json:json:20211205")
//
  implementation("io.temporal:temporal-testing-junit4:1.4.0")
  testImplementation ("io.temporal:temporal-testing-junit4:1.4.0")
  testImplementation ("io.temporal:temporal-testing-junit5:1.4.0")
  testImplementation ("junit:junit:4.13.2")
  testImplementation ("org.mockito:mockito-core:4.2.0")

}


java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
