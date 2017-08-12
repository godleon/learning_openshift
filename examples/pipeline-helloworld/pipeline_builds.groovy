node {

    stage('build 1') {
        openshiftBuild(buildConfig: 'bc1', showBuildLogs: 'true')
    }

    stage('build 3') {
        openshiftBuild(buildConfig: 'bc3', showBuildLogs: 'true')
    }
}




