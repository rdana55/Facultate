using System;
using System.Configuration;
using System.Data.SQLite;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Markup;
using AgentiiZboruriC.controller;
using AgentiiZboruriC.domain;
using AgentiiZboruriC.repository;
using AgentiiZboruriC.service;

namespace AgentiiZboruriC
{
    public class MainWindow : Window
    {
        private AngajatService angajatService;
        private BiletService biletService;
        private ZborService zborService;

        public MainWindow(AngajatService angajatService, BiletService biletService, ZborService zborService)
        {
            this.angajatService = angajatService;
            this.biletService = biletService;
            this.zborService = zborService;
           
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            // Load the XAML file
            try
            {
                using (var stream = new FileStream("/hello-view.xaml", FileMode.Open))
                {
                    this.Content = XamlReader.Load(stream);
                }

                LoginController controllerLogin = (LoginController)this.FindResource("controllerLogin");
                controllerLogin.SetService(angajatService, biletService, zborService, this);
            }
            catch (IOException e)
            {
                Console.WriteLine(e);
            }
        }
    }
}
