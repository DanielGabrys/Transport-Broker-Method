package tbm.com.transportbrokermethod;

public class Supplier
{
    private String name;
    private String supply;
    private String cost;

    public Supplier(String name,String supply,String cost)
    {
        this.name=name;
        this.supply=supply;
        this.cost=cost;
    }

    public String getName() {
        return name;
    }

    public String getSupply() {
        return supply;
    }

    public String getCost() {
        return cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
