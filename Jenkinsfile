node {
    stage('Clone/Pull') {
        if (fileExists('AdventOfCode2018')) {
            sh 'git -C AdventOfCode2018 pull --rebase --log'
        } else {
            sh 'git clone https://github.com/andersraberg/AdventOfCode2018.git'
        }
    }

    stage('Build') {
        dir('AdventOfCode2018') {
            sh './gradlew build'
        }
    }
    
    stage('Sonar') {
        dir('AdventOfCode2018') {
            sh './gradlew sonarqube -Dsonar.projectKey=AdventofCode2018 -Dsonar.host.url=http://sonarqube:9000 -Dsonar.login=cc9b6db0911b59b51fc766af0372efff5e1a7bec'
        }
    }

    stage('Run') {
        dir('AdventOfCode2018') {
            sh './gradlew run'
        }
    }
}