public class TableMLL {
    ColumnNode head;

    public void addColumn(String dataToAdd){
        ColumnNode temp;
        if(head == null){
            temp = new ColumnNode((dataToAdd));
            head = temp;
        }
        else {
            temp = head;
            while (temp.getDown() != null){
                temp = temp.getDown();
            }
            ColumnNode newNode = new ColumnNode(dataToAdd);
            temp.setDown(newNode);
        }
    }

    public void addItem(String Column,String Item){
        if(head==null)
            System.out.println("Add a Column before Item");
        else {
            ColumnNode temp = head;
            while (temp!= null){
                if(Column.equals(temp.getFirstItem())){
                    RowNode temp2 = temp.getRight();
                    if(temp2 == null){
                        temp2 = new RowNode(Item);
                        temp.setRight(temp2);
                    }
                    else {
                        while (temp2.getNext() !=null)
                            temp2 = temp2.getNext();

                        RowNode newNode = new RowNode(Item);
                        temp2.setNext(newNode);
                    }
                }
                temp = temp.getDown();
            }
        }
    }

    public int sizeColumn(){
        int count =0;
        if(head == null)
            System.out.println("linked list is empty");
        else {
            ColumnNode temp = head;
            while (temp != null){
                RowNode temp2 = temp.getRight();
                while (temp2!=null){
                    count++;
                    temp2 = temp2.getNext();
                }
                temp = temp.getDown();
            }
        }
        return count;
    }

    public void display(){
        if(head == null)
            System.out.println("linked list is empty");
        else {
            ColumnNode temp = head;
            while (temp != null) {
                System.out.print(temp.getFirstItem());
                RowNode temp2 = temp.getRight();
                while (temp2 != null) {
                    System.out.print(temp2.getItemName() + "+");
                    temp2 = temp2.getNext();
                }
                System.out.println();
                temp=temp.getDown();
                System.out.println("+");
            }
        }
    }

}
