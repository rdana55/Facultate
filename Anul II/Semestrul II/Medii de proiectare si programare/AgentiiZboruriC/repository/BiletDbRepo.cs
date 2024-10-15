using System.Data.SQLite;
using AgentiiZboruriC.domain;
using log4net.Repository.Hierarchy;

namespace AgentiiZboruriC.repository;

using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.SqlClient;
using log4net;

public class BiletDbRepo : Repository<int, Bilet>
    {
        private readonly DbUtils dbUtils;
        private readonly PersoanaDbRepo persoanaDbRepo;
        private readonly ZborDbRepo zborDbRepo;
        private readonly AngajatDbRepo angajatDbRepo;

        public BiletDbRepo(DbUtils dbUtils, PersoanaDbRepo persoanaDbRepo, ZborDbRepo zborDbRepo, AngajatDbRepo angajatDbRepo)
        {
            this.dbUtils = dbUtils;
            this.persoanaDbRepo = persoanaDbRepo;
            this.zborDbRepo = zborDbRepo;
            this.angajatDbRepo = angajatDbRepo;
        }

        public Bilet? FindOne(object o)
        {
            /*
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT * FROM Bilet WHERE id=@id", con))
            {
                cmd.Parameters.AddWithValue("@id", (int)o);
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    if (result.Read())
                    {
                        int id = result.GetInt32(0);
                        int idAngajat = result.GetInt32(1);
                        int idZbor = result.GetInt32(2);
                        int idPersoana = result.GetInt32(3);
                        Angajat angajat = angajatDbRepo.FindOne(idAngajat);
                        Zbor zbor = zborDbRepo.FindOne(idZbor);
                        Persoana persoana = persoanaDbRepo.FindOne(idPersoana);
                        return new Bilet(id, angajat, zbor, persoana);
                    }
                }
            }
            */
            return null;
        }

        public Zbor FindOne(int id)
        {
            throw new NotImplementedException();
        }

        public IEnumerable<Bilet> FindAll()
        {
            /*
            List<Bilet> bilete = new List<Bilet>();
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT * FROM Bilet", con))
            {
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    while (result.Read())
                    {
                        int id = result.GetInt32(0);
                        int idAngajat = result.GetInt32(1);
                        int idZbor = result.GetInt32(2);
                        int idPersoana = result.GetInt32(3);
                        Angajat angajat = angajatDbRepo.FindOne(idAngajat);
                        Zbor zbor = zborDbRepo.FindOne(idZbor);
                        Persoana persoana = persoanaDbRepo.FindOne(idPersoana);
                        bilete.Add(new Bilet(id, angajat, zbor, persoana));
                    }
                }
            }
            return bilete;
            */
            return null;
        }

        Optional<Bilet> Repository<int, Bilet>.Save(Bilet entity)
        {
            throw new NotImplementedException();
        }

        public Optional<Bilet> Delete(int id)
        {
            throw new NotImplementedException();
        }

        Optional<Bilet> Repository<int, Bilet>.Update(Bilet entity)
        {
            throw new NotImplementedException();
        }

        public Bilet? Save(Bilet entity)
        {
            return null;
        }

        public Bilet? SaveA(int idA, int idZ, int idC)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            {
                Random random = new Random();
                SQLiteCommand cmdTurist = new SQLiteCommand("SELECT idP FROM Turist WHERE nrClient = @idC", con);
                cmdTurist.Parameters.AddWithValue("@idC", idC);
                using (SQLiteDataReader resultSet = cmdTurist.ExecuteReader())
                {
                    while (resultSet.Read())
                    {
                        int idP = resultSet.GetInt32(0);
                        SQLiteCommand cmdBilet = new SQLiteCommand("INSERT INTO Bilet (id, idAngajat, idZbor, idPersoana) VALUES (@id, @idA, @idZ, @idP)", con);
                        cmdBilet.Parameters.AddWithValue("@id", random.Next(int.MaxValue));
                        cmdBilet.Parameters.AddWithValue("@idA", idA);
                        cmdBilet.Parameters.AddWithValue("@idZ", idZ);
                        cmdBilet.Parameters.AddWithValue("@idP", idP);
                        cmdBilet.ExecuteNonQuery();
                    }
                }

                if (idC != null)
                {
                    SQLiteCommand cmdBilet = new SQLiteCommand("INSERT INTO Bilet (id, idAngajat, idZbor, idPersoana) VALUES (@id, @idA, @idZ, @idC)", con);
                    cmdBilet.Parameters.AddWithValue("@id", random.Next(int.MaxValue));
                    cmdBilet.Parameters.AddWithValue("@idA", idA);
                    cmdBilet.Parameters.AddWithValue("@idZ", idZ);
                    cmdBilet.Parameters.AddWithValue("@idC", idC);
                    cmdBilet.ExecuteNonQuery();
                }
            }
            return null;
        }

        public Bilet? Delete(object o)
        {
            return null;
        }

        public Bilet? Update(Bilet entity)
        {
            return null;
        }

        public IEnumerable<Bilet> ChangeEntities(Dictionary<int, Bilet> entities)
        {
            return null;
        }

        public long Count()
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT COUNT(*) AS [count] FROM Bilet", con))
            {
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    if (result.Read())
                    {
                        return result.GetInt64(0);
                    }
                }
            }
            return 0;
        }

        public int GetTicketsSoldForFlight(int idZ)
        {
            int count = 0;
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT COUNT(*) FROM Bilet WHERE idZbor = @idZ", con))
            {
                cmd.Parameters.AddWithValue("@idZ", idZ);
                using (SQLiteDataReader resultSet = cmd.ExecuteReader())
                {
                    if (resultSet.Read())
                    {
                        count = resultSet.GetInt32(0);
                    }
                }
            }
            return count;
        }
    }