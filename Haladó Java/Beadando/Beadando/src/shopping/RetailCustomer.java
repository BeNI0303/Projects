package shopping;

import java.util.List;

public class RetailCustomer {
    int budget;

    public RetailCustomer() {
        budget = (int) (Math.random() * (1000 - 100 + 1)) + 100;
    }

    @ShoppingListValidator
    public boolean checkBudget(List<ItemCategory> categories, double price){
        if(price <= budget){
            return true;
        }
        return false;
    }
}
