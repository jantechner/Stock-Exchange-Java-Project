package stock;

import java.io.Serializable;

/** Klasa definiująca państwo */
public class Country implements Serializable{
    private String name;
    public Country(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
