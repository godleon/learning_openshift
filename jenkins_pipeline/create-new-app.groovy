openshift.withCluster() {
    def app = openshift.newApp("https://github.com/openshift/ruby-hello-world")

    // This Selector exposes the same operations you have already seen.
    // (And many more that you haven't!).
    echo "new-app created ${app.count()} objects name: ${app.names}"
    app.describe()
}