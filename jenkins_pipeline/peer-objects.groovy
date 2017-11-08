openshift.withCluster() {
    def dcs = openshift.newApp( 'https://github.com/openshift/ruby-hello-world' ).narrow('dc')

    //將 selector(dcs) 透過 object() 轉為 Grovvy object
    //若有多個 Groovy object 則是要透過 objects() 來轉為 Grovvy object
    def dc = dcs.object()

    //dc 不是 selector，而是一個帶有 DC 資訊的 Groovy map
    echo "new-app created a ${dc.kind} with name ${dc.metadata.name}"
    echo "The object has labels: ${dc.metadata.labels}"
}