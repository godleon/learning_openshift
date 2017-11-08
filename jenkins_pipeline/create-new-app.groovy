openshift.withCluster() {
    // 回傳 selector，帶有 oc new-app 產生的所有物件
    def app = openshift.newApp("https://github.com/openshift/ruby-hello-world")

    //顯示 selector 的資訊
    echo "new-app created ${app.count()} objects name: ${app.names()}"
    app.describe()

    //使用 narrow() 取得 selector 中的特定資訊
    def bc = app.narrow('bc')

    //等同 oc logs，但加上 -f 參數會等到全部工作完成才會往下繼續執行
    def result = bc.logs('-f')

    // Many operations, like logs(), return a Result object (even a Selector
    // is a subclass of Result). A Result object contains detailed information about
    // what actions, if any, took place to accomplish an operation.
    echo "The logs operation require ${result.actions.size()} oc interactions"

    // You can even see exactly what oc command was executed.
    echo "Logs executed: ${result.actions[0].cmd}"

    // And even obtain the standard output and standard error of the command.
    def logsString = result.actions[0].out
    def logsErr = result.actions[0].err

    // And if after some processing within your pipeline, if you decide 
    // you need to initiate a new build after the one initiated by 
    // new-app, simply call the `oc start-build` equivalent:
    def buildSelector = bc.startBuild()
    buildSelector.logs('-f') 
}