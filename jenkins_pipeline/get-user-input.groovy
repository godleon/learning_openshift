openshift.withCluster() {

    mail (
            to: 'godleon@gmail.com',
            subject: "Maps microservice (${env.BUILD_NUMBER}) is awaiting promotion",
             body: "Please go to ${env.BUILD_URL}.");
    
    input "Ready to update QE cluster with maps microservice?"

}