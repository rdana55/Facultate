# Servicii NSA

## 1. Ubuntu Server

Ubuntu Server este sistemul de bază pe care au fost instalate unele dintre celelalte servicii. După descărcarea ISO-ului oficial, s-a creat o mașină virtuală în VirtualBox. După instalare, au fost adăugate serviciile web folosind Apache și MySQL. Aplicația web frontend a fost construită cu `npm run build` și mutată în `/var/www/html/victima`, cu permisiuni pentru utilizatorul `www-data`. Backend-ul (API-ul) a fost adăugat în subfolderul `/api`.

Fluxul general:

1. Instalare Ubuntu Server
2. Instalare Apache + PHP + MySQL
3. Construirea aplicației frontend + mutarea în `victima`
4. Importul bazei de date cu `mysql`
5. Mutarea fișierelor backend în `/api`
6. Asigurarea permisiunilor și restartarea serviciului Apache

---

## 2. Windows 10 

Windows 10 a fost instalat ca mașină virtuală în VirtualBox folosind imaginea ISO oficială. S-a bifat opțiunea „Skip Unattended Installation”, deoarece nu s-a folosit un Product Key. După pornirea mașinii, s-au efectuat configurările inițiale necesare pentru a deveni client în rețea.

Utilizat ca și client pentru testarea partajărilor Samba

---

## 3. SSH

SSH este serviciul care permite acces de la distanță la serverul Ubuntu.

Fluxul configurării:

1. Instalare `openssh-server`
2. Verificare cu `systemctl status ssh`
3. Activare permanentă cu `enable`
4. Permiterea în firewall cu `ufw allow ssh`
5. Pe Windows, s-a folosit `pscp` (din pachetul Putty) pentru a trimite fișiere de pe PC în Ubuntu
6. Fișierele au fost mutate cu `mv` în `/var/www/html` pentru a deveni accesibile prin Apache

---

## 4. Proxy (Squid)

Squid este un proxy HTTP instalat pe Ubuntu pentru a redirecționa cererile HTTP din rețea.

Fluxul:

1. Instalare Squid cu `sudo apt install squid`
2. Configurarea fișierului `squid.conf` pentru a permite accesul (temporar, `http_access allow all`)
3. Restartarea serviciului
4. De pe Kali, s-a testat accesul la internet cu comanda `curl -x [ip_ubuntu]:3128 http://example.com`
5. Accesul s-a înregistrat în logurile `access.log`

---

## 5. Samba

Samba permite partajarea fișierelor între Ubuntu și Windows.

Fluxul:

1. Instalare `samba` și `smbclient`
2. Creare user local `sambatest` și parolă cu `smbpasswd`
3. Creare folder `/srv/samba/private` cu permisiuni exclusive pentru `sambatest`
4. Adăugarea unei secțiuni `[private]` în `smb.conf` cu autentificare obligatorie
5. Permiterea traficului în firewall (`ufw allow Samba`)
6. Pe Windows, s-a accesat folderul partajat prin `\ip\private`, introducând userul și parola create

Aceste configurări asigură un partaj controlat și securizat între cele două sisteme.

---

## 6. Apache

Apache este serverul web principal instalat pe Ubuntu Desktop pentru a rula aplicația web.

Fluxul:

1. Instalare Apache cu `sudo apt install apache2`
2. Verificarea funcționării cu `systemctl status apache2`
3. Creare folder `/var/www/victima` cu subfoldere `backend` și `frontend`
4. Setarea permisiunilor pentru `www-data`
5. Creare fișier `victima.conf` în `sites-available` cu configurare pentru domeniul local
6. Activare site cu `a2ensite` și dezactivare `000-default.conf`
7. Reload la Apache și adăugare nume local în `/etc/hosts`
8. Accesarea aplicației prin `http://victima`

---

## 7. DHCP

DHCP este serviciul care oferă automat IP-uri pentru clienții din rețea.

Fluxul:

1. Se setează un IP static pentru Ubuntu în fișierul Netplan
2. Se configurează rețeaua în VirtualBox pe tipul „Internal Network”
3. Se instalează `isc-dhcp-server`
4. Se editează `dhcpd.conf` cu domeniul și gama de IP-uri
5. Se setează interfața de rețea în `/etc/default/isc-dhcp-server`
6. Se aplică Netplan și se pornește serviciul DHCP
7. Pe client (Windows), se testează primirea IP-ului prin `ipconfig`

---

## 8. DNS (BIND9)

DNS este serviciul care transformă nume în IP-uri.

Fluxul:

1. Se instalează `bind9`, `bind9utils` și `dnsutils`
2. Se configurează o zonă nouă în `named.conf.local`
3. Se copiază fișierul `db.local` și se modifică pentru zona `victima`
4. Se verifică fișierul de zonă cu `named-checkzone` și configurația generală cu `named-checkconf`
5. Se pornește serviciul și se activează permanent
6. Pe client (Kali), se configurează interfața pentru a folosi DNS-ul Ubuntu
7. Se testează cu `ping victima` sau `dig @ip victima`

---

## 9. Docker

Docker permite rularea de aplicații izolate în containere.

Fluxul:

1. Instalare pachete și cheia oficială Docker
2. Adăugarea sursei de pachete Docker și instalarea engine-ului
3. Testare cu `docker run hello-world`
4. Rulare aplicație vulnerabilă DVWA cu port forward (`-p 8080:80`)
5. Accesarea aplicației DVWA din alt VM (ex: Kali) prin IP și portul 8080

---

## 10. FTP

FTP permite transferul de fișiere între client și server.

Fluxul:

1. Instalare `vsftpd`
2. Verificarea serviciului cu `systemctl status vsftpd`
3. Creare utilizator `ftpuser` și directoarele `/home/ftpuser/ftp` și `/upload`
4. Configurare permisiuni (scriere doar în `/upload`)
5. Modificarea fișierului `vsftpd.conf` pentru a permite autentificarea și accesul în directoare
6. Restartarea serviciului și testare cu FileZilla din Windows sau Kali

---

