package gym.management.Sessions;

public enum SessionType {
    Pilates("Pilates", 60, 30),
    MachinePilates("Machine Pilates", 80, 10),
    ThaiBoxing("Thai Boxing", 100, 20),
    Ninja("Ninja", 150, 5);

    private final String name;
    private final int price;
    private final int maxParticipants;

    SessionType(String name, int price, int maxParticipants) {
        this.name = name;
        this.price = price;
        this.maxParticipants = maxParticipants;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }
}
