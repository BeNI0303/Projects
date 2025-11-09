package shopping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shop {

    public Map<Item,Double> itemWithPrice = new HashMap<>();
    public Map<Item,Integer> itemWithStock = new HashMap<>();

    public Shop() {
        try (Stream<String> lines = Files.lines(Paths.get("warehouse.txt"))){
            lines.forEach(line -> {
                String[] data = line.split(",");
                Item currItem = new Item(data[0], ItemCategory.valueOf(data[1]));
                itemWithPrice.put(currItem, Double.parseDouble(data[2]));
                itemWithStock.put(currItem, 100);
            });
        } catch (IOException e) {
            System.err.println("Hiba a file beolvasása közben: "+ e);
        }
    }
    

    public synchronized Map<Item,Integer> generateRandomShoppingList(){
        Random random = new Random();
        List<Item> availableItems = itemWithStock.entrySet().stream()
                                        .filter(entry -> entry.getValue() > 0)
                                        .map(entry -> entry.getKey())
                                        .collect(Collectors.toList());
        Collections.shuffle(availableItems);

        int itemCount = random.nextInt(availableItems.size())+1;

        HashMap<Item, Integer> shoppingList = new HashMap<>();

        for (int i = 0; i < itemCount; i++) {
            shoppingList.put(availableItems.get(i), random.nextInt(24)+1);
        }

        return shoppingList;
    }

    public synchronized void tryToBuy(Map<Item, Integer> m, Object o){
        for (Map.Entry<Item,Integer> entry : m.entrySet()) {
            if(itemWithStock.getOrDefault(entry.getKey(),0) < entry.getValue() ){
                System.err.println("Purchase is not possible");
                return;
            }
        }

        double sumPrice = m.entrySet().stream()
                            .mapToDouble(entry -> itemWithPrice.get(entry.getKey()) * entry.getValue()).sum();


        HashSet<ItemCategory> categories = new HashSet<>();
        for (Item item : m.keySet()) {
            categories.add(item.getCategory());
        }
        List<ItemCategory> sortedCategories = new ArrayList<>(categories);

        boolean isAnnotationFound = false;
        for (Method method : o.getClass().getDeclaredMethods()) {
            if(method.isAnnotationPresent(ShoppingListValidator.class)){
                isAnnotationFound = true;
                try {
                    if((boolean)method.invoke(o, sortedCategories,sumPrice)){
                        System.out.println("Sell in " + sortedCategories + " for " + sumPrice);
                        itemWithStock = itemWithStock.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue() - m.getOrDefault(e.getKey(),0)));
                    }else{
                        System.out.println("Rejected sell");
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        if(!isAnnotationFound){
            System.out.println("Sell in " + sortedCategories + " for " + sumPrice);
            itemWithStock = itemWithStock.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue() - m.getOrDefault(e.getKey(),0)));
        }
    }
}
