package linkstaterouting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph
{
	List<LinkedList<LinkStateRouterInfo>> adjacencyList;

	public Graph(int size)
	{
		adjacencyList = new ArrayList<>(size);

		for (int i = 0; i < size; i++)
		{
			adjacencyList.add(null);
		}
	}

	public void insertAt(int id, LinkedList<LinkStateRouterInfo> directlyConnectedNodes)
	{
		adjacencyList.set(id, directlyConnectedNodes);
	}

	public LinkedList<LinkStateRouterInfo> getInfoByID(int id)
	{
		return adjacencyList.get(id);
	}

	public boolean contains(int id)
	{
		if(adjacencyList.get(id) == null)
		{
			return false;
		}
		
		return true;
	}

	public int size()
	{
		int size = 0;

		for (int i = 0; i < adjacencyList.size(); i++)
		{
			if (adjacencyList.get(i) != null)
			{
				size++;
			}
		}

		return size;
	}
}
