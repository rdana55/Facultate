using System.Data.SQLite;
using AgentiiZboruriC.domain;
using log4net.Repository.Hierarchy;

namespace AgentiiZboruriC.repository;

using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using log4net;

public class ClientDbRepo : Repository<int, Client>
    {
        private readonly DbUtils dbUtils;
        private readonly PersoanaDbRepo persoanaDbRepo;

        public ClientDbRepo(DbUtils dbUtils, PersoanaDbRepo persoanaDbRepo)
        {
            this.dbUtils = dbUtils;
            this.persoanaDbRepo = persoanaDbRepo;
        }

        public Client? FindOne(object o)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT * FROM Client c JOIN Persoana p ON c.id = p.id WHERE c.id=@id", con))
            {
                cmd.Parameters.AddWithValue("@id", (int)o);
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    if (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string adresa = result.GetString(1);
                        string nume = result.GetString(2);
                        string prenume = result.GetString(3);
                        return new Client(id, nume, prenume, adresa);
                    }
                }
            }
            return null;
        }

        public Zbor FindOne(int id)
        {
            throw new NotImplementedException();
        }

        public IEnumerable<Client> FindAll()
        {
            List<Client> clients = new List<Client>();
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT * FROM Client c JOIN Persoana p ON c.id = p.id", con))
            {
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    while (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string adresa = result.GetString(1);
                        string nume = result.GetString(2);
                        string prenume = result.GetString(3);
                        clients.Add(new Client(id, nume, prenume, adresa));
                    }
                }
            }
            return clients;
        }

        Optional<Client> Repository<int, Client>.Save(Client entity)
        {
            throw new NotImplementedException();
        }

        public Optional<Client> Delete(int id)
        {
            throw new NotImplementedException();
        }

        Optional<Client> Repository<int, Client>.Update(Client entity)
        {
            throw new NotImplementedException();
        }

        public Client? Save(Client entity)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("INSERT INTO Client (id, adresa) VALUES (@id, @adresa)", con))
            {
                cmd.Parameters.AddWithValue("@id", entity.Id);
                cmd.Parameters.AddWithValue("@adresa", entity.Adresa);
                cmd.ExecuteNonQuery();
            }
            return null;
        }

        public Client? Delete(object o)
        {
            return null;
        }

        public Client? Update(Client entity)
        {
            return null;
        }

        public IEnumerable<Client> ChangeEntities(Dictionary<int, Client> entities)
        {
            return null;
        }
    }