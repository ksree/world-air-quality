
#!/bin/bash

helpFunction()
{
   echo ""
   echo "Usage: $0 -a \"AWS_ACCESS_KEY\" -b \"AWS_SECRET_KEY\""
   echo -e "\t-a Provide your AWS access key"
   echo -e "\t-b Provide your aws secret key"
   exit 1 # Exit script after printing help
}

while getopts "a:b:" opt
do
   case "$opt" in
      a ) AWS_ACCESS_KEY="$OPTARG" ;;
      b ) AWS_SECRET_KEY="$OPTARG" ;;
      ? ) helpFunction ;; # Print helpFunction in case parameter is non-existent
   esac
done

# Print helpFunction in case parameters are empty
if [ -z "$AWS_ACCESS_KEY" ] || [ -z "$AWS_SECRET_KEY" ]
then
   echo "Some or all of the parameters are empty";
   helpFunction
fi

# Begin script in case all parameters are correct
export PROJECT_ID=$(gcloud config get-value project)
export GCS_TEMPORARY_BUCKET="${PROJECT_ID}-temp-bucket"

cd $HOME/world-air-quality/
mvn package -DskipTests

echo 'Generating spark application config'

 echo "AWS_ACCESS_KEY="\""${AWS_ACCESS_KEY}"\""
AWS_SECRET_KEY="\""${AWS_SECRET_KEY}"\""
AWS_BUCKET_NAME=openaq-fetches
AWS_BUCKET_PREFIX=realtime-gzipped
GCS_TEMPORARY_BUCKET="\""${GCS_TEMPORARY_BUCKET}"\""
BIGQUERY_TABLE_NAME="\""${PROJECT_ID}:OpenAQ.pm25_global"\""
startDate=2019-01-01
endDate=2019-12-31
applyAggregations=true" > $HOME/application.conf

echo "Created a new configuration file $HOME/application.conf"