#!/usr/bin/env bash

## 停止tomcat的函数, 参数$1带入tomcat的路径$TOMCAT_PATH
killTomcat()
{
    pid=`ps -ef|grep $1|grep java|awk '{print $2}'`
    echo "tomcat Id list :$pid"
    if [ "$pid" = "" ]
    then
      echo "no tomcat pid alive"
    else
      kill -9 $pid
    fi
}

## 构建web项目，生成blessing.war包
cd $PROJ_PATH/blessing
mvn clean install

## 停止tomcat
killTomcat $TOMCAT_PATH

## 删除tomcat中原有的工程
rm -f $TOMCAT_PATH/webapps/ROOT.war
rm -f $TOMCAT_PATH/webapps/blessing.war
rm -rf $TOMCAT_PATH/webapps/ROOT

## 复制/粘贴新blessing.war包到tomcat
cp $PROJ_PATH/iWeb/target/blessing.war $TOMCAT_PATH/webapps/
cd $TOMCAT_PATH/webapps/
mv blessing.war ROOT.war

## 启动tomcat
cd $TOMCAT_PATH/bin
sh startup.sh





