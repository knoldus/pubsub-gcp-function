Description
This project is designed to receive data from a publisher, publish the message to a topic, and trigger a cloud function that performs data transformation. The transformed data is then stored in Firestore, a NoSQL document database provided by Google Cloud Platform.

Usage
Publishing Data
To publish data to the project, you need to send a message to the configured topic.

Cloud Function
A cloud function is triggered whenever a message is published to the topic. This function performs data transformation on the received data and stores the transformed data in Firestore.

Configuration
Before running the project, you need to configure the following settings:
1. Google Cloud Platform (GCP) Project: Create a project on GCP if you haven't already. Enable Firestore and Cloud Functions for the project. Obtain the project ID and set it in the configuration.

2. Service Account Credentials: Create a service account on GCP with appropriate permissions to access Firestore and Pub/Sub. Download the service account credentials in JSON format and store them securely. Set the path to the JSON file containing the credentials in the configuration.

3. Pub/Sub Topic: Create a Pub/Sub topic and subscription. Set the topic name in the configuration.

4. Firestore Collection: Create a Firestore collection where the transformed data will be stored. Set the collection name in the configuration of the firestore.


To run the cloud function: 

1. Deploy the cloud function. 
To deploy a cloud function, run the following command on the console :
	gcloud alpha functions deploy cloud_function --entry-point pubsub.PubSubDataHandler --runtime java17 --trigger-topic topic_name 

2. After deploying the cloud function to the google cloud, publish a message to the configured topic. Given below is the reference example of the message to be published on topic.
The message should be of the model class format. 

{

    "carId": 123,
    "carModel": "Example Model",
    "brand": "Example Brand",
    "year": 2023,
    "color": "Red",
    "mileage": 5000.0,
    "price": 25000.0,
    "location": "Edin"
}

3. After the cloud function is triggered, you can check the logs to monitor its execution and any error messages.
Firestore
4. Open the firestore service, open the same collection as configured in the spring boot project and check the logs.
