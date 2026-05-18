pipeline {

    agent any

    tools {
        jdk 'JDK25'
        maven 'Maven3'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git 'https://github.com/yourusername/yourrepo.git'
            }
        }

        stage('Clean Project') {
            steps {
                bat 'mvn clean'
            }
        }

        stage('Run TestNG Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Generate Reports') {
            steps {
                echo 'Extent Reports Generated Successfully'
            }
        }
    }

    post {

        always {
            archiveArtifacts artifacts: 'Screenshots/*.png', allowEmptyArchive: true
        }

        success {
            echo 'BUILD SUCCESS'
        }

        failure {
            echo 'BUILD FAILED'
        }
    }
}