package com.soda_machine.models;

import java.util.ArrayList;

public class Machine {
    private String error;
    private String notification;
    private int remainMoney;

    private ArrayList<Product> products;
    private PromotionSetting promotionSetting;
    private ArrayList<Note> machineNotes;
    private ArrayList<Note> currentReceiveNotes;

    public void setError(String error) {
        this.error = error;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public int getRemainMoney() {
        return remainMoney;
    }

    private static Machine singleInstance = null;

    public Machine() {

    }

    public static Machine getInstance(){
        if(singleInstance == null){
            // INITIALIZE MACHINE STATE;
            singleInstance = new Machine();

            singleInstance.error = "";
            singleInstance.notification = "";
            singleInstance.promotionSetting = new PromotionSetting(50000, 10, 50, 3);

            singleInstance.products = new ArrayList<>();
            singleInstance.products.add(new Product(1,"Coke", 10000, 100));
            singleInstance.products.add(new Product(2,"Pepsi", 10000, 100));
            singleInstance.products.add(new Product(3, "Soda", 20000, 100));

            singleInstance.currentReceiveNotes = new ArrayList<>();
            singleInstance.currentReceiveNotes.add(new Note(10000, 0, 1));
            singleInstance.currentReceiveNotes.add(new Note(20000, 0, 2));
            singleInstance.currentReceiveNotes.add(new Note(50000, 0, 3));
            singleInstance.currentReceiveNotes.add(new Note(100000, 0, 4));
            singleInstance.currentReceiveNotes.add(new Note(200000, 0, 5));

            singleInstance.machineNotes = new ArrayList<>();
        }
        return singleInstance;
    }

    public boolean getUserNote(int input){
        for (Note note : this.currentReceiveNotes){
            if(note.getId() == input){
                note.setAmount(note.getAmount() + 1);
                return true;
            }
        }
        return false;
    }

    public String giveBackMoney(){
        for (Note note : this.currentReceiveNotes){
            note.setAmount(0);
        }

        return "You're paid back the money";
    }

    public boolean giveUserProduct(int input){
        for (Product product : this.products){
            if(product.getId() == input){
                if(product.getCurrentAmount() <= 0){
                    error = "The selected product in machine is empty! ";
                    return false;
                }

                if(remainMoney - product.getPrice() < 0){
                    error = "The remain money is not enough!";
                    return false;
                }

                Product promotionProduct = null;
                if(promotionSetting.checkPromotion(product)){
                    promotionProduct = promotionSetting.processPromotion(products);
                }

                String notificationString = "You got 1 " + product.getName();

                if(promotionProduct != null){
                    notificationString += " and 1 " + promotionProduct.getName() + " by promotion program";
                }

                notification = notificationString;
                remainMoney -= product.getPrice();
                product.setCurrentAmount(product.getCurrentAmount() - 1);
                return true;
            }
        }
        return false;
    }

    public void getUserMoney(){
        for(Note note : this.currentReceiveNotes){
            if(note.getAmount() > 0){
                boolean found = false;
                for (Note machineNote: this.machineNotes){
                    if(note.getId() == machineNote.getId()){
                        machineNote.setAmount(machineNote.getAmount() + note.getAmount());
                        found = true;
                        break;
                    }
                }

                if(!found){
                    machineNotes.add(new Note(note.getValue(), note.getAmount(), note.getId()));
                }

                remainMoney += note.getSum();
                note.setAmount(0);
            }
        }
    }

    public void displayMachineStatus(){
        System.out.println("THE PRODUCTS REMAIN IN MACHINE :");

        for(Product product : products){
            System.out.println(product.getName() + " - Price: " +  product.getPrice() + " - Remain: " + product.getCurrentAmount());
        }
        System.out.println("==============================");
        System.out.println("THE MONEY IN MACHINE :");
        for(Note note : machineNotes){
            System.out.println(note.getValue() + " - Amount: " +  note.getAmount());
        }
        System.out.println("SUM: " + Note.getSumNotes(machineNotes));
        System.out.println("==============================");
        System.out.println("THE REMAIN BUDGET IN MACHINE TODAY :");
        System.out.println(promotionSetting.getRemainBudget());
        System.out.println("==============================");
    }

    void displayNotification(){
        if(error != ""){
            System.out.println("ERR: " + error);
            Machine.getInstance().setError("");
        }

        if(notification != ""){
            System.out.println("NOTIFICATION: " + notification);
            Machine.getInstance().setNotification("");
        }
    }

    public void displayNoteListToSelect() {
        displayNotification();
        System.out.println("Choose your note to put in the machine");
        for(Note note : currentReceiveNotes){
            System.out.println(note.getValue() + " - Press " + note.getId() + "-> Enter");
        }
        int totalMachineReceived = Note.getSumNotes(currentReceiveNotes);
        System.out.println("=========================");
        System.out.println("The machine received total: " + totalMachineReceived + " - Remain money previous turn: " + Machine.getInstance().getRemainMoney());
        System.out.println("Press '0' -> Enter to cancel and Press 'c' -> Enter to Choose Product (Maintainer: Press 's' -> Enter for check machine status )");
    }

    public void displayProductListToSelect(){
        displayNotification();
        System.out.println("Choose product in the machine: ");
        for(Product product : products){
            System.out.println(product.getName() + " - Price: " +  product.getPrice() + " - Remain: " + product.getCurrentAmount() + " -- " + product.getId());
        }
        System.out.println("Press '0' -> Enter to cancel. Press the code's product -> Enter to select your suitable one. Money remain " + Machine.getInstance().getRemainMoney() );
    }
}
