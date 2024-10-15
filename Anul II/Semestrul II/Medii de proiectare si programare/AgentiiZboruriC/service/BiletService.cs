using AgentiiZboruriC.domain;
using AgentiiZboruriC.repository;

namespace AgentiiZboruriC.service;

public class BiletService
{
    private readonly BiletDbRepo biletDbRepo;

    public BiletService(BiletDbRepo biletDbRepo)
    {
        this.biletDbRepo = biletDbRepo;
    }

    public long GetTotalNumberOfTickets()
    {
        return biletDbRepo.Count();
    }

    public int GetTicketsSoldForFlight(int idZ)
    {
        return biletDbRepo.GetTicketsSoldForFlight(idZ);
    }

    public Bilet? SaveTicket(int idA, int idZ, int idC)
    {
        return biletDbRepo.SaveA(idA, idZ, idC);
    }
}