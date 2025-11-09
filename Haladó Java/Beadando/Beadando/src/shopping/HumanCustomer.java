package shopping;

import java.util.List;

public class HumanCustomer {
    int age;

    public HumanCustomer() {
        age = (int) (Math.random() * (67 - 12 + 1)) + 12;
    }

    @ShoppingListValidator
    public boolean canIBuy(List<ItemCategory> categories, double price){
        if(!categories.contains(ItemCategory.ALCOHOL)){
            return true;
        }else{
            if(age >= 18){
                return true;
            }
            return false;
        }
    }

    @Override
    public String toString() {
        return ""+age;
    }
}
