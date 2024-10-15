using AgentiiZboruriC.domain;

namespace AgentiiZboruriC.repository;
using System;
using System.Collections.Generic;

public interface Repository<ID, E> where E : Entity<ID>
{
    Zbor FindOne(ID id);

    IEnumerable<E> FindAll();

    Optional<E> Save(E entity);

    Optional<E> Delete(ID id);

    Optional<E> Update(E entity);

    IEnumerable<E> ChangeEntities(Dictionary<ID, E> entities);
}

public class Optional<T>
{
    private T value;
    private bool hasValue;

    public Optional(T value)
    {
        this.value = value;
        this.hasValue = true;
    }

    public Optional()
    {
        this.hasValue = false;
    }

    public T GetValueOrDefault()
    {
        return hasValue ? value : default(T);
    }

    public bool HasValue()
    {
        return hasValue;
    }
}
