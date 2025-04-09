import dex.plugins.outlet.v2.util.ReleaseType

plugins {
    `kotlin-script`
    `paper-script`
    `shadow-script`
    `publish-script`
    id("io.github.dexman545.outlet")
    `maven-publish`
}

dependencies {
    implementation(project(":core"))
}

sourceSets {
    main {
        resources.srcDirs("$rootDir/commons/")
    }
}

modrinth {
    uploadFile.set(tasks.jar)
    versionName = "Veinminer Plugin - ${properties["version"]}"
    outlet.mcVersionRange = properties["paperSupportedVersions"] as String
    outlet.allowedReleaseTypes = setOf(ReleaseType.RELEASE)
    gameVersions.addAll(outlet.mcVersions())
    changelog = properties["changelog"] as String
    loaders.addAll(buildList {
        add("paper")
        add("purpur")

        val foliaSupport = properties["foliaSupport"] as String == "true"
        if (foliaSupport) add("folia")
    })
    dependencies {
        // The scope can be `required`, `optional`, `incompatible`, or `embedded`
        // The type can either be `project` or `version`
//        required.project("fabric-api")
    }
}

publishing {
    repositories {
        maven {
            name = "nyon"
            url = uri("https://repo.nyon.dev/releases")
            credentials {
                username = providers.environmentVariable("NYON_USERNAME").orNull
                password = providers.environmentVariable("NYON_PASSWORD").orNull
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.miraculixx"
            artifactId = "veinminer"
            version = project.version.toString()
            from(components["java"])
        }
    }
}

java {
    withSourcesJar()

    JavaVersion.VERSION_21.let {
        sourceCompatibility = it
        targetCompatibility = it
    }
}