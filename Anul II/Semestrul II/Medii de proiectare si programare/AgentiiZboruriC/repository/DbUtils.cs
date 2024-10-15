using System;
using System.Data.SqlClient;
using System.Data.SQLite;
using System.IO;
using System.Reflection;
using log4net;

namespace AgentiiZboruriC.repository
{
    public class DbUtils
    {
        private readonly SQLiteConnectionStringBuilder sqlConnectionStringBuilder;
        private static readonly ILog logger = LogManager.GetLogger("DbUtils");

        public DbUtils(string connectionString)
        {
            sqlConnectionStringBuilder = new SQLiteConnectionStringBuilder(connectionString);
        }

        private SQLiteConnection instance = null;

        private SQLiteConnection GetNewConnection()
        {
            logger.Info("Getting new connection");
            string connectionString = sqlConnectionStringBuilder.ConnectionString;
            SQLiteConnection connection = null;
            try
            {
                connection = new SQLiteConnection(connectionString);
                connection.Open();
            }
            catch (Exception ex)
            {
                logger.Error("Error getting connection to the database", ex);
                Console.WriteLine("Error getting connection to the database: " + ex.Message);
            }
            return connection;
        }

        public SQLiteConnection GetConnection()
        {
            logger.Info("Getting connection");
            try
            {
                if (instance == null || instance.State == System.Data.ConnectionState.Closed)
                {
                    instance = GetNewConnection();
                }
            }
            catch (Exception ex)
            {
                logger.Error("Error getting connection", ex);
                Console.WriteLine("Error getting connection: " + ex.Message);
            }
            return instance;
        }
    }
}