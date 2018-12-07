package linkstaterouting;

public class LinkStateRouterInfo
{

	public LinkStateRouterInfo(int id, int cost, String network)
	{
		super();
		this.id = id;
		this.cost = cost;
		this.network = network;
	}

	private int		id		= 0;
	private int		cost	= 0;
	private String	network	= null;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getCost()
	{
		return cost;
	}

	public void setCost(int cost)
	{
		this.cost = cost;
	}

	public String getNetwork()
	{
		return network;
	}

	public void setNetwork(String network)
	{
		this.network = network;
	}

}
