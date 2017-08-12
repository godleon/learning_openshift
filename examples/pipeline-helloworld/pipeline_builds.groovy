// node {
//     stage('build 1') {
//         openshiftBuild(buildConfig: 'bc1', showBuildLogs: 'true')
//     }

//     // stage('build 2') {
//     // parallel {
//     //     "parallel 2.1": {
//     //     node {
//     //         openshiftBuild(buildConfig: 'bc2-1', showBuildLogs: 'true')
//     //     }
//     //     }, 
//     //     "parallel 2.2": {
//     //     node {
//     //         openshiftBuild(buildConfig: 'bc2-2', showBuildLogs: 'true')
//     //     }
//     //     }
//     // }
//     // }

//     stage('build 3') {
//         openshiftBuild(buildConfig: 'bc3', showBuildLogs: 'true')
//     }

//     // stage('job 1') {
//     // sh "oc delete pod/pi"

//     // sh "oc run pi --image=ubuntu --replicas=1 --restart=OnFailure --command -- echo 'Job in Ubuntu Image'; sleep 10; echo 'Job is done'"

//     // sh "oc logs pod/pi"
//     // }    
// }

stage('build 1') {
    openshiftBuild(buildConfig: 'bc1', showBuildLogs: 'true')
}

stage('build 3') {
    openshiftBuild(buildConfig: 'bc3', showBuildLogs: 'true')
}