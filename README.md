[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Java CI with Maven](https://github.com/ksree/world-air-quality/workflows/Java%20CI%20with%20Maven/badge.svg)
# world-air-quality
A project to read and aggregate world air quality data
Build and package code

```text
cd $HOME/world-air-aq/
mvn package -DskipTests
```

##Create BigQuery Dataset and temporary GCS for intermidate storage
```shell script
export PROJECT_ID="kapilsreed12-1dataflow"
export GCS_TEMPORARY_BUCKET="${PROJECT_ID}-openairaq-temp-bucket"
export STORAGE_CLASS=standard
export GCP_REGION=us-east1

gsutil mb -c $STORAGE_CLASS  gs://$GCS_TEMPORARY_BUCKET

bq --location=$GCP_REGION mk --dataset --description 'Open Air Quality ' "${PROJECT_ID}:OpenAirAQ"
```









	

export GCP_REGION=us-east1

gcloud dataproc jobs submit spark \
--cluster=cluster-8d6e  \
--region=$GCP_REGION \
--class=com.ksr.air.Run \
--jars=/home/kapilsreed12/world-air-quality/target/world-air-quality-1.0-SNAPSHOT.jar,gs://spark-lib/bigquery/spark-bigquery-latest_2.12.jar \
-- /home/kapilsreed12/world-air-quality/src/main/resources/application.conf
