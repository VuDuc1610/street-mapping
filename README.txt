- StreetMap.java 
	- all classes are in this file 
- Test Case files 
	- ur.txt / monroe.txt / nys.txt
- README.txt 


To Run: 

javac StreetMap.java

java Street Map <file name> <command> <Location 1> <Location 2>  (Locations arguments only if command is "direction") 

Commands are "--show" and "--directions", as depicted in the assignment documentation. 


Code Philopsoophy: 

The design idea for this project was quite straightforward. The documentation basically gives us the tools that we need to 
use to solve the problem, but I'll write it again here. 

Given a document of coordinate points, we first take the data from this document and add it to a graph. Then, with that 
graph, we run dijikstra's algorithm to find the shortest path between 2 coordinate points. Then, using everyone's favorite 
java graphics, we create a graphical representation of the graph/map. 


Code Functionality: 

- Node
	- Represent locations on the map
	- Keeps track of:
		- ID (name)
		- Coordinates (Latitude / Longitude) 
		- Visted (Boolean) 
		- Previous Node
		- Distance (from prev node to current) 
		- The closest unvisited node. 
- Edge 
	- Represents the roads in-betwixt our nodes
	- Keeps track of:
		- destination node of the edge
		- weight 
		- roadID (name)
	Methods: 
	- toString()
		- returns the id of the destination node

- EdgeComparator 
	- A helper method that compares the weight of 2 edges 
	- This is used in the priority queue for dijikstra's later on 

- Graphing 
	- Uses a hashmap that stores node IDs as keys 
	- An Arraylist that stores all nodes 
	- An arrayList that stores all the edges 
	
	
	Methods: 
	- printGraph()
		- prints all nodes in the graph
	- addEdge(source, edge) 
		- adds edge to adjacents queue with id "source" 
	- distance (a,b) 
		- calculates the distance between 2 nodes using Haversine formula 
	- itemize(s) 
		- parses the string from the input that adds either a node or an edge
	- FiletoGraph
		- reads the file and makes the graph
