關於 Namespace, Project & User
==================

## Namespace

- 在原生的 Kubernetes 中提供了 **namespace** 的機制作為隔離 tenant 之用

- project 除了 tenant 的隔離外，同時也提供了 resource 隔離。

- OpenShift 中有些 object 是屬於 system level 而無法以 namespace 方式隔離的，例如：**node**, **user** .... 等 object。


## Project

其實 project 本質上就是 namespace，只是又多了 `annotation`，且在 Openshift 中就是以 project 為中心進行資源的控管。要對 project 進行存取的 user，都必須由管理者預先授予合適的權限才可以。

> project creator 會預設擁有存取 project resource 的權限


## User

一個 OpenShift platform 的 **user** 表示一個被授予合適權限與系統進行互動的 actor，而 user 實際上不一定必須是某一個人，也有可能是系統服務；目前的 user 有分為以下幾種類型：

1. Regular User
> 這是真實與 OpenShift 互動的使用者，帳號會在第一次登入時自動被建立(**前題是 cluster admin 必須先設定好 identity provider**)或是透過 API 建立；這類的使用者在 OpenShift 會以 `User` object 的型式存在

2. System Users
> 很多此類的帳號會在 OpenShift infrastructure 被定義時自動產生，目的在讓 infrastructure 的物件可以與 API 進行安全的互動，這類的帳號有包含 cluster administrator、node user, router user, registry user ... 等等。
> 此外，還有個稱為 `anonymous` 的 system user 用來進行不需認証的 request，例如 **system:admin**, **system:openshift-registry**, **system:node:node1.example.com** ... 等等就是此類帳號

3. Service Account
> 這是屬於與 project 相關的 system user，以 `ServiceAccount` object 的型式存在，管理者可以針對 project 對 OpenShift resource 存取的需求來調整 service account 的權限，例如：**system:serviceaccount:default:deployer**, **system:serviceaccount:foo:builder** .... 等等都屬於此類帳號

以上 3 個類型的 user 都必須透過某種方是先與 OpenShift 認証後，才可以進行 resource 的存取；而未經認証的 user 進行 API request 時，一律都會被視為 **anonymous** system user。


使用者管理
========

## 建立使用者

使用者是無法直接建立的，而是在使用者第一次登入時所建立

因此開放給使用者登入之前，**cluster admin 必須先設定好 identity provider** 作為帳號驗證的依據，下面的例子將會以 LDAP 認証作為範例來說明。


## 檢視 user & identitity 資訊

假設 cluster admin 已經設定好 LDAP 認証(使用 **vm adm**)，並且已經有兩位使用者進行登入(Leon & Karl)。

檢視 user 清單：

```bash
$ oc get user
NAME          UID                                    FULL NAME     IDENTITIES
Karl.Chiang   c2603c28-6ad8-11e7-9d4d-2e5247d44584   Karl Chiang   QCT_LDAP_Provider:CN=Karl Chiang,OU=RDUser,DC=qctrd,DC=com
Leon.Tseng    1885b499-6933-11e7-98e5-e2041e634c8d   Leon Tseng    QCT_LDAP_Provider:CN=Leon Tseng,OU=RDUser,DC=qctrd,DC=com
vmadm         71de1eef-6ab0-11e7-9d4d-2e5247d44584   vm adm        QCT_LDAP_Provider:CN=vm adm,OU=RDUser,DC=qctrd,DC=com
```

檢視 identity 清單：

