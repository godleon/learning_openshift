
Docker 安全性設定
===============


```bash
$ export DOMAIN_NAME=docker-registry-default.os.qctrd.com

$ openssl s_client -connect $DOMAIN_NAME:443 -showcerts </dev/null 2>/dev/null | openssl x509 -outform PEM | tee /usr/local/share/ca-certificates/$DOMAIN_NAME.crt

$ update-ca-certificates

$ service docker restart
```

## 取得 self-signed CA certificate

```bash
# 建立目錄存放 self-signed CA certificate
$ mkdir /etc/docker/certs.d/docker-registry-default.os.qctrd.com

# 其中 10.103.10.121 為 master node 的 IP
$ scp root@10.103.10.121:/etc/origin/master/ca.crt /etc/docker/certs.d/docker-registry-default.os.qctrd.com/
```

## 設定 insecure registry

以 Ubuntu 16.04 為例，在 `/etc/default/docker` 中加入以下內容：

> DOCKER_OPTS="--insecure-registry docker-registry-default.os.qctrd.com"


-------------------------------


建立帳號作為上傳 Image 之用
=======================

```bash
$ oc new-project pushed
Now using project "pushed" on server "https://os-portal.qctrd.com:8443".

$ oc create serviceaccount pusher
serviceaccount "pusher" created

$ oc policy add-role-to-user system:image-builder system:serviceaccount:pushed:pusher
role "system:image-builder" added: "system:serviceaccount:pushed:pusher"

$ oc policy add-role-to-user edit system:serviceaccount:pushed:pusher
role "edit" added: "system:serviceaccount:pushed:pusher"

$ oc describe sa/pusher
Name:		pusher
Namespace:	pushed
Labels:		<none>
Annotations:	<none>

Mountable secrets: 	pusher-token-dvc41
                   	pusher-dockercfg-15xsn

Tokens:            	pusher-token-dvc41
                   	pusher-token-mcfzk

Image pull secrets:	pusher-dockercfg-15xsn


$ oc describe secret pusher-token-dvc41
Name:		pusher-token-dvc41
Namespace:	pushed
Labels:		<none>
Annotations:	kubernetes.io/service-account.name=pusher
		kubernetes.io/service-account.uid=fb430763-8bc7-11e7-a13b-faf564e56811

Type:	kubernetes.io/service-account-token

Data
====
ca.crt:		1070 bytes
namespace:	6 bytes
service-ca.crt:	2186 bytes
token:		eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJwdXNoZWQiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlY3JldC5uYW1lIjoicHVzaGVyLXRva2VuLWR2YzQxIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6InB1c2hlciIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6ImZiNDMwNzYzLThiYzctMTFlNy1hMTNiLWZhZjU2NGU1NjgxMSIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDpwdXNoZWQ6cHVzaGVyIn0.ojzgvbjeUVWrNf7y-MlnSwo4nzyEDF7TtP8UNG7qtq3KjNI-wXiNCUGC1SjrvmWPSduegJwKK_LsFV3Jrt4U5fhFRvcCjrOGqZ-ZjPijdc6RYKe6tStX-UwuPxyf7i_QvF1Ewu-xLFjOYmJ7ldJcPNS115lkUO-kk7zqqsAj13i4gigKHJP-b0LYNoORFX0yaQwgoYXXNjjfbz361rc3zqBYhId1lpOv6T_61aODNUL8-PVXmTRBC0t2-3PkW5wrvZB0hU5L7kqOT_oms-SwNKfB9d0yH1LFkNZuWcqP0M7LFTVaQ4eDLvtASQD4kt7FyMA-2x6zF4mmrkKnwlMGEA
```


```bash
$ oc create -f - <<API
apiVersion: v1
kind: ImageStream
metadata:
  annotations:
    description: Keeps track of changes in the application image
  name: s2i-nginx-ubuntu1604
  namespace: openshift
API
```

```bash
$ docker login --username=Leon.Tseng https://docker-registry-default.os.qctrd.com
Password: <此處貼上上面取得的 token>
Login Succeeded
```



References
==========

- [Remotely Push and Pull Container Images to OpenShift – OpenShift Blog](https://blog.openshift.com/remotely-push-pull-container-images-openshift/)

- [Securing and Exposing the Registry - Setting up the Registry | Installation and Configuration | OpenShift Container Platform 3.6](https://docs.openshift.com/container-platform/3.6/install_config/registry/securing_and_exposing_registry.html)