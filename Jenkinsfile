pipeline {

    agent any

    tools {
        jdk 'JDK 25'
        maven 'Maven-3.9.15'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git 'https://github.com/deepakkjogdand-dotcom/OrangeHRM.git'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn clean test -DsuiteXmlFile=testng.xml'
            }
        }

    }

    post {

        success {
            echo 'BUILD SUCCESS'
        }

        failure {
            echo 'BUILD FAILED'
        }
    }
}