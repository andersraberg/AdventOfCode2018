node {
    git 'https://github.com/andersraberg/AdventOfCode2018.git'
    stage('Build') {
        sh './gradlew clean build -Pversion=$BUILD_NUMBER --scan'
    }
    
    stage('Code coverage') {
        sh './gradlew jacocoTestReport -Pversion=$BUILD_NUMBER'
        jacoco( 
            execPattern: 'build/jacoco/*.exec',
        )
    }
 
    stage('Sonar') {
        withSonarQubeEnv() {
            sh './gradlew sonarqube -Dsonar.projectKey=AdventofCode2018 -Pversion=$BUILD_NUMBER'
        }
    }

    stage('Report') {
        junit 'build/test-results/**/*.xml'
    }

    stage('Run') {
        sh './gradlew run -Pversion=$BUILD_NUMBER'
    }

    stage('Publish') {
        sh './gradlew artifactoryPublish -Pversion=$BUILD_NUMBER'
    }

}
