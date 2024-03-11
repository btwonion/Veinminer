import dex.plugins.outlet.v2.util.ReleaseType
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    `kotlin-script`
    `fabric-script`
    `publish-script`
}

dependencies {
    implementation(include(project(":core"))!!)
}

modrinth {
    uploadFile.set(tasks.remapJar)
    outlet.mcVersionRange = properties["fabricSupportedVersions"] as String
    outlet.allowedReleaseTypes = setOf(ReleaseType.RELEASE)
    gameVersions.addAll(outlet.mcVersions())
    loaders.addAll(buildList {
        add("fabric")
        add("quilt")
    })
    dependencies {
        // The scope can be `required`, `optional`, `incompatible`, or `embedded`
        // The type can either be `project` or `version`
        required.project("fabric-api")
        required.project("fabric-language-kotlin")

        val useSilk = properties["useSilk"] as String == "true"
        if (useSilk) {
            required.project("silk")
        }

        val useConfig = properties["useConfig"] as String == "true"
        if (useConfig) {
            optional.project("cloth-config")
        }
    }
}

sourceSets {
    main {
        resources.srcDirs("$rootDir/commons/")
    }
}

tasks.jar {
    archivesName = "${properties["projectName"]}"
//    archiveBaseName = "${properties["projectName"]}.$version"
}
