openshift.withCluster() {

    echo "Hello from the project running Jenkins: ${openshift.project()}"

    openshift.withProject() {
        echo "Hello from project ${openshift.project()} in cluster ${openshift.cluster()}"
    }

}