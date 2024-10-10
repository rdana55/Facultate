using Lab13.Domain;

namespace Lab13.Repository;

public interface IRepo<ID, E> where E:  Entity<ID>
{
    IEnumerable<E> FindAll();
}