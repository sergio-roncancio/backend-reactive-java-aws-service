#!/bin/bash
source /etc/localstack/init/ready.d/variables.env

echo "Initializing localstack"

# Create Secrets

aws secretsmanager create-secret --name connection-db-local-reactive-pragma \
--description 'secret containing connection to the rds-local-reactive DB' \
--secret-string '{"host":"localhost","port":"'${DB_PORT}'","username":"'${DB_USERNAME}'","password":"'${DB_PASSWORD}'","dbname":"'${DB_NAME}'"}' \
--endpoint-url http://localhost:4566

# Create SQS queue
aws sqs create-queue --queue-name queue-reactive-pragma --endpoint-url http://localhost:4566