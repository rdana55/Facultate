using System.Data.SQLite;
using System.Security.Cryptography;
using System.Text;
using AgentiiZboruriC.domain;
using log4net.Repository.Hierarchy;

namespace AgentiiZboruriC.repository;

using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.SqlClient;
using log4net;

public class AngajatDbRepo : Repository<int, Angajat>
    {
        private readonly DbUtils dbUtils;

        public AngajatDbRepo(DbUtils dbUtils)
        {
            this.dbUtils = dbUtils;
        }

        public Angajat? FindOne(object o)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT a.id, a.username, a.password, p.nume, p.prenume FROM Angajat a JOIN Persoana p ON a.id = p.id WHERE a.id=@id", con))
            {
                cmd.Parameters.AddWithValue("@id", (int)o);
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    if (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string username = result.GetString(1);
                        string password = result.GetString(2);
                        string nume = result.GetString(3);
                        string prenume = result.GetString(4);
                        return new Angajat(id, nume, prenume, username, password);
                    }
                }
            }
            return null;
        }

        public Angajat? FindOneU(string username)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT a.idP, a.username, a.password, p.nume, p.prenume FROM Angajat a JOIN Persoana p ON a.idP = p.id WHERE a.username=@username", con))
            {
                cmd.Parameters.AddWithValue("@username", username);
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    if (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string password = result.GetString(1);
                        string nume = result.GetString(2);
                        string prenume = result.GetString(3);
                        return new Angajat(id, nume, prenume, username, password);
                    }
                }
            }
            return null;
        }

        public Zbor FindOne(int id)
        {
            throw new NotImplementedException();
        }

        public IEnumerable<Angajat> FindAll()
        {
            List<Angajat> angajati = new List<Angajat>();
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT a.id, a.username, a.password, p.nume, p.prenume FROM Angajat a JOIN Persoana p ON a.id = p.id", con))
            {
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    while (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string username = result.GetString(1);
                        string password = result.GetString(2);
                        string nume = result.GetString(3);
                        string prenume = result.GetString(4);
                        angajati.Add(new Angajat(id, nume, prenume, username, password));
                    }
                }
            }
            return angajati;
        }

        public Optional<Angajat> Save(Angajat entity)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("INSERT INTO Angajat (id, username, password) VALUES (@id, @username, @password)", con))
            {
                cmd.Parameters.AddWithValue("@id", entity.Id);
                cmd.Parameters.AddWithValue("@username", entity.Username);
                cmd.Parameters.AddWithValue("@password", EncryptPassword(entity.Password));
                cmd.ExecuteNonQuery();
            }
            return null;
        }

        public Optional<Angajat> Delete(int id)
        {
            throw new NotImplementedException();
        }

        public bool Login(string username, string password)
        {
            using (SQLiteConnection con = dbUtils.GetConnection())
            using (SQLiteCommand cmd = new SQLiteCommand("SELECT password FROM Angajat WHERE username = @username", con))
            {
                cmd.Parameters.AddWithValue("@username", username);
                using (SQLiteDataReader result = cmd.ExecuteReader())
                {
                    if (result.Read())
                    {
                        string storedPassword = result.GetString(0);
                        return storedPassword.Equals(EncryptPassword(password));
                    }
                }
            }
            return false;
        }

        public string EncryptPassword(string password)
        {
            using (SHA256 sha256Hash = SHA256.Create())
            {
                byte[] bytes = sha256Hash.ComputeHash(Encoding.UTF8.GetBytes(password));
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < bytes.Length; i++)
                {
                    builder.Append(bytes[i].ToString("x2"));
                }
                return builder.ToString();
            }
        }

        public Angajat? Delete(object o)
        {
            return null;
        }

        public Optional<Angajat> Update(Angajat entity)
        {
            return null;
        }

        public IEnumerable<Angajat> ChangeEntities(Dictionary<int, Angajat> entities)
        {
            return null;
        }
    }