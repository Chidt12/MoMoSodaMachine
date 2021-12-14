package com.soda_machine;

import com.soda_machine.models.Machine;
import com.soda_machine.models.Note;
import com.soda_machine.models.Product;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {

        while (true){
            Machine.getInstance().displayNoteListToSelect();

            int input = System.in.read();
            // AVOID ENTER PRESS IN PREVIOUS TURN
            if(input == 10){
                input = System.in.read();
            }

            // Press '0'
            if(input - 48 == 0){
                Machine.getInstance().setNotification(Machine.getInstance().giveBackMoney());
                continue;
            }

            // Press 's'
            if(input == 115){
                Machine.getInstance().displayMachineStatus();
                continue;
            }

            //  Press 'c'
            if(input == 99){
                Machine.getInstance().getUserMoney();
                ChooseProductProcess();
                continue;
            }

            //  Press other keys
            boolean result = Machine.getInstance().getUserNote(input - 48);
            if(!result){
                Machine.getInstance().setError("The input key is not valid");
            }
            continue;
        }
    }


     static void ChooseProductProcess() throws IOException{
        System.out.println("********************************");
        while (true){
            Machine.getInstance().displayProductListToSelect();
            int input = System.in.read();
            // AVOID ENTER PRESS IN PREVIOUS TURN
            if(input == 10){
                input = System.in.read();
            }

            // Press '0'
            if(input - 48 == 0){
                break;
            }

            //  Press other keys
            boolean result = Machine.getInstance().giveUserProduct(input - 48);
            continue;
        }
    }
}
