package linkstaterouting;

import java.util.ArrayList;
import java.util.List;

public class RoutingTableInfo
{
	private String			network			= null;
	private int				id				= 0;
	private int				outgoingLinkId	= -1;
	private int				cost			= 0;
	private List<Integer>	path			= new ArrayList<>();

	public String getNetwork()
	{
		return network;
	}

	public void setNetwork(String network)
	{
		this.network = network;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getOutgoingLinkId()
	{
		return outgoingLinkId;
	}

	public void setOutgoingLinkId(int outgoingLinkId)
	{
		this.outgoingLinkId = outgoingLinkId;
	}

	public int getCost()
	{
		return cost;
	}

	public void setCost(int cost)
	{
		this.cost = cost;
	}

	public void addPath(Integer id)
	{
		path.add(id);
	}

	public void remove(Integer id)
	{
		path.remove(id);
	}

	public List<Integer> getPath()
	{
		return path;
	}
	
	public void setPath(List<Integer> path)
	{
		this.path = path;
	}

}
