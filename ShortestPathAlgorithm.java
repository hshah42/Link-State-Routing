package linkstaterouting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShortestPathAlgorithm
{
	private Map<Integer, RoutingTableInfo>	routingTable	= null;
	private Graph							graph			= null;
	private Map<Integer, Router>			routers			= null;
	private Router							router			= null;

	public Map<Integer, RoutingTableInfo> getRoutingTable()
	{
		return routingTable;
	}

	public ShortestPathAlgorithm(Graph graph, Map<Integer, Router> routers, Router router)
	{
		this.graph = graph;
		this.routers = routers;
		this.router = router;

		init();
	}

	public void computeShortestPath()
	{
		this.computeShortestPath(graph);
	}

	public void computeShortestPath(Graph graph)
	{
		reset();

		Set<Integer> visited = new HashSet<>();
		Set<Integer> awaiting = new HashSet<>();

		boolean isNew = true;

		Router currentRouter = router;

		while ((awaiting.size() > 0 || currentRouter != null) || isNew)
		{
			isNew = false;

			if (visited.contains(currentRouter.getId()))
			{
				int nextNodeID = getNextrounter(awaiting);
				currentRouter = routers.get(nextNodeID);

				continue;
			}

			LinkedList<LinkStateRouterInfo> neighbours = graph.getInfoByID(currentRouter.getId());
			visited.add(currentRouter.getId());

			if (!graph.contains(currentRouter.getId()))
			{
				int nextNodeID = getNextrounter(awaiting);
				currentRouter = routers.get(nextNodeID);

				continue;
			}

			for (LinkStateRouterInfo linkStateRouterInfo : neighbours)
			{
				if (linkStateRouterInfo.getId() == router.getId())
				{
					continue;
				}

				RoutingTableInfo routingTableInfo = routingTable.get(linkStateRouterInfo.getId());

				int prevCost = routingTableInfo.getCost();

				if (prevCost < 0 || prevCost > (linkStateRouterInfo.getCost()
						+ routingTable.get(currentRouter.getId()).getCost()))
				{
					if (currentRouter.getId() != router.getId())
					{
						if (linkStateRouterInfo.getCost() < 0)
						{
							routingTableInfo.setCost(-1);
						}
						else
						{
							routingTableInfo.setCost(
									routingTable.get(currentRouter.getId()).getCost() + linkStateRouterInfo.getCost());
						}
					}
					else
					{
						routingTableInfo.setCost(linkStateRouterInfo.getCost());
					}

					List<Integer> path = new ArrayList<>(routingTable.get(currentRouter.getId()).getPath());
					path.add(currentRouter.getId());
					path.add(linkStateRouterInfo.getId());

					routingTableInfo.setPath(path);
					routingTable.put(linkStateRouterInfo.getId(), routingTableInfo);
				}

				if (routingTableInfo.getCost() > 0)
				{
					awaiting.add(linkStateRouterInfo.getId());
				}
			}

			int nextNodeID = getNextrounter(awaiting);

			currentRouter = routers.get(nextNodeID);
		}
	}

	private int getNextrounter(Set<Integer> awaiting)
	{
		int lowestCost = -1;
		int lowestCostId = -1;

		for (int id : awaiting)
		{
			if (lowestCost < 0)
			{
				lowestCost = routingTable.get(id).getCost();
				lowestCostId = id;
			}
			else
			{
				if (lowestCost > routingTable.get(id).getCost())
				{
					lowestCost = routingTable.get(id).getCost();
					lowestCostId = id;
				}
			}
		}

		if (lowestCostId != -1)
		{
			awaiting.remove(lowestCostId);
		}

		return lowestCostId;
	}

	private void reset()
	{
		Set<Integer> routingIds = routingTable.keySet();

		for (Integer routingId : routingIds)
		{
			RoutingTableInfo routingTableInfo = routingTable.get(routingId);
			routingTableInfo.setCost(-1);
			routingTable.put(routingId, routingTableInfo);
		}
	}

	private void init()
	{
		routingTable = new HashMap<>();
		Set<Integer> routerKeys = routers.keySet();

		for (int routerId : routerKeys)
		{
			Router currentRouter = routers.get(routerId);

			RoutingTableInfo routingTableInfo = new RoutingTableInfo();
			routingTableInfo.setCost(-1);
			routingTableInfo.setId(currentRouter.getId());
			routingTableInfo.setNetwork(currentRouter.getNetworkName());

			routingTable.put(currentRouter.getId(), routingTableInfo);
		}
	}

}
