[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Java CI with Maven](https://github.com/ksree/world-air-quality/workflows/Java%20CI%20with%20Maven/badge.svg)
# world-air-quality
A project to read and aggregate world air quality data
Build and package code



```text
cd $HOME/world-air-aq/
mvn package -DskipTests
```
## Create BigQuery Dataset and temporary GCS for intermidate storage
```shell script
export PROJECT_ID="kapilsreed12-1dataflow"
export GCS_TEMPORARY_BUCKET="${PROJECT_ID}-openairaq-temp-bucket"
export STORAGE_CLASS=standard
export GCP_REGION=us-east1

gsutil mb -c $STORAGE_CLASS  gs://$GCS_TEMPORARY_BUCKET

bq --location=$GCP_REGION mk \
--dataset \
--description 'Open Air Quality' \
 "${PROJECT_ID}:OpenAirAQ"
```

## application.conf
```shell script

WS_ACCESS_KEY="Add Your Key here"
AWS_SECRET_KEY="Your Secret "
AWS_BUCKET_NAME="openaq-fetches"
AWS_BUCKET_PREFIX="realtime-gzipped"
GCS_TEMPORARY_BUCKET="your-gcs-temp-bucket"
BIGQUERY_TABLE_NAME="yourprojectname:OpenAirAQ.pm25_global"
startDate="2019-01-01"   -- Start Date OpenAQ dataset
endDate="2019-12-31"      -- End Date OpenAQ dataset
applyAggregations="true"  -- true= applies pm2.5 aggreagation, false= loads openaq data as is into BigQuery. 
```

## Execute Dataproc job 
```
export GCP_REGION=us-east1

gcloud dataproc jobs submit spark \
--cluster=cluster-3e8d  \
--region=$GCP_REGION \
--class=com.ksr.air.Run \
--files=/home/kapilsreed12/application.conf \
--jars=/home/kapilsreed12/world-air-quality/target/world-air-quality-1.0-SNAPSHOT.jar,gs://spark-lib/bigquery/spark-bigquery-latest_2.12.jar \
-- application.conf
```
