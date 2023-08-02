Team Members: 
David Xie dxie3@u.rochester.edu 
Vu Duc dvu2@u.rochester.edu

Files included in the submission: 
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


Runtime: 

IN THEORY, parsing the input data and constructing the graph should take O(n), where n is the number of lines of input. 

Dijikstra's algorithm with a priority queue implementation takes O((E + V)log V), where E is the num of edges, 
and V is the num of vertices. 

So I would imagine the runtime would be O(n + (E + V)log V). 



Obstacles: 

A large obstacle that we faced was early on in our development, which was figuring out how to have the coordinates scale 
with the size of our window. When we first ran our test cases, the coordinates were relatively close together, which did not 
display the larger cases correctly. We solved this by calculating a scaling factor for the map by taking the difference 
between the max and min latitude and longitude values, and made sure that the entire graph fit in to the Frame. Another 
challenge that we faced was the runtime of our project. Our first implementation of dijikstra's didn't use a priority queue,
rather, we just used a normal list, which meant that our dijikstra's algorithm would have to iterate through the entire 
list in order to find the minimum distance, which was definitely a CSC 171 move. For the larger test cases (nys.txt, monroe.txt), 
it took multiple minutes to output the map. 



Workload Distribution: 

(Note: The distribution is not a strict one. We heavily collaborated on all parts of the project, just some parts were 
originally designated to one person)

David worked on: 
- Graph Implementation (nodes, edges etc.) 
- Writing the README

Duc worked on: 
- Dijikstra's Implementation 
- Graphics Wizardry

Both of us worked on: 
- Fixing Bugs
- Commenting


We hope you have good luck in your finals! Thank you for TA'ing for us this semester! 

- David & Duc 