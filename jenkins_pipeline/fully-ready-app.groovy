openshift.withCluster() {
    def bc = openshift.newApp('https://github.com/openshift/ruby-hello-world').narrow('bc')

    // The build config will create a new build object automatically, but how do
    // we find it? The 'related(kind)' operation can create an appropriate Selector for us.
    def builds = bc.related('builds')

    timeout(10) {
        builds.watch {
            // Within the body, the variable 'it' is bound to the watched Selector (i.e. builds)
            echo "So far, ${bc.name()} has created builds: ${it.names()}"

            // End the watch only once a build object has been created.
            return it.count() > 0  
        }

        // But we can actually want to wait for the build to complete.
        builds.watch {
            if ( it.count() == 0 ) return false
        }

        // A robust script should not assume that only one build has been created, so
        // we will need to iterate through all builds.
        // def allDone = true
        it.withEach {
            // 'it' is now bound to a Selector selecting a single object for this iteration.
            // Let's model it in Groovy to check its status.
            def buildModel = it.object() 
            if ( it.object().status.phase != "Complete" ) {
                allDone = false
            }
        }
        
        return allDone;
    }
}