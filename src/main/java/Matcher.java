import com.sun.source.tree.Tree;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class Matcher  implements Callable<Map<CheckedName, List<PositionInText>>> {

    private List<Line> inputLines;

    public Matcher(List<Line> inputStrings) {
        this.inputLines = inputStrings;
    }

    @Override
    public Map<CheckedName, List<PositionInText>> call() {

        Map<CheckedName, List<PositionInText>> ret = new EnumMap<>(CheckedName.class);
        for (Line line : inputLines){
            for (CheckedName name : CheckedName.values()){
                java.util.regex.Matcher m = Pattern.compile("(?=("+ name + "))").matcher(line.getLineText());
                while (m.find())
                {
                    List<PositionInText> positionList = ret.getOrDefault(name, new ArrayList<>());
                    positionList.add(new PositionInText(line.getLineNumber(), m.start()+1));
                    ret.put(name, positionList);
                }
            }
        }
        return ret;
    }
}
