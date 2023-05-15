public class ColumnNode {
    private String FirstItem;
    private ColumnNode down;
    private RowNode right;

    public ColumnNode(String dataToAdd) {
        FirstItem = dataToAdd;
        down = null;
        right = null;
    }

    public String getFirstItem() {
        return FirstItem;
    }

    public ColumnNode getDown() {
        return down;
    }

    public RowNode getRight() {
        return right;
    }

    public void setFirstItem(String data) {
        this.FirstItem = data;
    }

    public void setDown(ColumnNode down) {
        this.down = down;
    }

    public void setRight(RowNode right) {
        this.right = right;
    }
}
