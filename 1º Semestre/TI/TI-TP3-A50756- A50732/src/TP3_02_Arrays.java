public class TP3_02_Arrays
{
    public static int max (int[]array)
    {
        int max=Integer.MIN_VALUE;
        for (int i=0;i< array.length;i++)
        {
            if(max<array[i])
            {
                max=array[i];
            }
        }
        return max;
    }
    public static int min (int[]array)
    {
        int min=Integer.MAX_VALUE;
        for (int i=0;i< array.length;i++)
        {
            if (min>array[i])
            {
                min=array[i];
            }
        }
        return min;
    }
    public static int sum (int[]array)
    {
        int soma_array=0;
        for (int i=0;i< array.length;i++)
        {
            soma_array=soma_array +array[i];
        }
        return soma_array;
    }
    public static double avg(int[]array)
    {
        double soma_lista=sum(array);
        double media = soma_lista/ array.length;
        return media;
    }

    public static void main(String[] args)
    {

        int[]lista={1,2,3,4,5,6,7,8,9,10, 321,-55,40};
        System.out.println("O numero Max da lista: " + max(lista));
        System.out.println("O numero min da lista: " + min(lista));
        System.out.println("A soma dos numeros da lista: " + sum(lista));
        System.out.println("A media da lista: " + avg(lista));
    }
}
