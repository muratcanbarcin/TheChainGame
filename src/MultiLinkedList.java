import enigma.console.Console;

public class MultiLinkedList {
    ColumnNode head;

    public void addCategory(String dataToAdd) {
        ColumnNode temp;
        if (head == null) {
            temp = new ColumnNode(dataToAdd);
            head = temp;
        }
        else {
            temp = head;
            while (temp.getDown() != null)
                temp = temp.getDown();
            ColumnNode newnode = new ColumnNode(dataToAdd);
            temp.setDown(newnode);
        }
    }

    public void addItem(String Category, String Item) {
        if (head == null)
            System.out.println("Add a Category before Item");
        else {
            ColumnNode temp = head;
            while (temp != null)
            {
                if (Category.equals(temp.getCategoryName())) {
                    ItemNode temp2 = temp.getRight();
                    if (temp2 == null) {
                        temp2 = new ItemNode(Item);
                        temp.setRight(temp2);
                    }
                    else {
                        while (temp2.getNext() != null)
                            temp2 = temp2.getNext();
                        ItemNode newnode = new ItemNode(Item);
                        temp2.setNext(newnode);
                    }
                }
                temp = temp.getDown();
            }
        }
    }

    public int sizeCategory()
    {
        int count = 0;
        if (head == null)
            System.out.println("linked list is empty");
        else {
            ColumnNode temp = head;
            while (temp != null)
            {
                count++;
                temp=temp.getDown();
            }
        }
        return count;
    }

    public int sizeItem()
    {
        int count = 0;
        if (head == null)
            System.out.println("linked list is empty");
        else {
            ColumnNode temp = head;
            while (temp != null)
            {
                ItemNode temp2 = temp.getRight();
                while (temp2 != null)
                {
                    count++;
                    temp2 = temp2.getNext();
                }
                temp = temp.getDown();
            }
        }
        return count;
    }

    public void display(Console cn)
    {
        int count=0;
        if (head == null)
            System.out.println("linked list is empty");
        else {
            ColumnNode temp = head;
            while (temp != null)
            {
                cn.getTextWindow().setCursorPosition(35,5+count);
                //System.out.print(temp.getCategoryName() + " --> ");
                ItemNode temp2 = temp.getRight();
                while (temp2 != null)
                {
                    System.out.print(temp2.getItemName());
                    try {
                        if(!temp2.getNext().equals(null)) {
                            System.out.print("+");
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    temp2 = temp2.getNext();

                }
                temp = temp.getDown();
                //System.out.println();
                cn.getTextWindow().setCursorPosition(35,6+count);
                System.out.print("+");
                count+=2;

            }
        }
    }
}
