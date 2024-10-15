using System.Collections.Specialized;
using System.Data.SqlClient;

namespace Persistence
{
    public class DbUtils
    {
        private static readonly Logger Logger = LogManager.GetCurrentClassLogger();
        private readonly NameValueCollection _jdbcProps;
        private SqlConnection _instance;

        public DbUtils(NameValueCollection props)
        {
            _jdbcProps = props;
        }

        private SqlConnection GetNewConnection()
        {
            Logger.Trace("Entering GetNewConnection");
            var url = _jdbcProps["jdbc.url"];
            var user = _jdbcProps["jdbc.user"];
            var pass = _jdbcProps["jdbc.password"];
            SqlConnection con = null;
            try
            {
                con = string.IsNullOrEmpty(user) || string.IsNullOrEmpty(pass) 
                    ? new SqlConnection(url) 
                    : new SqlConnection(url, new SqlCredential(user, pass));
                con.Open();
            }
            catch (SqlException ex)
            {
                Logger.Error(ex, "Error getting connection to the database");
                System.Console.WriteLine($"Error getting connection to the database {ex}");
            }
            Logger.Trace("Exiting GetNewConnection");
            return con;
        }

        public SqlConnection GetConnection()
        {
            Logger.Trace("Entering GetConnection");
            try
            {
                if (_instance == null || _instance.State == System.Data.ConnectionState.Closed)
                    _instance = GetNewConnection();
            }
            catch (SqlException ex)
            {
                Logger.Error(ex, "Error DB");
                System.Console.WriteLine($"Error DB {ex}");
            }
            Logger.Trace("Exiting GetConnection");
            return _instance;
        }
    }
}