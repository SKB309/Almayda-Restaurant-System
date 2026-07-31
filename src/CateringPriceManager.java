import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CateringPriceManager {
    private List<String> riceDishTypes;
    private List<String> proteinTypes;
    private List<String> beefChickenDishTypes;
    private List<String> fishDishTypes;

    // Price variables and mappings
    private Map<String, Double> ricePrices;
    private Map<String, Double> muttonSizePrices;
    private Map<String, Double> proteinPrices;
    private Map<String, Double> proteinCookingFees;

    private double beefPricePerMun;
    private double chickenPricePerMun;
    private double fishPricePerMun;

    private double beefCookingFeePerMun;
    private double chickenCookingFeePerMun;
    private double fishCookingFeePerMun;
    private double muttonCookingFee;

    public CateringPriceManager() {
        // Initialize dynamic lists
        riceDishTypes = new ArrayList<>(Arrays.asList("عيش الأبيض", "عيش المكبوس", "هريس", "عرسية", "ثريد"));
        proteinTypes = new ArrayList<>(Arrays.asList("بقر", "دجاج", "سمك", "ضأن"));
        beefChickenDishTypes = new ArrayList<>(Arrays.asList("مكبوس", "برياني", "مضبي"));
        fishDishTypes = new ArrayList<>(Arrays.asList("صيادية", "مفلق", "مقلي"));

        // Initialize default prices
        ricePrices = new HashMap<>();
        ricePrices.put("عيش الأبيض", 4.0);
        ricePrices.put("عيش المكبوس", 5.0);
        ricePrices.put("هريس", 6.0);
        ricePrices.put("عرسية", 6.0);
        ricePrices.put("ثريد", 5.5);

        muttonSizePrices = new HashMap<>();
        muttonSizePrices.put("كامل (Full)", 60.0);
        muttonSizePrices.put("نصف (Half)", 35.0);
        muttonSizePrices.put("ربع (Quarter)", 20.0);

        proteinPrices = new HashMap<>();
        proteinPrices.put("بقر", 10.0);
        proteinPrices.put("دجاج", 8.0);
        proteinPrices.put("سمك", 12.0);

        proteinCookingFees = new HashMap<>();
        proteinCookingFees.put("بقر", 10.0);
        proteinCookingFees.put("دجاج", 8.0);
        proteinCookingFees.put("سمك", 12.0);
        proteinCookingFees.put("ضأن", 15.0);

        beefPricePerMun = 10.0;
        chickenPricePerMun = 8.0;
        fishPricePerMun = 12.0;

        beefCookingFeePerMun = 10.0;
        chickenCookingFeePerMun = 8.0;
        fishCookingFeePerMun = 12.0;
        muttonCookingFee = 15.0;
    }

    // --- Rice Types Management ---
    public List<String> getRiceDishTypes() {
        return riceDishTypes;
    }

    public List<String> getRiceTypes() {
        return riceDishTypes;
    }

    public void setRiceTypes(List<String> riceTypes) {
        if (riceTypes != null) {
            this.riceDishTypes = new ArrayList<>(riceTypes);
            for (String type : riceTypes) {
                if (!ricePrices.containsKey(type)) {
                    ricePrices.put(type, 5.0);
                }
            }
        }
    }

    public void addRiceType(String type) {
        if (type != null && !type.trim().isEmpty() && !riceDishTypes.contains(type.trim())) {
            riceDishTypes.add(type.trim());
            ricePrices.put(type.trim(), 5.0);
        }
    }

    public void removeRiceType(String type) {
        riceDishTypes.remove(type);
        ricePrices.remove(type);
    }

    // --- Rice Price Getters & Setters ---
    public double getRicePricePerMun(String riceType) {
        return ricePrices.getOrDefault(riceType, 5.0);
    }

    public double getRicePricePerMun() {
        if (!riceDishTypes.isEmpty()) {
            return ricePrices.getOrDefault(riceDishTypes.get(0), 5.0);
        }
        return 5.0;
    }

    public void setRicePricePerMun(String riceType, double price) {
        if (riceType != null && !riceType.trim().isEmpty()) {
            ricePrices.put(riceType.trim(), price);
        }
    }

    public void setRicePricePerMun(double price) {
        if (!riceDishTypes.isEmpty()) {
            ricePrices.put(riceDishTypes.get(0), price);
        }
    }

    // --- Protein Types Management ---
    public List<String> getProteinTypes() {
        return proteinTypes;
    }

    public void setProteinTypes(List<String> proteinTypes) {
        if (proteinTypes != null) {
            this.proteinTypes = new ArrayList<>(proteinTypes);
            for (String type : proteinTypes) {
                proteinPrices.putIfAbsent(type, 10.0);
                proteinCookingFees.putIfAbsent(type, 10.0);
            }
        }
    }

    public void addProteinType(String type) {
        if (type != null && !type.trim().isEmpty() && !proteinTypes.contains(type.trim())) {
            proteinTypes.add(type.trim());
            proteinPrices.put(type.trim(), 10.0);
            proteinCookingFees.putIfAbsent(type.trim(), 10.0);
        }
    }

    public void removeProteinType(String type) {
        proteinTypes.remove(type);
        proteinPrices.remove(type);
        proteinCookingFees.remove(type);
    }

    // --- Dynamic Protein Price & Cooking Fee Getters & Setters ---
    public double getProteinPrice(String proteinName) {
        return proteinPrices.getOrDefault(proteinName, 10.0);
    }

    public void setProteinPrice(String proteinName, double price) {
        if (proteinName != null && !proteinName.trim().isEmpty()) {
            proteinPrices.put(proteinName.trim(), price);
            if (proteinName.equals("بقر")) beefPricePerMun = price;
            if (proteinName.equals("دجاج")) chickenPricePerMun = price;
            if (proteinName.equals("سمك")) fishPricePerMun = price;
        }
    }

    public double getProteinCookingFee(String proteinName) {
        return proteinCookingFees.getOrDefault(proteinName, 10.0);
    }

    public void setProteinCookingFee(String proteinName, double fee) {
        if (proteinName != null && !proteinName.trim().isEmpty()) {
            proteinCookingFees.put(proteinName.trim(), fee);
            if (proteinName.equals("بقر")) beefCookingFeePerMun = fee;
            if (proteinName.equals("دجاج")) chickenCookingFeePerMun = fee;
            if (proteinName.equals("سمك")) fishCookingFeePerMun = fee;
            if (proteinName.equals("ضأن")) muttonCookingFee = fee;
        }
    }

    // --- Legacy Protein Price Getters & Setters ---
    public double getBeefPricePerMun() {
        return getProteinPrice("بقر");
    }

    public void setBeefPricePerMun(double beefPricePerMun) {
        setProteinPrice("بقر", beefPricePerMun);
    }

    public double getChickenPricePerMun() {
        return getProteinPrice("دجاج");
    }

    public void setChickenPricePerMun(double chickenPricePerMun) {
        setProteinPrice("دجاج", chickenPricePerMun);
    }

    public double getFishPricePerMun() {
        return getProteinPrice("سمك");
    }

    public void setFishPricePerMun(double fishPricePerMun) {
        setProteinPrice("سمك", fishPricePerMun);
    }

    // --- Legacy Cooking Fee Getters & Setters ---
    public double getBeefCookingFeePerMun() {
        return getProteinCookingFee("بقر");
    }

    public void setBeefCookingFeePerMun(double beefCookingFeePerMun) {
        setProteinCookingFee("بقر", beefCookingFeePerMun);
    }

    public double getChickenCookingFeePerMun() {
        return getProteinCookingFee("دجاج");
    }

    public void setChickenCookingFeePerMun(double chickenCookingFeePerMun) {
        setProteinCookingFee("دجاج", chickenCookingFeePerMun);
    }

    public double getFishCookingFeePerMun() {
        return getProteinCookingFee("سمك");
    }

    public void setFishCookingFeePerMun(double fishCookingFeePerMun) {
        setProteinCookingFee("سمك", fishCookingFeePerMun);
    }

    public double getMuttonCookingFee() {
        return getProteinCookingFee("ضأن");
    }

    public void setMuttonCookingFee(double muttonCookingFee) {
        setProteinCookingFee("ضأن", muttonCookingFee);
    }

    // --- Mutton Size Price Getters & Setters ---
    public double getMuttonFullPrice() {
        return muttonSizePrices.getOrDefault("كامل (Full)", 60.0);
    }

    public void setMuttonFullPrice(double price) {
        muttonSizePrices.put("كامل (Full)", price);
    }

    public double getMuttonHalfPrice() {
        return muttonSizePrices.getOrDefault("نصف (Half)", 35.0);
    }

    public void setMuttonHalfPrice(double price) {
        muttonSizePrices.put("نصف (Half)", price);
    }

    public double getMuttonQuarterPrice() {
        return muttonSizePrices.getOrDefault("ربع (Quarter)", 20.0);
    }

    public void setMuttonQuarterPrice(double price) {
        muttonSizePrices.put("ربع (Quarter)", price);
    }

    // --- Beef / Chicken Dish Types Management ---
    public List<String> getBeefChickenDishTypes() {
        return beefChickenDishTypes;
    }

    public void setBeefChickenDishTypes(List<String> beefChickenDishTypes) {
        if (beefChickenDishTypes != null) {
            this.beefChickenDishTypes = new ArrayList<>(beefChickenDishTypes);
        }
    }

    public void addBeefChickenDishType(String type) {
        if (type != null && !type.trim().isEmpty() && !beefChickenDishTypes.contains(type.trim())) {
            beefChickenDishTypes.add(type.trim());
        }
    }

    public void removeBeefChickenDishType(String type) {
        beefChickenDishTypes.remove(type);
    }

    // --- Fish Dish Types Management ---
    public List<String> getFishDishTypes() {
        return fishDishTypes;
    }

    public void setFishDishTypes(List<String> fishDishTypes) {
        if (fishDishTypes != null) {
            this.fishDishTypes = new ArrayList<>(fishDishTypes);
        }
    }

    public void addFishDishType(String type) {
        if (type != null && !type.trim().isEmpty() && !fishDishTypes.contains(type.trim())) {
            fishDishTypes.add(type.trim());
        }
    }

    public void removeFishDishType(String type) {
        fishDishTypes.remove(type);
    }

    // --- Price Calculations ---
    public double calculateRiceTotal(String riceType, int mun) {
        double rate = ricePrices.getOrDefault(riceType, 5.0);
        return mun * rate;
    }

    public double calculateBeefTotal(int mun) {
        return calculateDynamicProteinTotal("بقر", mun, false);
    }

    public double calculateChickenTotal(int mun) {
        return calculateDynamicProteinTotal("دجاج", mun, false);
    }

    public double calculateFishTotal(int mun) {
        return calculateDynamicProteinTotal("سمك", mun, false);
    }

    public double calculateMuttonTotal(String size, int quantity) {
        double rate = muttonSizePrices.getOrDefault(size, 60.0);
        return quantity * rate;
    }

    public double calculateDynamicProteinTotal(String proteinName, int mun, boolean isCustomerProvided) {
        double cookingFee = proteinCookingFees.getOrDefault(proteinName, 10.0);
        if (isCustomerProvided) {
            return mun * cookingFee;
        } else {
            double rawPrice = proteinPrices.getOrDefault(proteinName, 10.0);
            return mun * (rawPrice + cookingFee);
        }
    }
}