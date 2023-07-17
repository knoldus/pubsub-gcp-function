package com.knoldus.cloudfunction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.CloudEventsFunction;
import com.google.events.cloud.pubsub.v1.MessagePublishedData;
import com.google.events.cloud.pubsub.v1.Message;
import io.cloudevents.CloudEvent;
import java.util.Base64;
import java.util.logging.Logger;
import com.knoldus.cloudfunction.model.Vehicle;

/**
 * A CloudEventsFunction implementation that handles Pub/Sub data.
 */
public class PubSubDataHandler implements CloudEventsFunction {
    private static final Logger logger = Logger.getLogger(
            PubSubDataHandler.class.getName());

    private static final double PRICE_CONVERSION_RATE_ = 82.11;
    private static final double MILEAGE_CONVERSION_RATE_ = 1.609344;

    /**
     * Handles the incoming CloudEvent.
     *
     * @param event The CloudEvent to be processed.
     * @throws JsonProcessingException if an error
     * occurs during JSON processing.
     */
    @Override
    public void accept(final CloudEvent event)
            throws JsonProcessingException {
        String cloudEventData = new String(event
                .getData().toBytes());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature
                .FAIL_ON_UNKNOWN_PROPERTIES, false);
        MessagePublishedData data = objectMapper.readValue(
                cloudEventData, MessagePublishedData.class);
        Message message = data.getMessage();
        String encodedData = message.getData();
        String decodedData = new String(Base64.getDecoder()
                .decode(encodedData));

        logger.info("Vehicle data: " + decodedData);

        Vehicle vehicleData = objectMapper.readValue(
                decodedData, Vehicle.class);

        double priceInRupees = transformPrice(
                vehicleData.getPrice());
        double mileageInKmpl = transformMileage(
                vehicleData.getMileage());

        vehicleData.setPriceInRupees(priceInRupees);
        vehicleData.setMileageInKmpl(mileageInKmpl);

        logger.info("Mileage in kmpl: " +
                vehicleData.getMileageInKmpl());
        logger.info("Price in rupees: " +
                vehicleData.getPriceInRupees());
    }

    /**
     * Transforms the price from dollars to rupees
     * based on the conversion rate.
     *
     * @param priceInDollars The price in dollars.
     * @return The price in rupees.
     */
    private double transformPrice(final double priceInDollars) {
        double conversionRate = PRICE_CONVERSION_RATE_;
        return priceInDollars * conversionRate;
    }

    /**
     * Transforms the mileage from miles to kilometers
     * per liter based on the conversion factor.
     *
     * @param mileageInMiles The mileage in miles.
     * @return The mileage in kilometers per liter.
     */
    private double transformMileage(
            final double mileageInMiles) {
        double conversionFactor = MILEAGE_CONVERSION_RATE_;
        return mileageInMiles * conversionFactor;
    }
}
