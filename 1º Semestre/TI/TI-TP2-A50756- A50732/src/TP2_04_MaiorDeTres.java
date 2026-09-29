import java.util.Scanner;

public class TP2_04_MaiorDeTres
{
    public static void main(String[] args)
    {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Introduza três números: ");
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int nmaior=0, nmenor=0, nmedio=0;
        if(a>b)
        {
            if(a>c)
            {
                nmaior=a;
                if(b>c)
                {
                    nmedio=b;
                    nmenor=c;
                }else
                {
                    nmedio=c;
                    nmenor=b;
                }
            }else
            {
                nmaior=c;
                nmedio=a;
                nmenor=b;
            }
        } else
        {
            if(b>c)
            {
                nmaior=b;
                if(a>c)
                {
                    nmedio=a;
                    nmenor=c;
                }else
                {
                    nmedio=c;
                    nmenor=a;
                }
            }else
            {
                nmaior=c;
                nmedio=b;
                nmenor=a;
            }
        }
        System.out.println(" O número " + nmaior +" é o maior! ");
        System.out.println(" O número " + nmedio +" é o do meio! ");
        System.out.println(" O número " + nmenor +" é o menor! ");
    }
}
