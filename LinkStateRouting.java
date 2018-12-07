package linkstaterouting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LinkStateRouting
{
	private static Map<Integer, Router>	routers		= new HashMap<>();
	private static BufferedReader		fileReader	= null;
	private static BufferedReader		inputReader	= null;

	public static void main(String[] args)
	{
		try
		{
			init("infile.dat");
		}
		catch (IOException e)
		{
			System.err.println("error in reading file : " + e.getMessage());
		}

		try
		{
			startNetwork();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("error in routing : " + e.getMessage());
		}
	}

	private static void init(String fileName) throws IOException
	{
		fileReader = new BufferedReader(
				new InputStreamReader(LinkStateRouting.class.getResourceAsStream("infile.dat")));
		String line;

		Router router = null;

		while ((line = fileReader.readLine()) != null)
		{
			if (line.startsWith(" "))
			{
				String[] data = line.split(" ");
				int neighbourId = Integer.parseInt(data[1]);
				int cost = 1;

				if (data.length == 3)
				{
					cost = Integer.parseInt(data[2]);
				}

				router.addNetwork(neighbourId, cost);
			}
			else
			{
				router = new Router();
				String[] data = line.split(" ");

				router.setId(Integer.parseInt(data[0]));
				router.setNetworkName(data[1]);
			}

			routers.put(router.getId(), router);
		}

		Set<Integer> keys = routers.keySet();

		for (int routerID : keys)
		{
			router = routers.get(routerID);
			router.setRouters(routers);
			router.setNetworkSize(routers.size());
			router.start();

			routers.put(router.getId(), router);
		}

		fileReader.close();
	}

	private static void startNetwork() throws IOException
	{
		inputReader = new BufferedReader(new InputStreamReader(System.in));
		Set<Integer> keys = routers.keySet();

		String input = "";

		while (!input.equalsIgnoreCase("q"))
		{
			System.out.println("Select an option:");
			System.out.println("C for continue");
			System.out.println("P router_id (separated by space) to print a routing table");
			System.out.println("S router_id (separated by space) to switch off a router");
			System.out.println("T router_id (separated by space) to switch on a router");
			System.out.println("Q to quit");

			input = inputReader.readLine();

			input = input.toLowerCase();

			if (input.equals("c"))
			{
				for (int id : keys)
				{
					Router router = routers.get(id);
					router.originatePacket();
				}
				
				for (int id : keys)
				{
					Router router = routers.get(id);
					//router.computeShortestPath();
				}
			}
			else if (input.startsWith("s"))
			{
				String[] inputData = input.split(" ");

				switchOnOffRouter(inputData, "OFF");

				for (int id : keys)
				{
					Router router = routers.get(id);
					router.setRouters(routers);
					routers.put(id, router);
				}

			}
			else if (input.startsWith("t"))
			{
				String[] inputData = input.split(" ");

				switchOnOffRouter(inputData, "ON");

				for (int id : keys)
				{
					Router router = routers.get(id);
					router.setRouters(routers);
					routers.put(id, router);
				}
				

			}
			else if (input.equals("q"))
			{
				break;
			}
			else if (input.startsWith("p"))
			{
				String[] inputData = input.split(" ");

				Router router = routers.get(Integer.parseInt(inputData[1]));

				router.printRoutingTable();
			}
			else
			{
				System.err.println("Enter a valid input!");
			}
		}

		System.out.println("Quitting...");
	}

	private static void switchOnOffRouter(String[] inputData, String status)
	{
		if (inputData.length == 2)
		{
			int id = Integer.parseInt(inputData[1]);
			Router router = routers.get(id);

			if (status.equals("OFF"))
			{
				router.setActive(false);
			}
			else
			{
				router.setActive(true);
			}

			routers.put(id, router);
		}
		else
		{
			System.err.println("Please enter in correct format...");
		}
	}
}
