import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            ExecutorCompletionService<Map<CheckedName, List<PositionInText>>> executorCompletionService = new ExecutorCompletionService<>(executorService);
            long startTime = System.currentTimeMillis();

            URL url = new URL("http://norvig.com/big.txt");

            int numberOfTasks = 0;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String st;
                List<Line> linesForMatcher = new ArrayList<>();

                int counter = 1;

                while ((st = br.readLine()) != null) {
                    linesForMatcher.add(new Line(counter, st));
                    counter++;
                    if (linesForMatcher.size() == 1000){
                        executorCompletionService.submit(new Matcher(new ArrayList<>(linesForMatcher)));
                        numberOfTasks++;
                        linesForMatcher.clear();
                    }
                }
                if (!linesForMatcher.isEmpty()){
                    executorCompletionService.submit(new Matcher(new ArrayList<>(linesForMatcher)));
                    numberOfTasks++;
                }
            }


            Aggregator aggregator = new Aggregator(executorCompletionService, numberOfTasks);
            aggregator.aggregateResultsFromTasks();

            executorService.shutdown();
            long endTime = System.currentTimeMillis();
            LOG.debug("----------");
            LOG.debug("Execution time: " + (endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
            executorService.shutdownNow();
        }
    }

}
