
以建置 `nginx-ubuntu1604` builder image 為例，以下是建置流程：

```bash
# 複製 repository
$ git clone https://github.com/godleon/learning_openshift.git

# 進入 builder image 目錄
$ cd learning_openshift/builder-images/nginx-ubuntu1604

# 產生 builder image
$ docker build -t nginx-ubuntu1604 .

# 測試 builder image 是否有如預期的執行
$ IMAGE_NAME=nginx-ubuntu1604 test/run.sh
```


References
==========

- [Creating a basic S2I builder image](https://github.com/openshift/source-to-image/tree/master/examples/nginx-centos7)