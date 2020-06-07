package menu.menu_fridge.models;

public enum Category {
    SOUPS,
    SECOND,
    MEAT,
    SEAFOOD,
    SALADS,
    SNACKS,
    DESSERTS,
    DRINKS,
    OTHER;

    public String nameCategory(Category category) {
        String name = "";
        if (category == Category.SOUPS) name = "супы";
        else if (category == Category.SECOND) name = "вторые блюда";
        else if (category == Category.MEAT) name = "мясо, птица";
        else if (category == Category.SEAFOOD) name = "рыба";
        else if (category == Category.SALADS) name = "салаты";
        else if (category == Category.SNACKS) name = "закуски";
        else if (category == Category.DESSERTS) name = "десерты";
        else if (category == Category.DRINKS) name = "напитки";
        else if (category == Category.OTHER) name = "другое";
        return name;
    }
}
