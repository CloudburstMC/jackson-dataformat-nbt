pipeline {
    agent any
    tools {
        maven 'Maven 3'
        jdk 'Java 8'
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage ('Deploy') {
            when {
                anyOf {
                    branch "master"
                    branch "development"
                }
            }
            steps {
                rtMavenDeployer(
                    id: "maven-deployer",
                    serverId: "opencollab-artifactory",
                    releaseRepo: "maven-releases",
                    snapshotRepo: "maven-snapshots"
                )
                rtMavenResolver(
                    id: "maven-resolver",
                    serverId: "opencollab-artifactory",
                    releaseRepo: "release",
                    snapshotRepo: "snapshot"
                )
                rtMavenRun(
                    pom: 'pom.xml',
                    goals: 'javadoc:jar source:jar install -DskipTests',
                    deployerId: "maven-deployer",
                    resolverId: "maven-resolver"
                )
                rtPublishBuildInfo(
                    serverId: "opencollab-artifactory"
                )
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
