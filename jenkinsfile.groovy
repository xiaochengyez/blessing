stage('pull source code') {
    node('slave'){
        git([url: 'https://github.com/xiaochengyez/blessing.git', branch: 'master'])
    }
}

stage('clean docker environment') {
    node('slave'){
        try{
            sh 'docker stop myblessing'
        }catch(exc){
            echo 'blessing container is not running!'
        }

        try{
            sh 'docker rm myblessing'
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
            sh 'docker build -t  blessing .'
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