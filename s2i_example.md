
前置作業
=======

要使用 S2I 的功能要使用特殊的 docker image，不是隨便一個 docker image 都可以拿來用

在 image 中的根目錄必須內含 `s2i` 資料夾，裏面有 3 個檔案非常重要，作為驅動 S2I 流程的進行：

1. `s2i/bin/assemble`
> Used to install the sources into the location where the application will be run and prepare the application for deployment (eg. installing dependencies, etc.)

2. `s2i/bin/run`
> This script is responsible for running the application by using the application web server.

3. `s2i/bin/usage`
> This script prints the usage of this image.


工作流程說明
==========

以下的範例所要完成的工作是：

> 將會使用 **ansible lint** 來為 ansible playbook 進行語法的檢查

1. 使用 docker image [centos/python-35-centos7](https://hub.docker.com/r/centos/python-35-centos7/)

2. 



References
==========

- [sclorg/s2i-python-container](https://github.com/sclorg/s2i-python-container) (要製作 Python S2I image 可參考此範例專案)
> This repository contains the source for building various versions of the Python application as a reproducible Docker image using source-to-image. Users can choose between RHEL and CentOS based builder images. The resulting image can be run using Docker.