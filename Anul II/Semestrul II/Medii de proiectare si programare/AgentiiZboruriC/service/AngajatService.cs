using AgentiiZboruriC.domain;
using AgentiiZboruriC.repository;

namespace AgentiiZboruriC.service;

public class AngajatService
{
    private static AngajatDbRepo angajatDbRepo;

    public AngajatService(AngajatDbRepo angajatDbRepo)
    {
        AngajatService.angajatDbRepo = angajatDbRepo;
    }

    public IEnumerable<Angajat> FindAll()
    {
        return angajatDbRepo.FindAll();
    }

    public bool Login(string username, string password)
    {
        return angajatDbRepo.Login(username, password);
    }

    public Angajat? FindOneU(string username)
    {
        return angajatDbRepo.FindOneU(username);
    }
}