node {
    git 'https://github.com/andersraberg/AdventOfCode2018.git'
    stage('Build') {
        sh './gradlew clean build'
    }
    
    stage('Code coverage') {
        sh './gradlew jacocoTestReport'
    }
 
    stage('Sonar') {
        withSonarQubeEnv() {
            sh './gradlew sonarqube -Dsonar.projectKey=AdventofCode2018'
        }
    }

    stage('Report') {
        junit 'build/test-results/**/*.xml'
    }

    stage('Run') {
        sh './gradlew run'
    }
   
}
