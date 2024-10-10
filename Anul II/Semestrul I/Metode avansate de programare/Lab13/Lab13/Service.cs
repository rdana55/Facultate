using System.Runtime.CompilerServices;
using Lab13.Domain;
using Lab13.Repository;

namespace Lab13;

class Service
{
    public AchizitieRepo repoAchizitie { get; set; }
    public DocumentRepo repoDocument { get; set; }
    public FacturaRepo repoFactura { get; set; }

    public List<Achizitie> findAllAchizitii()
    {
        return this.repoAchizitie.FindAll().ToList();
    }
    public List<Document> findAllDocumente()
    {
        return this.repoDocument.FindAll().ToList();
    }
    public List<Factura> findAllFacturi()
    {
        return this.repoFactura.FindAll().ToList();
    }

    public List<Document> task1()
    {
        return repoDocument.getFrom2023();
    }

    public List<Factura> task2()
    {
        return repoFactura.getScadent();
    }
    
    public List<FacturaDTO> task3()
    {
        List<FacturaDTO> filtered = new List<FacturaDTO>();
        foreach (var fac in repoFactura.FindAll().ToList())
        {
            List<Achizitie> achizitii = repoAchizitie.FindAll().Where(o=>fac.Achizitii.Contains(o.id)).ToList();
            int total = 0;
            foreach (var ac in achizitii)
            {
                total += ac.Cantitate;
            }
            if(total>3)
                filtered.Add(new FacturaDTO(fac.Name,total));
        }

        return filtered;
    }

    public List<AchizitieDTO> task4()
    {
        List<AchizitieDTO> achizitieDtos = new List<AchizitieDTO>();
        foreach (var ac in repoAchizitie.FindAll().ToList())
        {
            Factura fac = repoFactura.findOne(ac.Factura);
            if (fac.Categorie == Categorii.Utilities)
            {
                AchizitieDTO temp = new AchizitieDTO(ac.Produs, fac.Name,fac.Categorie.ToString());
                achizitieDtos.Add(temp);
            }
        }
        return achizitieDtos;
    }

    public Categorii task5()
    {
        double countUtility = 0, countGroceries = 0, countFashion = 0, countEntertainment = 0, countElectronics = 0;
        foreach (var fac in repoFactura.FindAll().ToList())
        {
            double countTemp = 0;
            List<Achizitie> achizities = fac.Achizitii.Select(o=>repoAchizitie.findOne(o)).ToList();
            foreach (var ac in achizities)
            {
                countTemp += ac.Cantitate * ac.PretProdus;
            }

            if (fac.Categorie == Categorii.Electronics)
                countElectronics += countTemp;
            else if (fac.Categorie == Categorii.Utilities)
                countUtility += countTemp;
            else if (fac.Categorie == Categorii.Fashion)
                countFashion += countTemp;
            else if (fac.Categorie == Categorii.Entertainment)
                countEntertainment += countTemp;
            else if (fac.Categorie == Categorii.Groceries)
                countFashion += countTemp;
        }

        double max = Math.Max(countElectronics, Math.Max(countEntertainment, Math.Max(countGroceries, Math.Max(countFashion, Math.Max(countEntertainment,
            countUtility)))));

        if (countElectronics == max)
            return Categorii.Electronics;
        if (countEntertainment == max)
            return Categorii.Electronics;
        if (countFashion == max)
            return Categorii.Fashion;
        if (countGroceries == max)
            return Categorii.Groceries;
        
        return Categorii.Utilities;
    }
}