public class PositionInText implements Comparable<PositionInText> {
    private int lineOffset;
    private int charOffset;

    public PositionInText(int lineOffset, int charOffset) {
        this.lineOffset = lineOffset;
        this.charOffset = charOffset;
    }

    public int getLineOffset() {
        return lineOffset;
    }

    public void setLineOffset(int lineOffset) {
        this.lineOffset = lineOffset;
    }

    public int getCharOffset() {
        return charOffset;
    }

    public void setCharOffset(int charOffset) {
        this.charOffset = charOffset;
    }

    @Override
    public int compareTo(PositionInText o) {
        int lineCompare = Integer.compare(this.lineOffset, o.lineOffset);
        if (lineCompare != 0){
            return lineCompare;
        }
        return Integer.compare(this.charOffset, o.charOffset);

    }
}
