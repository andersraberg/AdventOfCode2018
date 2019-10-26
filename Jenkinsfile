node {
    stage('Build') {
        dir('AdventOfCode2018') {
            sh './gradlew build'
        }
    }
    
    stage('Sonar') {
        dir('AdventOfCode2018') {
	    withSonarQubeEnv() { // Will pick the global server connection you have configured
                sh './gradlew sonarqube -Dsonar.projectKey=AdventofCode2018'
            }
        }
    }

    stage('Run') {
        dir('AdventOfCode2018') {
            sh './gradlew run'
        }
    }
}
