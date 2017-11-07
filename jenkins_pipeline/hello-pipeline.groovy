try {
    node {
        stage('Build') { 
            sh 'echo "In Build Stage"'
            sh 'uname -a'
            sh 'pwd'
            sh 'whoami'
            sh 'cat /proc/cpuinfo | grep Xeon'
            sh 'free -m'
        }
        stage('Test'){
            sh 'echo "In Test Stage"' 
            sh 'uname -a'
            sh 'pwd'
        }
        stage('Deploy') {
            sh 'echo "In Test Stage"' 
            sh 'uname -a'
            sh 'pwd'
            sh 'cat /proc/cpui | grep Xeon'
        }
    }
} catch (err) {
    echo "in catch block"
    echo "Caught: ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}   