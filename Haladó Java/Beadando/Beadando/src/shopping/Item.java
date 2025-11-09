package shopping;

public class Item {
    private String name;
    private ItemCategory category;

    public Item(String name, ItemCategory category){
        this.name = name;
        this.category = category;
    }

    public boolean isHealthy() throws IllegalStateException{
        if(category == ItemCategory.ALCOHOL){
            return false;
        }else if(category == ItemCategory.FOOD){
            return true;
        }else{
            throw new IllegalStateException("Not supported question for: <"+category+">");
        }
    }

    public ItemCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " " + category;
    }
}
