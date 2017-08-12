stage ("task 1") {

  hook = registerWebhook()

  node {
    writeFile file: 'job.yaml', text: formatJob(env.BUILD_ID, "1")
    sh "oc create -f job.yaml"
  }

  data = waitForWebhook hook
  echo "Job 1 result --------> ${data}"
}



def formatJob(buildId, id) {
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
        command: ["echo", "job-${buildId}-${id} in Ubuntu image"]
      restartPolicy: Never
  """
}