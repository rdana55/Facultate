using System.IO;
using System.Windows.Data;
using System.Windows.Markup;
using AgentiiZboruriC.domain;
using AgentiiZboruriC.service;

namespace AgentiiZboruriC.controller;
using System.IO;
using System.Windows;
using System.Windows.Markup;
using AgentiiZboruriC.controller;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows;
using System.Windows.Controls;

public class AngajatController
{
    private AngajatService service;
    private BiletService biletService;
    private ZborService zborService;
    private Angajat angajat;
    private Window dialogStage;

    private DataGrid tabelZboruri;
    private DataGridTextColumn columnPlecare;
    private DataGridTextColumn columnSosire;
    private DataGridTextColumn columnLocuri;
    private DataGridTextColumn columnOcupate;
    private ComboBox comboFrom;
    private ComboBox comboTo;
    private DatePicker dataFlight;
    private TextBox client;
    private Button cumpara;
    private Button logoutButton;
    private Button btn_Search;

    public void SetAngajat(Angajat angajat)
    {
        this.angajat = angajat;
    }

    public void SetService(AngajatService service, BiletService biletService, ZborService zborService, Window stage)
    {
        this.service = service;
        this.biletService = biletService;
        this.zborService = zborService;
        this.dialogStage = stage;

        logoutButton.Click += (sender, e) => HandleLogout(sender,e);
        cumpara.Click += (sender, e) => HandleCumpara(sender,e);
        btn_Search.Click += (sender, e) => HandleSearch(sender,e);
        InitModel();
    }

    public void Initialize()
    {
        columnPlecare.Binding = new Binding("From");
        columnSosire.Binding = new Binding("To");
        columnLocuri.Binding = new Binding("DataOra");
        columnOcupate.Binding = new Binding("LocuriDisponibile");
    }

    private void InitModel()
    {
        List<Zbor> flightList = zborService.GetFlightList()
            .Select(n => new Zbor(n.Id, n.From, n.To, n.DataOra, n.LocuriDisponibile))
            .ToList();

        tabelZboruri.ItemsSource = flightList;

        List<string> fromList = flightList.Select(Zbor => Zbor.From).ToList();
        List<string> toList = flightList.Select(Zbor => Zbor.To).ToList();

        List<string> uniqueFromList = fromList.Distinct().ToList();
        List<string> uniqueToList = toList.Distinct().ToList();

        comboFrom.ItemsSource = uniqueFromList;
        comboTo.ItemsSource = uniqueToList;
    }

    public void HandleSearch(object sender, RoutedEventArgs e)
    {
        DateTime data = dataFlight.SelectedDate.Value.Date;
        string plecare = comboFrom.SelectedItem.ToString();
        string sosire = comboTo.SelectedItem.ToString();

        List<Zbor> filteredFlightList = zborService.GetFlightList()
            .Where(n => n.From == plecare && n.To == sosire && n.DataOra.Date == data)
            .Select(n => new Zbor(n.Id, n.From, n.To, n.DataOra, n.LocuriDisponibile))
            .ToList();

        tabelZboruri.ItemsSource = filteredFlightList;
    }

    private void ClearFields()
    {
        client.Text = "";
    }

    public void HandleCumpara(object sender, RoutedEventArgs e)
    {
        string clientId = client.Text;
        Zbor flight = (Zbor)tabelZboruri.SelectedItem;
        biletService.SaveTicket(angajat.Id, flight.Id, int.Parse(clientId));
        ClearFields();
        MessageAlert.ShowMessage(null, MessageBoxImage.Information, "Cumparat!", "Biletul a fost achizitionat.");
    }

    public void HandleLogout(object sender, RoutedEventArgs e)
    {
        try
        {
            var root = (Window)Application.LoadComponent(new Uri("/MainWindow.xaml", UriKind.Relative));

            Window loginStage = new Window();
            loginStage.Title = "Login";
            loginStage.Content = root;

            LoginController loginController = (LoginController)root.DataContext;
            loginController.SetService(service, biletService, zborService, dialogStage);

            dialogStage.Close();
            loginStage.Show();
        }
        catch (IOException x)
        {
            Console.WriteLine(x);
        }
    }
}