```bash
$ oc get identity
NAME                                                         IDP NAME            IDP USER NAME                              USER NAME     USER UID
QCT_LDAP_Provider:CN=Karl Chiang,OU=RDUser,DC=qctrd,DC=com   QCT_LDAP_Provider   CN=Karl Chiang,OU=RDUser,DC=qctrd,DC=com   Karl.Chiang   c2603c28-6ad8-11e7-9d4d-2e5247d44584
QCT_LDAP_Provider:CN=Leon Tseng,OU=RDUser,DC=qctrd,DC=com    QCT_LDAP_Provider   CN=Leon Tseng,OU=RDUser,DC=qctrd,DC=com    Leon.Tseng    1885b499-6933-11e7-98e5-e2041e634c8d
QCT_LDAP_Provider:CN=vm adm,OU=RDUser,DC=qctrd,DC=com        QCT_LDAP_Provider   CN=vm adm,OU=RDUser,DC=qctrd,DC=com        vmadm         71de1eef-6ab0-11e7-9d4d-2e5247d44584
```

> 從上面可清楚看出除了 user 資訊外，還有 LDAP identity 的資訊



## 為 user 設定 label

除了 name 之外，我們可以使用 label 為 user 額外增加一些 metadata 來作為識別 or 資源分配之用。

新增 Label：

```bash
# 語法 => oc label user/<user_name> <label_name>
# 為 Leon.Tseng 增加 Label level=vip
$ oc label user/Leon.Tseng level=vip

# 檢視使用者資訊
$ oc describe user/Leon.Tseng
Name:		Leon.Tseng
Namespace:	<none>
Created:	2 weeks ago
Labels:		level=vip
Annotations:	<none>
Full Name:	Leon Tseng
Identities:	QCT_LDAP_Provider:CN=Leon Tseng,OU=RDUser,DC=qctrd,DC=com
```

移除 Label：

```bash
# 語法 => oc label user/<user_name> <label_name>-
$ oc label user/Leon.Tseng level-
user "Leon.Tseng" labeled

# 檢視使用者資訊
$  oc describe user/Leon.Tseng
Name:		Leon.Tseng
Namespace:	<none>
Created:	2 weeks ago
Labels:		<none>
Annotations:	<none>
Full Name:	Leon Tseng
Identities:	QCT_LDAP_Provider:CN=Leon Tseng,OU=RDUser,DC=qctrd,DC=com
```


## 刪除使用者

刪除使用者包含兩個步驟：

1. 刪除 user

2. 刪除 user identity

以下為刪除使用者的範例：

```bash
# 刪除 user
$ oc delete user Leon.Tseng

# 刪除 user identity
$ oc delete identity "QCT_LDAP_Provider:CN=Leon Tseng,OU=RDUser,DC=qctrd,DC=com"

# 檢視使用者資訊
$ oc describe user/Leon.Tseng
Error from server (NotFound): users "Leon.Tseng" not found
```


Service Account
===============

service account 的目的是要讓使用者認証後取得的 token 就可以拿來與 OpenShift API 互動，而不需要一直提供 credential

## Name & Group

每個 service account 都會會有一個 name，而管理者則可以針對 `system:serviceaccount:<project>:<service_account_name>` 這樣的組合來指定 role，並且在特定的 project 來賦予特定的權限。

舉例來說，若要在 **top-secret** project 中，要賦予 **robot** service account 擁有 **view** role 的權限，可以透過以下指令：

> oc policy add-role-to-user view system:serviceaccount:top-secret:robot

此外，每個 service account 都會是以下兩個 group 的 member：

- `system:serviceaccounts`：此 group 包含了系統中所有的 service account

- `system:serviceaccounts:<project>`：在特定 project 中所有的 service account
> 這代表 service account 都會與特定的 project 有所關聯

以下舉兩個針對 group 設定權限的範例：

1. 在所有 project 中的所有 service account，加上檢視(view) **top-secret** project 資源的權限
> oc policy add-role-to-group view system:serviceaccounts -n top-secret

2. 在 **manager** project 中的所有 service account 上，加入編輯 **top-secret** project 資源的權限：
> oc policy add-role-to-group edit system:serviceaccounts:managers -n top-secret

References
==========

- [Managing Users | Cluster Administration | OpenShift Origin Latest](https://docs.openshift.org/latest/admin_guide/manage_users.html)