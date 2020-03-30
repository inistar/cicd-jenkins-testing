pipeline {
   agent any
    tools {
        jdk 'jdk8'
    }
   stages {
      stage('Build') {
         steps {
             
            git 'https://github.com/inistar/cicd-jenkins-testing.git'

            sh "ls -la"

            sh "/usr/local/bin/mvn --version"
            
            sh '''
                REGION=us-central1
                PROJECT_ID=myspringml2
                MAIN_CLASS_NAME=GenerateSequenceToGCS
                BUCKET_NAME=jdbc-testing
                
                echo "PROJECT=$PROJECT_ID"
                echo "MAIN_CLASS=$MAIN_CLASS_NAME"
                echo "REGION=$REGION"
                echo "BUCKET_NAME=$BUCKET_NAME"
                
                /usr/local/bin/mvn compile exec:java \
                  -Dexec.mainClass=$MAIN_CLASS_NAME \
                  -Dexec.cleanupDaemonThreads=false \
                  "-Dexec.args= \
                        --project=$PROJECT_ID \
                        --region=$REGION \
                        --stagingLocation=gs://$BUCKET_NAME/$MAIN_CLASS_NAME/staging \
                        --tempLocation=gs://$BUCKET_NAME/$MAIN_CLASS_NAME/temp \
                        --templateLocation=gs://$BUCKET_NAME/$MAIN_CLASS_NAME/template \
                        --runner=DataflowRunner \
                        --serviceAccount=jdbc-testing@myspringml2.iam.gserviceaccount.com \
                        --outputGCSPath=gs://$BUCKET_NAME/$MAIN_CLASS_NAME/output \
                  "
            '''
         }
      }
    //   stage('Run gcloud') {
    //         steps {
    //             withEnv(['GCLOUD_PATH=/Users/inis/google-cloud-sdk/bin']) {
    //                 sh '$GCLOUD_PATH/gcloud dataflow jobs run manual-trigger-1 --gcs-location=gs://jdbc-testing/GenerateSequenceToGCS/template'
    //             }
    //         }
    //     }
     stage('TF Plan') {
       steps {
           sh 'terraform init'
           sh 'terraform plan -out myplan'
       }
     }

   }
}
