using System;
using System.Collections.Generic;
using System.IO;
using AgentiiZboruriC.repository;

namespace AgentiiZboruriC
{
    public partial class MainBD : Application
    {
        public static void Main(string[] args)
        {
            Application app = new Application();
            app.Run(new MainWindow());
        }

        public MainBD()
        {
            Properties props = new Properties();
            try
            {
                props.Load(new StreamReader("bd.config"));
            }
            catch (IOException e)
            {
                Console.WriteLine("Cannot find bd.config " + e);
            }

            DbUtils dbUtils = new DbUtils(props);

            PersoanaDbRepo persoanaDbRepo = new PersoanaDbRepo(dbUtils);
            AngajatDbRepo angajatDbRepo = new AngajatDbRepo(dbUtils);
            ClientDbRepo clientDbRepo = new ClientDbRepo(dbUtils, persoanaDbRepo);
            ZborDbRepo zborDbRepo = new ZborDbRepo(dbUtils);
            BiletDbRepo biletDbRepo = new BiletDbRepo(dbUtils, persoanaDbRepo, zborDbRepo, angajatDbRepo);

            BiletService biletService = new BiletService(biletDbRepo);
            ZborService zborService = new ZborService(zborDbRepo);
            AngajatService angajatService = new AngajatService(angajatDbRepo);

            MainWindow mainWindow = new MainWindow(angajatService, biletService, zborService);
            mainWindow.Show();
        }
    }
}