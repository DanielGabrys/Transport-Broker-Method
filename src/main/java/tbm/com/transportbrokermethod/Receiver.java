package tbm.com.transportbrokermethod;

public class Receiver
{
    private String name;
    private String demand;
    private String cost;

    public Receiver(String name,String demand,String cost)
    {
        this.name=name;
        this.demand=demand;
        this.cost=cost;
    }

    public String getName() {
        return name;
    }

    public String getDemand() {
        return demand;
    }

    public String getCost() {
        return cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
