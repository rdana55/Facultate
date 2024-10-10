using Lab13.Domain;
using Microsoft.VisualBasic;

namespace Lab13.Repository;

class AchizitieRepo: InFileRepository<string, Achizitie>
{
    public AchizitieRepo(string filename) : base(filename, EntityToFileMaping.CreateAchizitie)
    {
    }

    public Achizitie findOne(String id)
    {
        return FindAll().Where(o => o.id.Equals(id)).ToList()[0];
    }
}