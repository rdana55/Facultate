namespace AgentiiZboruriC.domain;
using System;

[Serializable]
public class Entity<ID>
{
    internal ID Id;

    public Entity(ID id)
    {
        this.Id = id;
    }

    public void SetId(ID id)
    {
        this.Id = id;
    }

    public ID GetId()
    {
        return Id;
    }
}
