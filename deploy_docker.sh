#!/usr/bin/env bash

. /etc/profile

cd $PROJ_PATH
mvn clean install

#准备ROOT.war包
cd $PROJ_PATH/target
mv blessing.war ROOT.war

#制作新的docker image - iweb
cd $PROJ_PATH
docker stop blessing
docker rm blessing
docker rmi blessing
docker build -t blessing .

#启动docker image, 宿主机暴露端口 8112
docker run --name myblessing -d -p 8112:8080 blessing



