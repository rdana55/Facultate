using AgentiiZboruriC.domain;
using AgentiiZboruriC.repository;

namespace AgentiiZboruriC.service;

public class ZborService
{
    private ZborDbRepo zborDbRepo;

    public ZborService(ZborDbRepo zborDbRepo)
    {
        this.zborDbRepo = zborDbRepo;
    }

    public List<Zbor> GetFlightList()
    {
        return zborDbRepo.FindAll() as List<Zbor>;
    }
}