package pubsub.eventmodel;

import lombok.Data;

@Data
public class CarModel {
    private CarMessage message;

    public CarMessage getMessage() {
        return message;
    }
    @Data
    public class CarMessage{
        /**
         * The ID of the car.
         */
        private Integer carId;

        /**
         * The model of the car.
         */
        private String carModel;

        /**
         * The brand of the car.
         */
        private String brand;

        /**
         * The year of the car.
         */
        private Integer year;

        /**
         * The color of the car.
         */
        private String color;

        /**
         * The mileage of the car.
         */
        private Double mileage;

        /**
         * The price of the car.
         */
        private Double price;

        /**
         * The location of the car.
         */
        private String location;

        /**
         * The price of the car in Rupees.
         */
        private Double priceInRupees;

        /**
         * The mileage of the car in kilometers per liter (kmpl).
         */
        private Double mileageInKmpl;
    }

}
