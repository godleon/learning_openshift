openshift.withCluster() {

    echo "Hello from the project running Jenkins: ${openshift.project()}"

    def saSelector = openshift.selector('serviceaccount')
    saSelector.describe()

    // Selectors also allow you to easily iterate through all objects they currently select.
    saSelector.withEach { // The closure body will be executed once for each selected object.
        // The 'it' variable will be bound to a Selector which selects a single
        // object which is the focus of the iteration.
        echo "Service account: ${it.name()} is defined in ${openshift.project()}"
    }

    openshift.withProject() {
        echo "Hello from project ${openshift.project()} in cluster ${openshift.cluster()}"
    }

}