
public class SingleLinkedList {

    private Node head;

    public void add(Object dataToAdd)
    {
        if (head == null)
        {
            Node newnode = new Node(dataToAdd);
            head = newnode;
        }
        else
        {
            Node temp = head;
            while (temp.getLink() != null)
                temp = temp.getLink();
            Node newnode = new Node(dataToAdd);
            temp.setLink(newnode);
        }
    }

    public void display()
    {
        if(head == null)
            System.out.println("linked list is empty");
        else {
            Node temp = head;
            while (temp != null)
            {
                System.out.print(temp.getData() + " ");
                temp = temp.getLink();
            }
        }
    }

    public boolean search(Object item)
    {
        boolean flag = false;

        Node temp = head;
        while (temp != null)
        {
            if (item == temp.getData())
                flag = true;
            temp = temp.getLink();
        }

        return flag;
    }

    public int size()
    {
        int count = 0;
        if (head == null)
            System.out.println("linked list is empty");
        else {
            Node temp = head;
            while (temp != null)
            {
                count++;
                temp = temp.getLink();
            }
        }
        return count;
    }

    public void delete(Object dataToDelete)
    {
        if(head == null)
            System.out.println("linked list is empty");
        else {
            while ((String)head.getData() == (String)dataToDelete)
                head = head.getLink();

            Node temp = head;
            Node previous = null;
            while(temp != null)
            {
                if((String)temp.getData() == (String)dataToDelete)
                {
                    previous.setLink(temp.getLink());
                    temp = previous;
                }

                previous = temp;
                temp = temp.getLink();
            }
        }
    }
}