using Lab13.Domain;

namespace Lab13.Repository;

public delegate E CreateEntity<E>(string line);

abstract class InFileRepository<ID, E> : IRepo<ID, E> where E : Entity<ID>
{
    protected string fileName;
    protected CreateEntity<E> createEntity;
    protected IDictionary<ID, E> entities = new Dictionary<ID, E>();


    public InFileRepository(String fileName, CreateEntity<E> createEntity)
    {
        this.fileName = fileName;
        this.createEntity = createEntity;
        if (createEntity != null)
            loadFromFile();
    }

    protected virtual void loadFromFile()
    {
        List<E> list = DataReader.ReadData(fileName, createEntity);
        list.ForEach(x => entities[x.id] = x);
    }
    
    public IEnumerable<E> FindAll()
    {
        return entities.Values.ToList<E>();
    }


}