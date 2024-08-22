package com.yourpackage.models;

public class Event {
    private int id;
    private String name;
    private String distance;
    private int temperature;

    public Event(int id, String name, String distance, int temperature) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.temperature = temperature;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }
    
    public int getTemperature() {
        return temperature;
    }

    // Override toString() method - dropdown menu listhez
    @Override
    public String toString() {
        return "Esemény{" +
                "id=" + id +
                ", Név='" + name + '\'' +
                ", Táv='" + distance + '\'' +
                ", Hőmérséklet=" + temperature +
                '}';
    }
}