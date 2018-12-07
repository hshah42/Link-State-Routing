package linkstaterouting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Router
{
	private Map<Integer, Integer>		connectedNetworks		= new HashMap<>();
	private Map<Integer, Integer>		permanentNeighbourCost	= new HashMap<>();
	private Map<Integer, Integer>		sequenceNumberMap		= new HashMap<>();
	private Map<Integer, List<Integer>>	tickCount				= new HashMap<>();
	private Map<Integer, Router>		routers					= new HashMap<>();

	private Graph						networkGraph			= null;
	private String						networkName				= null;
	private ShortestPathAlgorithm		shortestPathAlgorithm	= null;

	private boolean						isActive				= true;
	private int							id						= 0;
	private int							sequenceNumber			= 0;
	private int							tick					= 0;
	private int							networkSize				= 0;

	public boolean isActive()
	{
		return isActive;
	}

	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	public String getNetworkName()
	{
		return networkName;
	}

	public void setNetworkName(String networkName)
	{
		this.networkName = networkName;
	}

	public int getSequenceNumber()
	{
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	public int getTick()
	{
		return tick;
	}

	public void setTick(int tick)
	{
		this.tick = tick;
	}

	public void setNetworkGraph(Graph networkGraph)
	{
		this.networkGraph = networkGraph;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setNetworkSize(int networkSize)
	{
		this.networkSize = networkSize;
	}

	public void addNetwork(int id, int cost)
	{
		connectedNetworks.put(id, cost);
		permanentNeighbourCost.put(id, cost);
	}

	public void setRouters(Map<Integer, Router> routers)
	{
		this.routers = routers;
	}

	public void printRoutingTable()
	{
		Map<Integer, RoutingTableInfo> routingTable = shortestPathAlgorithm.getRoutingTable();
		Set<Integer> keys = routingTable.keySet();

		for (int key : keys)
		{
			if (key == id)
			{
				continue;
			}

			RoutingTableInfo routingTableInfo = routingTable.get(key);

			if (routingTableInfo.getPath().size() > 0)
			{
				int cost = routingTableInfo.getCost();
				String costString = "";

				if (cost >= 0)
				{
					costString = costString + cost;
				}
				else
				{
					costString = "Router Down";
				}

				System.out.println(
						routingTableInfo.getNetwork() + " " + routingTableInfo.getPath().get(1) + " " + costString);
			}

		}
	}

	public void start()
	{
		networkGraph = new Graph(networkSize);

		Set<Integer> keys = connectedNetworks.keySet();
		LinkedList<LinkStateRouterInfo> list = new LinkedList<>();

		for (int neighbourId : keys)
		{
			Router router = routers.get(neighbourId);
			LinkStateRouterInfo linkStateRouterInfo = new LinkStateRouterInfo(neighbourId,
					connectedNetworks.get(neighbourId), router.getNetworkName());
			list.add(linkStateRouterInfo);
		}

		networkGraph.insertAt(id, list);

		shortestPathAlgorithm = new ShortestPathAlgorithm(networkGraph, routers, this);
	}

	public void resetNeighbourGraph()
	{
		Set<Integer> keys = connectedNetworks.keySet();
		LinkedList<LinkStateRouterInfo> list = new LinkedList<>();

		for (int neighbourId : keys)
		{
			Router router = routers.get(neighbourId);
			LinkStateRouterInfo linkStateRouterInfo = new LinkStateRouterInfo(neighbourId,
					connectedNetworks.get(neighbourId), router.getNetworkName());
			list.add(linkStateRouterInfo);
		}

		networkGraph.insertAt(id, list);
	}

	public void computeShortestPath()
	{
		shortestPathAlgorithm.computeShortestPath(networkGraph);
	}

	public boolean receivePacket(LinkStatePacket linkStatePacket)
	{
		if (isActive)
		{
			linkStatePacket.decrementTTL();

			if (linkStatePacket.getTimeToLive() < 0)
			{
				return true;
			}

			if (sequenceNumberMap.get(linkStatePacket.getId()) == null
					|| (sequenceNumberMap.get(linkStatePacket.getId()) < linkStatePacket.getSequenceNumber()))
			{
				networkGraph.insertAt(linkStatePacket.getId(), linkStatePacket.getConnectedLinks());
				sequenceNumberMap.put(linkStatePacket.getId(), linkStatePacket.getSequenceNumber());

				shortestPathAlgorithm.computeShortestPath(networkGraph);
				int sourceId = linkStatePacket.getSourceId();

				linkStatePacket.setSourceId(id);

				Set<Integer> routerIds = connectedNetworks.keySet();

				for (int id : routerIds)
				{
					Router router = routers.get(id);

					if (router.getId() != sourceId)
					{
						router.receivePacket(linkStatePacket);
					}
				}
			}

			return true;
		}
		else
		{
			return false;
		}
	}

	public void originatePacket()
	{
		if (!isActive)
		{
			return;
		}

		tick++;
		sequenceNumber++;

		LinkStatePacket lsp = new LinkStatePacket();
		lsp.setId(id);
		lsp.setSequenceNumber(sequenceNumber);
		lsp.setSourceId(id);

		List<Router> destinations = new ArrayList<>();

		Set<Integer> keyset = connectedNetworks.keySet();

		for (int neighbourRouterId : keyset)
		{
			String network = null;

			tickCount.put(sequenceNumber, new ArrayList<>());
			int currentTickCount = 0;

			for (int i = 1; i <= 2; i++)
			{
				if (sequenceNumber > 2)
				{
					List<Integer> prevTickMembers = tickCount.get(sequenceNumber - i);

					if (prevTickMembers == null)
					{
						currentTickCount++;
					}
					else
					{
						if (!prevTickMembers.contains(neighbourRouterId))
						{
							currentTickCount++;
						}
					}
				}
			}

			Router router = routers.get(neighbourRouterId);
			network = router.getNetworkName();
			destinations.add(router);

			if (currentTickCount >= 2)
			{
				connectedNetworks.put(neighbourRouterId, -1);
				//networkGraph.getInfoByID(neighbourRouterId);
				resetNeighbourGraph();
				shortestPathAlgorithm.computeShortestPath(networkGraph);
			}
			else
			{
				if (connectedNetworks.get(neighbourRouterId) == -1)
				{
					connectedNetworks.put(neighbourRouterId, permanentNeighbourCost.get(neighbourRouterId));
				}
			}

			LinkStateRouterInfo linkStateRouterInfo = new LinkStateRouterInfo(neighbourRouterId,
					connectedNetworks.get(neighbourRouterId), network);

			lsp.addConnectedLink(linkStateRouterInfo);
		}

		lsp.setNetworkGraph(networkGraph);

		for (Router router : destinations)
		{
			lsp.resetTTL();
			boolean success = router.receivePacket(lsp);

			if (success)
			{
				List<Integer> listForTicks = tickCount.get(tick);

				if (listForTicks == null)
				{
					listForTicks = new ArrayList<>();
				}

				listForTicks.add(router.getId());

				tickCount.put(tick, listForTicks);
			}
		}
	}
}
