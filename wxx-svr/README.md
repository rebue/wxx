# wxx-api-svr

[TOC]

## 1. 启动内网穿透服务器调试微信

```sh
natapp -authtoken=996a791d8e8dd73a
sunny clientid 07b02b237ed53060
```

## 2. 部署

- 本机

```sh
cd ~/workspaces/02/wxx/wxx-api-svr
./deploy/deploy-wblapp2.sh
```

## 3. 配置中心

在git服务器中加入 wxx-api-svr-prod.yml 文件的内容

## 4. 创建容器并启动

```sh
docker run -d --net=host --name wxx-svr-a -v /usr/local/wxx-svr/a:/usr/local/myservice -v /usr/local/wxx-svr/templates:/usr/local/myservice/templates --restart=always nnzbz/spring-boot-app
```