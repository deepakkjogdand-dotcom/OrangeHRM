pipeline {

    agent any

    tools {
        jdk 'JDK 25'
        maven 'Maven-3.9.15'
    }

    stages {

        stage('Run Automation Tests') {

            steps {

                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {

                    bat 'mvn clean test -DsuiteXmlFile=testng.xml'

                }
            }
        }

        stage('Publish Extent Reports') {

            steps {

                publishHTML([
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'Reports',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report'
                ])
            }
        }
    }

    post {

        always {

            archiveArtifacts artifacts: 'Screenshots/*.png', allowEmptyArchive: true

            echo 'Pipeline Execution Completed'
        }
    }
}