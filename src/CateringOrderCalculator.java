import java.util.List;

public class CateringOrderCalculator {
    private CateringPriceManager priceManager;

    public static final String[] MUTTON_SIZES = {"كامل (Full)", "نصف (Half)", "ربع (Quarter)"};

    public CateringOrderCalculator(CateringPriceManager priceManager) {
        this.priceManager = priceManager;
    }

    public List<String> getRiceDishTypes() {
        return priceManager.getRiceTypes();
    }

    public List<String> getBeefChickenDishTypes() {
        return priceManager.getBeefChickenDishTypes();
    }

    // Fish is strictly Grilled (مشوي) per kitchen rules
    public String[] getFishDishTypes() {
        return new String[]{"مشوي"};
    }

    public double calculateRiceTotal(String riceType, int mun) {
        if (mun <= 0) return 0.0;
        return priceManager.getRicePricePerMun() * mun;
    }

    public double calculateBeefTotal(int mun, boolean isCustomerProvided) {
        if (mun <= 0) return 0.0;
        return isCustomerProvided ? priceManager.getBeefCookingFeePerMun() * mun : priceManager.getBeefPricePerMun() * mun;
    }

    public double calculateChickenTotal(int mun, boolean isCustomerProvided) {
        if (mun <= 0) return 0.0;
        return isCustomerProvided ? priceManager.getChickenCookingFeePerMun() * mun : priceManager.getChickenPricePerMun() * mun;
    }

    public double calculateFishTotal(int mun, boolean isCustomerProvided) {
        if (mun <= 0) return 0.0;
        return isCustomerProvided ? priceManager.getFishCookingFeePerMun() * mun : priceManager.getFishPricePerMun() * mun;
    }

    public double calculateMuttonTotal(String size, int quantity, boolean isCustomerProvided) {
        if (quantity <= 0) return 0.0;
        if (isCustomerProvided) {
            return priceManager.getMuttonCookingFee() * quantity;
        } else {
            double basePrice = 0.0;
            if (size != null) {
                if (size.contains("Full")) basePrice = priceManager.getMuttonFullPrice();
                else if (size.contains("Half")) basePrice = priceManager.getMuttonHalfPrice();
                else if (size.contains("Quarter")) basePrice = priceManager.getMuttonQuarterPrice();
            }
            return basePrice * quantity;
        }
    }
}