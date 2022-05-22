# bigid-test-project

Main class executes a sample run which read sample from url: http://norvig.com/big.txt.  

**How does it work**  
The main method opens InputStream from input URL and reads the lines from it by one line at a time.  
Every 1000 lines it create a  matcher task for CachedThreadPoolExecutorService, that start to seacrh names from enum CheckedName in these 1000 lines.   
After reading all the lines instance of Aggregator class starts to work, aggregating results from every completed task to resultMap via executorCompletionService mechanism.    
Finnaly after aggregating all the tasks Aggregator sorts and prints completed result in method named printResultMap.
