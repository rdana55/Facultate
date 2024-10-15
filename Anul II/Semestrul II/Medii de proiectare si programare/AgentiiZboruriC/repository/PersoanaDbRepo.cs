using System.Collections;
using System.Data.SQLite;
using AgentiiZboruriC.domain;
namespace AgentiiZboruriC.repository;
using System.Diagnostics;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using log4net;

public class PersoanaDbRepo : Repository<int, Persoana>
    {
        private readonly DbUtils dbUtils;

        public PersoanaDbRepo(DbUtils dbUtils)
        {
            this.dbUtils = dbUtils;
        }

        public Persoana? FindOne(object o)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT * FROM Persoana WHERE id=@id", con))
            {
                cmd.Parameters.AddWithValue("@id", (int)o);
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    if (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string nume = result.GetString(1);
                        string prenume = result.GetString(2);
                        return new Persoana(id, nume, prenume);
                    }
                }
            }
            return null;
        }

        public Zbor FindOne(int id)
        {
            throw new NotImplementedException();
        }

        public IEnumerable<Persoana> FindAll()
        {
            List<Persoana> persoane = new List<Persoana>();
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT * FROM Persoana", con))
            {
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    while (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string nume = result.GetString(1);
                        string prenume = result.GetString(2);
                        persoane.Add(new Persoana(id, nume, prenume));
                    }
                }
            }
            return persoane;
        }

        Optional<Persoana> Repository<int, Persoana>.Save(Persoana entity)
        {
            throw new NotImplementedException();
        }

        public Optional<Persoana> Delete(int id)
        {
            throw new NotImplementedException();
        }

        Optional<Persoana> Repository<int, Persoana>.Update(Persoana entity)
        {
            throw new NotImplementedException();
        }

        public Persoana? Save(Persoana entity)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("INSERT INTO Persoana (id, nume, prenume) VALUES (@id, @nume, @prenume)", con))
            {
                cmd.Parameters.AddWithValue("@id", entity.Id);
                cmd.Parameters.AddWithValue("@nume", entity.Nume);
                cmd.Parameters.AddWithValue("@prenume", entity.Prenume);
                cmd.ExecuteNonQuery();
            }
            return null;
        }

        public Persoana? Delete(object o)
        {
            return null;
        }

        public Persoana? Update(Persoana entity)
        {
            return null;
        }

        public IEnumerable<Persoana> ChangeEntities(Dictionary<int, Persoana> entities)
        {
            return null;
        }
    }