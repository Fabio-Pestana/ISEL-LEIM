public class TP2_13_MergeSorted
{
    public static void main(String[] args)
    {
        int [] lista1 = new int [] {-1, 3, 5, 7 ,8, 10, 25};
        int [] lista2 = new int [] {1, 2, 4, 6, 9, 16,  23, 30};
        int l1= lista1.length;
        int l2= lista2.length;
        int lf= l1 + l2;
        int [] lista_final = new int [lf];
        boolean executar=true;
        for (int i = 0; i<l1; i++)
        {
            lista_final[i] = lista1[i];
        }
        for (int i = 0; i<l2; i++)
        {
            lista_final[l1+i] = lista2[i];
        }
        while (executar==true)
        {
            executar=false;
            for (int i=0; i<(lf-1); i++)
            {

                if (lista_final[i]>lista_final[i+1])
                {
                    int ord= lista_final[i+1];
                    lista_final[i+1]=lista_final[i];
                    lista_final[i]=ord;
                    executar=true;
                }
            }
        }
        System.out.println("Lista criada apartir de duas listas integradas no código: ");
        System.out.println(" ");
        System.out.print("[ ");
        for (int i = 0; i<lf; i++)
        {
            System.out.print(lista_final[i] + " ");
        }
        System.out.print("]");
    }
}
