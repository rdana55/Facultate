using Lab13;
using Lab13.Domain;
using Lab13.Repository;

public class MyClass
{
    static void Main()
    {
        Service service = new Service()
        {
            repoAchizitie = new AchizitieRepo("..\\..\\..\\data\\achizitii.txt"),
            repoDocument = new DocumentRepo("..\\..\\..\\data\\documente.txt"),
            repoFactura = new FacturaRepo("..\\..\\..\\data\\facturi.txt")
        };
 
        // service.findAllDocumente().ForEach(n=>Console.WriteLine(n));
        // Console.WriteLine();
        // service.findAllAchizitii().ForEach(n=>Console.WriteLine(n));
        // Console.WriteLine();
        // service.findAllFacturi().ForEach(n=>Console.WriteLine(n));
        Console.WriteLine("TASK 1 ==============================================");
        service.task1().ForEach(n=>Console.WriteLine(n.getDTO()));
        Console.WriteLine("TASK 2 ==============================================");
        service.task2().ForEach(n=>Console.WriteLine(n.getDTO()));
        Console.WriteLine("TASK 3 ==============================================");
        service.task3().ForEach(n=>Console.WriteLine(n));
        Console.WriteLine("TASK 4 ==============================================");
        service.task4().ForEach(n=>Console.WriteLine(n));
        Console.WriteLine("TASK 5 ==============================================");
        Console.WriteLine(service.task5());
    }
}