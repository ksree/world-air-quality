[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Java CI with Maven](https://github.com/ksree/world-air-quality/workflows/Java%20CI%20with%20Maven/badge.svg)
# world-air-quality


A project to read and aggregate world air quality data
############# export GCS_TEMPORARY_BUCKET="${PROJECT_ID}-temp-bucket"
export GCS_TEMPORARY_BUCKET="openaq-ksr-temp"
export STORAGE_CLASS=standard
export GCP_REGION=us-east1

gsutil mb -c $STORAGE_CLASS  gs://$GCS_TEMPORARY_BUCKET

bq mk PM25DailyAverage

bq --location=$LOCATION mk \ 
	--dataset \
	--description 'NOAA gsod weather data' \
	"${PROJECT_ID}:GlobalHistoricalWeatherData"
	
