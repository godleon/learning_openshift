// node {

//     stage('build 1') {
//         openshiftBuild(buildConfig: 'bc1', showBuildLogs: 'true')
//     }

//     stage('build 2 parallel') {
//       parallel branchA: {
//         openshiftBuild(buildConfig: 'bc2-1', showBuildLogs: 'true')
//       }, branchB: {
//         openshiftBuild(buildConfig: 'bc2-2', showBuildLogs: 'true')
//       }
//     }

//     stage('build 3') {
//         openshiftBuild(buildConfig: 'bc3', showBuildLogs: 'true')
//     }
// }


stage('build 1') {
    node {
      openshiftBuild(buildConfig: 'bc1', showBuildLogs: 'true')
    }
}

stage('build 2 parallel') {
  parallel branchA: {
    node {
      openshiftBuild(buildConfig: 'bc2-1', showBuildLogs: 'true')
    }
  }, branchB: {
    node {
      openshiftBuild(buildConfig: 'bc2-2', showBuildLogs: 'true')
    }
  }
}

stage('build 3') {
    node {
      openshiftBuild(buildConfig: 'bc3', showBuildLogs: 'true')
    }
}



