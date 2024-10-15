using System.Windows;

namespace AgentiiZboruriC.controller;

public static class MessageAlert
{
    public static void ShowMessage(Window owner, MessageBoxImage type, string header, string text)
    {
        MessageBox.Show(owner, text, header, MessageBoxButton.OK, type);
    }

    public static void ShowErrorMessage(Window owner, string text)
    {
        MessageBox.Show(owner, text, "Mesaj eroare", MessageBoxButton.OK, MessageBoxImage.Error);
    }
}