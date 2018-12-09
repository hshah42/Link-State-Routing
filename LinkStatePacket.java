package linkstaterouting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinkStatePacket
{
	private int								id					= 0;
	private int								sourceId			= 0;
	private LinkedList<LinkStateRouterInfo>	connectedLinks		= new LinkedList<>();
	private List<Integer>					disconnectedRouters	= new ArrayList<>();
	private Graph							networkGraph		= null;

	private int								sequenceNumber		= 0;
	private int								timeToLive			= 1;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getSequenceNumber()
	{
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	public int getTimeToLive()
	{
		return timeToLive;
	}

	public void decrementTTL()
	{
		timeToLive--;
	}

	public boolean isActive()
	{
		if (timeToLive > 0)
		{
			return true;
		}

		return false;
	}

	public void resetTTL()
	{
		timeToLive = 1;
	}

	public void addConnectedLink(LinkStateRouterInfo linkStateRouterInfo)
	{
		connectedLinks.add(linkStateRouterInfo);
	}

	public LinkedList<LinkStateRouterInfo> getConnectedLinks()
	{
		return connectedLinks;
	}

	public int getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(int sourceIp)
	{
		this.sourceId = sourceIp;
	}

	public Graph getNetworkGraph()
	{
		return networkGraph;
	}

	public void setNetworkGraph(Graph networkGraph)
	{
		this.networkGraph = networkGraph;
	}

	public List<Integer> getDisconnectedRouters()
	{
		return disconnectedRouters;
	}

	public void addDisconnectedRouter(int id)
	{
		disconnectedRouters.add(id);
	}
}
