stage ("task 1") {

  hook = registerWebhook()

  node {
    writeFile file: 'job.yaml', text: formatJob(env.BUILD_ID, "1", hook.getURL())
    sh "oc create -f job.yaml"
  }

  data = waitForWebhook hook
  echo "Job 1 result: ${data}"
}



def formatJob(buildId, id, callbackUrl) {
  """
apiVersion: batch/v1
kind: Job
metadata:
  name: curl-${buildId}-${id}
spec:
  template:
    metadata:
      name: curl-${buildId}-${id}
    spec:
      activeDeadlineSeconds: 60
      containers:
      - name: ubuntu-job-${buildId}-${id}
        image: ubuntu
        env:
        - name: CALLBACK_URL
          value: ${callbackUrl}
        command: ["echo", "$CALLBACK_URL"]
      restartPolicy: Never
  """
}