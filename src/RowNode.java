public class RowNode {
    private String ItemName;
    private RowNode next;

    public RowNode(String dataToAdd){
        ItemName = dataToAdd;
        next = null;
    }

    public String getItemName() {
        return ItemName;
    }

    public RowNode getNext() {
        return next;
    }

    public void setItemName(String data) {
        this.ItemName = data;
    }

    public void setNext(RowNode next) {
        this.next = next;
    }
}
