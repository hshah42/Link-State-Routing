package linkstaterouting;

import java.util.ArrayList;
import java.util.List;

public class LinkStateRouterInfo
{

	public LinkStateRouterInfo(int id, int cost, String network)
	{
		super();
		this.id = id;
		this.cost = cost;
		this.network = network;
	}

	private int				id		= 0;
	private int				cost	= 0;
	private String			network	= null;
	private List<Integer>	path	= new ArrayList<>();

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

	public void addToPath(int routerId)
	{
		path.add(routerId);
	}

	public void setPath(List<Integer> path)
	{
		this.path = path;
	}

	public List<Integer> getPath()
	{
		return path;
	}

}
