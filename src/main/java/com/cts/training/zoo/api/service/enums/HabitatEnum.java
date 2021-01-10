package com.cts.training.zoo.api.service.enums;

/**
 * Available animal Habitat types.
 */
public enum HabitatEnum {
    WALKING("WALKING", "FOREST"),
    SWIMMING("SWIMMING", "OCEAN"),
    FLYING("FLYING", "NEST");
    private String type;
    private String name;

    HabitatEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }
    public String getType(){
        return this.type;
    }
    public String getName(){
        return this.name;
    }
}
