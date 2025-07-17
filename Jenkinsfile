node {
    git 'https://github.com/andersraberg/AdventOfCode2018.git'
    stage('Build') {
        sh './gradlew clean build -Pversion=$BUILD_NUMBER --profile'
    }
    
    stage('Run') {
        sh './gradlew run -Pversion=$BUILD_NUMBER'
    }

    stage('Code coverage') {
        sh './gradlew jacocoTestReport -Pversion=$BUILD_NUMBER'

        recordCoverage tools: [
            [parser: 'JACOCO', pattern: '**/build/reports/jacoco/test/jacocoTestReport.xml'] 
        ]	

    }
 
    stage('Sonar') {
        withSonarQubeEnv() {
            sh './gradlew sonarqube -Dsonar.projectKey=AdventofCode2018 -Pversion=$BUILD_NUMBER'
        }
    }

    stage('Publish') {
        sh './gradlew artifactoryPublish -Pversion=$BUILD_NUMBER'
    }

}
