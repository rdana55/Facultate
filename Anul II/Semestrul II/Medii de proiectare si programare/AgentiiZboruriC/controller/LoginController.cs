using System.IO;
using System.Windows.Markup;
using AgentiiZboruriC.domain;
using AgentiiZboruriC.service;

namespace AgentiiZboruriC.controller;

using System;
using System.Windows;
using System.Windows.Controls;

public class LoginController
{
    private TextBox UnTextField;
    private PasswordBox PsTextField;
    private Button Login;
    private AngajatService service;
    private BiletService biletService;
    private ZborService zborService;
    private Window dialogStage;
    private Angajat angajat;

    public void Initialize(Window window)
    {
        // Căutați controalele în arborele vizual al ferestrei
        UnTextField = (TextBox)window.FindName("UnTextField");
        PsTextField = (PasswordBox)window.FindName("PsTextField");
        Login = (Button)window.FindName("Login");

        // Atribuiți handlerul de evenimente butonului Login
        Login.Click += HandleLogin;
    }

    public void SetService(AngajatService service, BiletService biletService, ZborService zborService, Window stage)
    {
        this.service = service;
        this.biletService = biletService;
        this.zborService = zborService;
        this.dialogStage = stage;
        
         
        Initialize(stage);

    }

    private void ClearFields()
    {
        UnTextField.Text = "";
        PsTextField.Password = "";
    }

    public void HandleLogin(object sender, RoutedEventArgs e)
    {
        string username = UnTextField.Text;
        string password = PsTextField.Password;

        if (service.Login(username, password))
        {
            Angajat user = service.FindOneU(username);
            if (user != null)
            {
                ShowPage(user);
                dialogStage.Close();
            }
            else
            {
                MessageAlert.ShowMessage(null, MessageBoxImage.Information, "Invalid", "Utilizatorul sau parola sunt incorecte.");
            }
        }
    }

    private void ShowPage(Angajat u)
    {
        try
        {
            // Load the XAML file
            using (var stream = new FileStream("/angajat.xaml", FileMode.Open))
            {
                var root = (Window)XamlReader.Load(stream);

                Window requestStage = new Window();
                requestStage.Title = u.ToString();
                requestStage.Content = root;

                AngajatController angajatController = (AngajatController)root.DataContext;
                angajatController.SetService(service, biletService, zborService, requestStage);
                angajatController.SetAngajat(u);

                requestStage.Show();
            }
        }
        catch (IOException e)
        {
            Console.WriteLine(e);
        }
    }
}