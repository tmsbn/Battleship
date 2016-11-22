/**
 * Player class to control the play logic
 **/
public class Player {

    private String name;
    private Ocean ocean;
    private Fleet fleet;

    public Player(String name, Fleet fleet) {
        this.name = name;
        this.ocean = fleet.getOcean();
        this.fleet = fleet;

    }

    public String getName() {
        return name;
    }

    public Ocean getOcean() {
        return ocean;
    }

    public Fleet getFleet() {
        return fleet;
    }


}
