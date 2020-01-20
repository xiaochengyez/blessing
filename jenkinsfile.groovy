stage('pull source code') {
    node('slave'){
        git([url: 'https://github.com/xiaochengyez/blessing.git', branch: 'master'])
    }
}

stage('maven compile & package') {
    node('slave'){
        sh ". /etc/profile"


        //定义maven java环境
        def mvnHome = tool 'maven-3.6.1_slave'
        def jdkHome = tool 'jdk1.8_slave'
        env.PATH = "${mvnHome}/bin:${env.PATH}"
        env.PATH = "${jdkHome}/bin:${env.PATH}"
        sh "mvn clean install"
        //sh "mv target/blessing.war target/ROOT.war"
    }
}

stage('clean docker environment') {
    node('slave'){
        try{
            sh 'docker stop blessing'
        }catch(exc){
            echo 'blessing container is not running!'
        }

        try{
            sh 'docker rm blessing'
        }catch(exc){
            echo 'blessing container does not exist!'
        }
        try{
            sh 'docker rmi blessing'
        }catch(exc){
            echo 'blessing image does not exist!'
        }
    }
}

stage('make new docker image') {
    node('slave'){
        try{
            sh 'docker build -t blessing .'
        }catch(exc){
            echo 'Make blessing docker image failed, please check the environment!'
        }
    }
}

stage('start docker container') {
    node('slave'){
        try{
            sh 'docker run --name myblessing -d -p 8112:8080 blessing'
        }catch(exc){
            echo 'Start docker image failed, please check the environment!'
        }
    }
}