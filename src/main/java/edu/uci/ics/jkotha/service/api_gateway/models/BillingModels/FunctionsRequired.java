package edu.uci.ics.jkotha.service.api_gateway.models.BillingModels;

public class FunctionsRequired {

    public static String getMessage(int resultCode) {
        switch (resultCode) {
            case -11:
                return "Email address has invalid format.";
            case -10:
                return "Email address has invalid length.";
            case -3:
                return "JSON Parse Exception.";
            case -2:
                return "JSON Mapping Exception.";
            case 33:
                return "Quantity has invalid value.";
            case 311:
                return " Duplicate insertion.";
            case 3100:
                return "Shopping cart item inserted successfully.";
            case 312:
                return "Shopping item does not exist.";
            case 3110:
                return "Shopping cart item updated successfully.";
            case 3120:
                return "Shopping cart item deleted successfully.";
            case 3130:
                return "Shopping cart retrieved successfully.";
            case 3140:
                return "Shopping cart cleared successfully.";
            case 321:
                return "Credit card ID has invalid length.";
            case 322:
                return "Credit card ID has invalid value.";
            case 323:
                return "expiration has invalid value.";
            case 3200:
                return "Credit card inserted successfully.";
            case 324:
                return "Credit card does not exist.";
            case 325:
                return "Duplicate insertion.";
            case 3210:
                return "Credit card updated successfully.";
            case 3220:
                return "Credit card deleted successfully.";
            case 3230:
                return "Credit card retrieved successfully.";
            case 331:
                return "Credit card ID not found.";
            case 3300:
                return "Customer inserted successfully.";
            case 332:
                return "Customer does not exist.";
            case 333:
                return "Duplicate insertion.";
            case 3310:
                return "Customer updated successfully.";
            case 3320:
                return "Customer retrieved successfully.";
            case 341:
                return "Shopping cart for this customer not found.";
            case 342:
                return "Create payment failed.";
            case 3400:
                return "Order placed successfully.";
            case 3410:
                return "Orders retrieved successfully.";
            case 3421:
                return "Token not found";
            case 3422:
                return "Payment can not be completed";
            case 3420:
                return "Payment is completed successfully";
        }
        return null;
    }

}
