package com.soda_machine.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

public class PromotionSetting {
    private int limitBudget;
    private int remainBudget;

    public int getRemainBudget() {
        return remainBudget;
    }

    private int limitConsecutiveProduct;
    private int originalChance;
    private int increasePercentage;

    // FOR CALCULATE BUDGET IN A DAY
    private LocalDate dateGetPromotion;
    private LocalDate dateForStartCalChance;

    private Product consecutiveProduct;

    public PromotionSetting(int limitBudget, int originalChance, int increasePercentage, int limitConsecutiveProduct){
        this.limitBudget = limitBudget;
        this.remainBudget = limitBudget;
        this.originalChance = originalChance;
        this.increasePercentage = increasePercentage;
        this.limitConsecutiveProduct = limitConsecutiveProduct;
        this.dateForStartCalChance = LocalDate.now();
        this.dateGetPromotion = LocalDate.now();
    }

    long calculateChancePercentage(){
        LocalDate now = LocalDate.now();
        long diffDays = dateForStartCalChance.until(now, ChronoUnit.DAYS);
        double multiplyFactor =  Math.pow( 1 + ((float)this.increasePercentage / 100), diffDays);
        return multiplyFactor * originalChance > 100 ? 100 : Math.round(originalChance * multiplyFactor);
    }

    public boolean checkPromotion(Product product){
        LocalDate now = LocalDate.now();
        long diffDays = now.until(dateGetPromotion, ChronoUnit.DAYS);
        if(diffDays > 0){
            // RESET BUDGET
            remainBudget = limitBudget;
        }

        if(consecutiveProduct != null && consecutiveProduct.getId() == product.getId()){

            // CHECK CONSECUTIVE EXCEED LIMIT OR NOT
            consecutiveProduct.setCurrentAmount(consecutiveProduct.getCurrentAmount() + 1);
            return consecutiveProduct.getCurrentAmount() >= limitConsecutiveProduct;
        }
        else{
            consecutiveProduct = new Product(product.getId(), product.getName(), product.getPrice(), 1);
        }

        return false;
    }

    public Product processPromotion(ArrayList<Product> machineProducts ){
        // RESET
        consecutiveProduct = null;
        dateGetPromotion = LocalDate.now();

        // RANDOM PROMOTION PRODUCT
        int chancePercentage = (int)calculateChancePercentage();
        double random = Math.random() * 100;

        if(random <= chancePercentage){
            Product[] productsAvailable = machineProducts.stream().filter(o -> o.getCurrentAmount() > 0 && o.getPrice() <= remainBudget).toArray(Product[]::new);
            if(productsAvailable.length > 0){
                int randomProductIndex = (int)Math.round(Math.random() * (productsAvailable.length - 1));

                // CHECK REMAIN BUDGET EXCEED LIMIT IN A DAY OR NOT
                remainBudget -= productsAvailable[randomProductIndex].getPrice();
                if(remainBudget <= 0){
                    dateForStartCalChance = LocalDate.now();
                }

                // DECREASE PRODUCT IN MACHINE
                productsAvailable[randomProductIndex].setCurrentAmount(productsAvailable[randomProductIndex].getCurrentAmount() - 1);
                return productsAvailable[randomProductIndex];
            }
        }
        return null;
    }
}
