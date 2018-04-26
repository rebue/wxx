# wxx-access-token-svr

[TOC]

## 1. 部署

- 本机

```sh
cd ~/workspaces/02/wxx/wxx-access-token-svr
./deploy/deploy-cocapp2.sh
```

## 2. 配置中心

在git服务器中加入 wxx-access-token-svr-prod.yml 文件的内容

## 3. 设置白名单

在公众号中设置IP白名单，否则获取access_token时，微信服务器会返回40164错误

## 4. 创建容器并启动

```sh
docker run -d --net=host --name wxx-access-token-svr -v /usr/local/wxx-access-token-svr:/usr/local/myservice --restart=always nnzbz/spring-boot-app
```