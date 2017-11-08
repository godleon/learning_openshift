openshift.withCluster() {

    def saSelector = openshift.selector('serviceaccount')
    saSelector.describe()

    // Selectors also allow you to easily iterate through all objects they currently select.
    saSelector.withEach { // The closure body will be executed once for each selected object.
        // The 'it' variable will be bound to a Selector which selects a single
        // object which is the focus of the iteration.
        echo "Service account: ${it.name()}(SECRET=${it.secret()}, AGE=${it.age()}) is defined in ${openshift.project()}"
    }

    // Prints a list of current service accounts to the console
    echo "There are ${saSelector.count()} service accounts in project ${openshift.project()}"
    echo "They are named: ${saSelector.names()}"

    // Selectors can also be defined using qualified names
    openshift.selector( 'buildconfig/selector' ).describe()
}