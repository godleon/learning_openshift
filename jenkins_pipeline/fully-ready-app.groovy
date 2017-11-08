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

            def allDone = true
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

        // Uggh. That was actually a significant amount of code. Let's use untilEach(){} 
        // instead. It acts like watch, but only executes the closure body once
        // a minimum number of objects meet the Selector's criteria only terminates 
        // once the body returns true for all selected objects.
        builds.untilEach(1) { // We want a minimum of 1 build
        
            // Unlike watch(), untilEach binds 'it' to a Selector for a single object.
            // Thus, untilEach will only terminate when all selected objects satisfy this 
            // the condition established in the closure body (or until the timeout(10) 
            // interrupts the operation).
            return it.object().status.phase == "Complete"
        }

        openshift.selector('dc/ruby-hello-world').delete()
        openshift.selector('svc/ruby-hello-world').delete()
        openshift.selector('is/ruby-22-centos7').delete()
        openshift.selector('bc/ruby-hello-world').delete()
    }
}