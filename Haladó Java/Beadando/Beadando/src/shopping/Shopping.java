package shopping;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Shopping {
    public static void main(String[] args) {
        Shop shop = new Shop();

        ExecutorService executor = Executors.newFixedThreadPool(1000000);
        for (int i = 0; i < 50; i++) {
            executor.execute(() -> {
                try {
                    Map<Item, Integer> shoppingList = shop.generateRandomShoppingList();
                    Random random = new Random();
                    int customerType = random.nextInt(3);
                    Object customer;
                    switch (customerType) {
                        case 0:
                            customer = new HumanCustomer();
                            break;
                        case 1:
                            customer = new RetailCustomer();
                            break;
                        case 2:
                            customer = new NonRestrictedCustomer();
                            break;
                        default:
                            throw new IllegalStateException("Not available customer type: " + customerType);
                    }
                    
                    shop.tryToBuy(shoppingList, customer);
                } catch (Exception e) {
                    System.err.println("Error during buy!" + e.getMessage());
                }
            });
        }

        executor.shutdown();
    }
}
