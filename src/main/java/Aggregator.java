import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;

public class Aggregator {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    private final ExecutorCompletionService<Map<CheckedName, List<PositionInText>>> executorCompletionService;
    private final int numberOfTasks;

    public Aggregator(ExecutorCompletionService<Map<CheckedName, List<PositionInText>>> executorCompletionService, int numberOfTasks) {
        this.executorCompletionService = executorCompletionService;
        this.numberOfTasks = numberOfTasks;
    }
    
    public void aggregateResultsFromTasks() throws ExecutionException, InterruptedException {

        Map<CheckedName, List<PositionInText>> resultMap = new HashMap<>();

            for (int i = 0; i<numberOfTasks; i++){
                    Map<CheckedName, List<PositionInText>> checkedNameListMap = executorCompletionService.take().get();
                    for (CheckedName name : checkedNameListMap.keySet()){
                        List<PositionInText> nameList = resultMap.getOrDefault(name, new ArrayList<>());
                        nameList.addAll(checkedNameListMap.get(name));
                        resultMap.put(name, nameList);
                    }
            }


        printResultMap(resultMap);
    }

    private void printResultMap(Map<CheckedName, List<PositionInText>> resultMap) {
        for (Map.Entry<CheckedName, List<PositionInText>> entry : resultMap.entrySet()){
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey());
            sb.append("-->[");
            Collections.sort(entry.getValue());
            for (int i= 0; i<entry.getValue().size(); i++){
                PositionInText positionInText = entry.getValue().get(i);
                sb.append("[");
                sb.append("lineOffset=").append(positionInText.getLineOffset());
                sb.append(", ");
                sb.append("charOffset=").append(positionInText.getCharOffset());
                sb.append("]");
                if (i != entry.getValue().size()-1){
                    sb.append(", ");
                }
            }
            sb.append("]");
            LOG.info(sb.toString());
        }
    }
}
