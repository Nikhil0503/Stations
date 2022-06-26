package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	private static void addToEnd(int stop, TNode beginning){
		TNode prev = null;
		TNode current = beginning;
		if(current == null){
			TNode node = new TNode(stop);
			current = node;
		}else{
			while(current != null){
				prev = current;
				current = current.getNext();
			}
			TNode node = new TNode(stop);
			prev.setNext(node);
		}
	}

	public void makeList(int[] trainStations, int[] busStops, int[] locations) {
		trainZero = new TNode();
		TNode trainHead = trainZero;

		for (int i = 0; i < trainStations.length; i++) {
			addToEnd(trainStations[i], trainHead);
		}

		TNode busZero = new TNode();
		TNode busHead = busZero;
		trainHead.setDown(busHead);

		int trainIndex = 0;

		for(int i = 0; i < busStops.length; i++){
		 TNode prev = null;
		 TNode current = busHead;

			while(current != null){
				prev = current;
				current = current.getNext();
			}

			TNode busStop = new TNode(busStops[i]);
			prev.setNext(busStop);

			if((trainIndex < trainStations.length) && (busStops[i] == trainStations[trainIndex])){
				TNode frontTrain = trainHead;
				while(frontTrain != null && frontTrain.getLocation() != busStops[i]){
					frontTrain = frontTrain.getNext();
				}
				frontTrain.setDown(busStop);
				trainIndex++;
			}
		}	

		TNode walkZero = new TNode();
		TNode walkHead = walkZero;
		busHead.setDown(walkHead);
		int busIndex = 0;

		for (int i = 0; i < locations.length; i++) {
			TNode prev = null;
		 	TNode current = walkHead;

			while(current != null){
				prev = current;
				current = current.getNext();
			}

			TNode walkLocation = new TNode(locations[i]);
			prev.setNext(walkLocation);

			if((busIndex < busStops.length) && (locations[i] == busStops[busIndex])){
				TNode busNode = busHead;
				while(busNode != null && busNode.getLocation() != locations[i]){
					busNode = busNode.getNext();
				}
				busNode.setDown(walkLocation);
				busIndex++;
			}
		}
	}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station){
		TNode prev = null;
		TNode current = trainZero;
			while(current != null && current.getLocation() != station){
				prev = current;
				current = current.getNext();
			}
			if(current != null){
				prev.setNext(current.getNext());
			}
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
		TNode busZero = trainZero.getDown();
		TNode busHead = busZero;
		TNode prev = null;
		TNode current = busHead;

		while(current != null && current.getLocation() < busStop){
			prev = current;
			current = current.getNext();
		}

		TNode newBusStop = null;
		if(current == null){
			newBusStop = new TNode(busStop);
			newBusStop.setNext(current);
			prev.setNext(newBusStop);

			TNode walkZero = busZero.getDown();
			TNode prevWalkHead = null;
			TNode walkHead = walkZero;

			while(walkHead != null && walkHead.getLocation() != busStop){
				prevWalkHead = walkHead;
				walkHead = walkHead.getNext();
			}

			newBusStop.setDown(walkHead);
		}
		
		if(current != null && current.getLocation() != busStop){
			newBusStop = new TNode(busStop);
			newBusStop.setNext(current);
			prev.setNext(newBusStop);
			
			TNode walkZero = busZero.getDown();
			TNode prevWalkHead = null;
			TNode walkHead = walkZero;

			while(walkHead != null && walkHead.getLocation() != busStop){
				prevWalkHead = walkHead;
				walkHead = walkHead.getNext();
			}

			newBusStop.setDown(walkHead);
		}
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {
		ArrayList<TNode> bestPathNodes = new ArrayList<>();

		TNode trainHead = trainZero;
		bestPathNodes.add(trainHead);

		while(trainHead.getNext() != null && trainHead.getNext().getLocation() <= destination){
			trainHead = trainHead.getNext();
			bestPathNodes.add(trainHead);
		}

		TNode busHead = trainHead.getDown();
		bestPathNodes.add(busHead);

		while(busHead.getNext() != null && busHead.getNext().getLocation() <= destination){
			busHead = busHead.getNext();
			bestPathNodes.add(busHead);
		}

		TNode walkHead = busHead.getDown();
		bestPathNodes.add(walkHead);

		while(walkHead.getNext() != null && walkHead.getNext().getLocation() <= destination){
			walkHead = walkHead.getNext();
			bestPathNodes.add(walkHead);
		}
			
	    return bestPathNodes;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {
		//oNode = Original List Node & dNode = Duplicate List Node

		TNode oTrainZero = trainZero;
		TNode oTrainHead = oTrainZero;
		TNode dTrainZero = new TNode();
		TNode dTrainHead = dTrainZero;

		while(oTrainHead.getNext() != null){
			oTrainHead = oTrainHead.getNext();
			addToEnd(oTrainHead.getLocation(), dTrainHead);
		}

		TNode oBusZero = trainZero.getDown();
		TNode oBusHead = oBusZero;
		TNode dBusZero = new TNode();
		TNode dBusHead = dBusZero;
		dTrainHead.setDown(dBusHead);

		while(oBusHead.getNext() != null){
			oBusHead = oBusHead.getNext();
			TNode prev = null;
		 	TNode current = dBusHead;

			while(current != null){
				prev = current;
				current = current.getNext();
			}

			TNode newBusStop = new TNode(oBusHead.getLocation());
			newBusStop.setNext(current);
			prev.setNext(newBusStop);
			
			TNode dAnotherTrainStop = dTrainHead;
			while(dAnotherTrainStop != null && dAnotherTrainStop.getLocation() != newBusStop.getLocation()){
				dAnotherTrainStop = dAnotherTrainStop.getNext();
			}

			if((dAnotherTrainStop != null) && (dAnotherTrainStop.getLocation() == newBusStop.getLocation())){
				dAnotherTrainStop.setDown(newBusStop);
			}
		}

			TNode oWalkZero = oBusZero.getDown();
			TNode oWalkHead = oWalkZero;
			TNode dWalkZero = new TNode();
			TNode dWalkHead = dWalkZero;
			dBusHead.setDown(dWalkHead);

			while(oWalkHead.getNext() != null){
				oWalkHead = oWalkHead.getNext();
				TNode prev = null;
		 		TNode current = dWalkHead;

				while(current != null){
					prev = current;
					current = current.getNext();
				}

				TNode walkStop = new TNode(oWalkHead.getLocation());
				walkStop.setNext(current);
				prev.setNext(walkStop);

				TNode dAnotherBusStop = dBusHead;
				while(dAnotherBusStop != null && dAnotherBusStop.getLocation() != walkStop.getLocation()){
					dAnotherBusStop = dAnotherBusStop.getNext();
				}
				if(dAnotherBusStop != null){
					dAnotherBusStop.setDown(walkStop);
				}
			}
	    return dTrainZero;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {
		
	    // Creating a busZero stop, which is a slower mode of transportation than trainZero at 0.
		TNode busZero = trainZero.getDown();
		TNode busHead = busZero;
		TNode walkZero = busZero.getDown();
		TNode walkHead = walkZero;

		TNode scooterZero = new TNode();
		TNode scooterHead = scooterZero;
		scooterHead.setDown(walkHead);
		busHead.setDown(scooterHead);

		for (int i = 0; i < scooterStops.length; i++) {
			TNode prev = null;
		 	TNode current = scooterHead;

			while(current != null){
				prev = current;
				current = current.getNext();
			}

			TNode scooterStop = new TNode(scooterStops[i]);
			scooterStop.setNext(current);
			prev.setNext(scooterStop);

			TNode anotherWalkHead = walkHead;
			while(anotherWalkHead.getLocation() != scooterStop.getLocation()){
				anotherWalkHead = anotherWalkHead.getNext();
			}
			scooterStop.setDown(anotherWalkHead);

			TNode anotherBusHead = busHead;
			try {
				while(anotherBusHead.getLocation() != scooterStop.getLocation()){
					anotherBusHead = anotherBusHead.getNext();
				}
				anotherBusHead.setDown(scooterStop);
			} catch (Exception e) {
				//TODO: handle exception
			}
		}
	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}