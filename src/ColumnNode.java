
public class ColumnNode {
    private String categoryName;
    private ColumnNode down;
    private ItemNode right;

    public ColumnNode(String dataToAdd) {
        categoryName = dataToAdd;
        down = null;
        right = null;
    }

    public String getCategoryName () {
        return categoryName;
    }

    public void setCategoryName (String data) {
        this.categoryName = data;
    }

    public ColumnNode getDown () {
        return down;
    }

    public void setDown (ColumnNode down) {
        this.down = down;
    }

    public ItemNode getRight() {
        return right;
    }

    public void setRight (ItemNode right) {
        this.right = right;
    }
}
