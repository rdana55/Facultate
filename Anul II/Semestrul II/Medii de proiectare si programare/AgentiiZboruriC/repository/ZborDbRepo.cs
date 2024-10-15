using System.Collections;
using System.Data.SQLite;
using AgentiiZboruriC.domain;
using log4net.Repository.Hierarchy;
using System;
using System.Collections.Generic;
namespace AgentiiZboruriC.repository;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using log4net;

public class ZborDbRepo : Repository<int, Zbor>
    {
        private readonly DbUtils dbUtils;

        public ZborDbRepo(DbUtils dbUtils)
        {
            this.dbUtils = dbUtils;
        }

        public Zbor? FindOne(object o)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT * FROM Zbor WHERE id=@id", con))
            {
                cmd.Parameters.AddWithValue("@id", (int)o);
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    if (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string from = result.GetString(1);
                        string to = result.GetString(2);
                        string dataOra = result.GetString(3);
                        DateTime dataOraDateTime = DateTime.Parse(dataOra);
                        int locuriDisponibile = result.GetInt32(4);
                        Zbor zbor = new Zbor(id, from, to, dataOraDateTime, locuriDisponibile);
                        return zbor;
                    }
                }
            }
            return null;
        }

        public Zbor FindOne(int id)
        {
            throw new NotImplementedException();
        }

        public IEnumerable<Zbor> FindAll()
        {
            List<Zbor> zboruri = new List<Zbor>();
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT * FROM Zbor", con))
            {
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    while (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string from = result.GetString(1);
                        string to = result.GetString(2);
                        string dataOra = result.GetString(3);
                        DateTime dataOraDateTime = DateTime.Parse(dataOra);
                        int locuriDisponibile = result.GetInt32(4);
                        Zbor zbor = new Zbor(id, from, to, dataOraDateTime, locuriDisponibile);
                        zboruri.Add(zbor);
                    }
                }
            }
            return zboruri;
        }

        Optional<Zbor> Repository<int, Zbor>.Save(Zbor entity)
        {
            throw new NotImplementedException();
        }

        public Optional<Zbor> Delete(int id)
        {
            throw new NotImplementedException();
        }

        Optional<Zbor> Repository<int, Zbor>.Update(Zbor entity)
        {
            throw new NotImplementedException();
        }

        public Zbor? Save(Zbor entity)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("INSERT INTO Zbor (id, from, to, dataOra, locuriDisponibile) VALUES (@id, @from, @to, @dataOra, @locuriDisponibile)", con))
            {
                cmd.Parameters.AddWithValue("@id", entity.Id);
                cmd.Parameters.AddWithValue("@from", entity.From);
                cmd.Parameters.AddWithValue("@to", entity.To);
                cmd.Parameters.AddWithValue("@dataOra", entity.DataOra.ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Parameters.AddWithValue("@locuriDisponibile", entity.LocuriDisponibile);
                cmd.ExecuteNonQuery();
            }
            return null;
        }

        public IEnumerable<Zbor> FindAllFiltered(string to, string dataOra)
        {
            List<Zbor> zboruri = new List<Zbor>();
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT * FROM Zbor WHERE to = @to AND dataOra = @dataOra", con))
            {
                cmd.Parameters.AddWithValue("@to", to);
                cmd.Parameters.AddWithValue("@dataOra", dataOra);
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    while (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string from = result.GetString(1);
                        string toResult = result.GetString(2);
                        string dataOraR = result.GetString(3);
                        DateTime dataOraDateTime = DateTime.Parse(dataOraR);
                        int locuriDisponibile = result.GetInt32(4);
                        Zbor zbor = new Zbor(id, from, toResult, dataOraDateTime, locuriDisponibile);
                        zboruri.Add(zbor);
                    }
                }
            }
            return zboruri;
        }

        public Zbor? Delete(object o)
        {
            return null;
        }

        public Zbor? Update(Zbor entity)
        {
            return null;
        }

        public IEnumerable<Zbor> ChangeEntities(Dictionary<int, Zbor> entities)
        {
            return null;
        }
    }
