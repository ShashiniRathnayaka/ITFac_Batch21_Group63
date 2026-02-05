pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9.0'
        jdk 'JDK-11'
    }
    
    parameters {
        choice(name: 'BROWSER', choices: ['chromium', 'firefox', 'webkit'], description: 'Browser to run tests')
        choice(name: 'TAGS', choices: ['@Smoke', '@UI', '@API', '@Authentication', '@Plants', '@Categories', '@Sales', '@Dashboard'], description: 'Test tags to execute')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    }
    
    environment {
        APP_URL = 'http://localhost:8080/ui'
        API_URL = 'http://localhost:8080/api'
        MAVEN_OPTS = '-Xmx1024m'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from repository...'
                checkout scm
            }
        }
        
        stage('Install Dependencies') {
            steps {
                echo 'Installing Maven dependencies...'
                bat 'mvn clean install -DskipTests'
            }
        }
        
        stage('Install Playwright Browsers') {
            steps {
                echo 'Installing Playwright browsers...'
                bat 'mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"'
            }
        }
        
        stage('Run Tests') {
            steps {
                echo "Running tests with tag: ${params.TAGS}"
                bat """
                    mvn clean test ^
                    -Dbrowser.type=${params.BROWSER} ^
                    -Dbrowser.headless=${params.HEADLESS} ^
                    -Dcucumber.filter.tags="${params.TAGS}" ^
                    -Dapp.base.url=${env.APP_URL} ^
                    -Dapi.base.url=${env.API_URL}
                """
            }
        }
        
        stage('Generate Allure Report') {
            steps {
                echo 'Generating Allure report...'
                bat 'mvn allure:report'
            }
        }
    }
    
    post {
        always {
            echo 'Archiving test results...'
            
            // Archive test reports
            archiveArtifacts artifacts: 'target/cucumber-reports/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/allure-results/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/screenshots/**/*', allowEmptyArchive: true
            
            // Publish Allure Report
            allure([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'target/allure-results']]
            ])
            
            // Publish JUnit test results
            junit 'target/surefire-reports/*.xml'
            
            // Clean workspace
            cleanWs()
        }
        
        success {
            echo 'Test execution completed successfully!'
            // You can add email notification here
            // emailext (
            //     subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
            //     body: "Test execution completed successfully. Check Allure report for details.",
            //     to: "team@example.com"
            // )
        }
        
        failure {
            echo 'Test execution failed!'
            // You can add email notification here
            // emailext (
            //     subject: "FAILURE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
            //     body: "Test execution failed. Check console output and Allure report for details.",
            //     to: "team@example.com"
            // )
        }
    }
}
