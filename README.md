[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Java CI with Maven](https://github.com/ksree/world-air-quality/workflows/Java%20CI%20with%20Maven/badge.svg)
# world-air-quality


A project to read and aggregate world air quality data
############# export GCS_TEMPORARY_BUCKET="${PROJECT_ID}-temp-bucket"
export GCS_TEMPORARY_BUCKET="openaq-ksr-temp"
export STORAGE_CLASS=standard
export GCP_REGION=us-east1

# Begin script in case all parameters are correct
echo 'Setting up JAVA8'
sudo apt-get install -y openjdk-8-jre
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

cd $HOME/ghcn-daily-analysis/
mvn package -DskipTests



gsutil mb -c $STORAGE_CLASS  gs://$GCS_TEMPORARY_BUCKET

bq mk PM25DailyAverage

bq --location=$LOCATION mk \ 
	--dataset \
	--description 'NOAA gsod weather data' \
	"${PROJECT_ID}:GlobalHistoricalWeatherData"
	

export LOCATION=us-central1

gcloud dataproc jobs submit spark \
--cluster=cluster-54db  \
--region=$LOCATION \
--class=com.ksr.air.Run \
--jars=/home/kapilsreed12/world-air-quality/target/world-air-quality-1.0-SNAPSHOT.jar,gs://spark-lib/bigquery/spark-bigquery-latest.jar 
