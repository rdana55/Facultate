using Lab13.Domain;

namespace Lab13.Repository;

class DocumentRepo : InFileRepository<string, Document>
{
    public DocumentRepo(string filename): base(filename, EntityToFileMaping.CreateDocument)
    {}

    public List<Document> getFrom2023()
    {
        List<Document> all = FindAll().ToList();
        List<Document> filtered = new List<Document>();
        foreach (var doc in all)
        {
            if (doc.Date.Year == 2023)
            {
                filtered.Add(doc);
            }
        }
        return filtered;
    }
}