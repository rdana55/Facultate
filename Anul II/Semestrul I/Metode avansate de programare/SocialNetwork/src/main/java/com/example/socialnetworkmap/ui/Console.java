/*
package com.example.socialnetworkmap.ui;

import com.example.socialnetworkmap.domain.Tuple;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.service.FriendshipService;
import com.example.socialnetworkmap.service.UserService;
import com.example.socialnetworkmap.service.CommunityService;
import com.example.socialnetworkmap.service.dbService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class Console {
    private UserService userService;
    private FriendshipService friendshipService;
    private CommunityService communityService;
    private dbService DbService;

    public Console(UserService userService, FriendshipService friendshipService, CommunityService communityService, dbService DbService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.communityService = communityService;
        this.DbService=DbService;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Adauga utilizator");
            System.out.println("2. Sterge utilizator");
            System.out.println("3. Adauga prieten");
            System.out.println("4. Sterge prieten");
            System.out.println("5. Afisare utilizatori si prieteni");
            System.out.println("6. Afisare numar de comunitati");
            System.out.println("7. Afisare cea mai sociabila comunitate");
            System.out.println("8. Afisare lista cu cel putin N prieteni");
            System.out.println("9. Filtrare prieteni dupa luna");
            System.out.println("10. Afisare utilizatori care contin un anumit string");
            System.out.println("0. Iesire");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Nume utilizator: ");
                    String surname = scanner.next();
                    System.out.println("Prenume utilizator: ");
                    String firstname = scanner.next();
                    User user = userService.createUser(firstname, surname);
                    System.out.println("Utilizator creat cu ID-ul: " + user.getId());
                    break;

                case 2:
                    userService.findAll().forEach(userAll -> {
                        System.out.println("Utilizator: " + userAll.getId() + ". " + userAll.getLastName() + " " + userAll.getFirstName());
                    });
                    System.out.println("ID utilizator: ");
                    Long id = Long.valueOf(scanner.next());
                    Optional<User> userOptional = userService.findUserById(id);
                    User user3 = userOptional.get();
                    userService.deleteUser(user3.getId());
                    break;

                case 3:
                    userService.findAll().forEach(userAll -> {
                        System.out.println("Utilizator: " + userAll.getId() + ". " + userAll.getLastName() + " " + userAll.getFirstName());
                    });
                    System.out.println("ID utilizator 1: ");
                    long id1 = scanner.nextLong();
                    System.out.println("ID utilizator 2: ");
                    long id2 = scanner.nextLong();
                    LocalDateTime date = LocalDateTime.now();
                    friendshipService.addFriendship(id1, id2, date);
                    System.out.println("Prietenie creata in data de: " + date);
                    break;

                case 4:
                    userService.findAll().forEach(userAll -> {
                        System.out.println("Utilizator: " + userAll.getId() + ". " + userAll.getLastName() + " " + userAll.getFirstName());
                    });
                    System.out.println("ID utilizator 1: ");
                    long id3 = scanner.nextLong();
                    System.out.println("ID utilizator 2: ");
                    long id4 = scanner.nextLong();
                    friendshipService.removeFriendship(id3, id4);
                    System.out.println("Prietenie stearsa.");
                    break;

                case 5:
                    userService.findAll().forEach(userAll -> {
                        Set<User> friends = communityService.getFriendsOfUser(userAll);
                        System.out.println("Utilizator: " + userAll.getId() + ". " + userAll.getLastName() + " " + userAll.getFirstName() + " | Prieteni: " + friends);
                    });
                    break;
                case 6:
                    System.out.println("Numarul comunitatilor: " + communityService.getNumberOfCommunities());
                    break;
                case 7:
                    System.out.println("Cea mai sociabila comunitate: " + communityService.findMostSociable());
                    break;
                case 8:
                    System.out.println("Introduceti un numar natural: ");
                    int n=scanner.nextInt();

                    Predicate<User> nonNull=Objects::nonNull;
                    List<User> uFriends = StreamSupport.stream(userService.findAll().spliterator(),false)
                            .filter(nonNull.and(u->communityService.getFriendsOfUser(u).size()>=n))
                            .toList();


                    if (uFriends.isEmpty()){
                        System.out.println("Nu exista utilizatori care sa aiba cel putin "+n+" prieteni");
                    }
                    else{
                        uFriends.forEach(uF->{
                            System.out.println(uF.getId() + ". " + uF.getLastName() + " " + uF.getFirstName() + " | Nr. prieteni: " + communityService.getFriendsOfUser(uF).size());
                        });
                    }
                    break;
                case 9:
                    System.out.println("Introduceti id-ul utilizatorului: ");
                    Long idUser= scanner.nextLong();
                    System.out.println("Introduceti un an: ");
                    String year=scanner.next();
                    System.out.println("Introduceti o luna: ");
                    String month=scanner.next();
                    Set<Tuple<Optional<User>,LocalDateTime>> list = DbService.getFriendsOfUserByMonth(idUser,year,month);
                    if(list.size()>0) {
                        list.forEach(tuple -> {
                            Optional<User> uO = tuple.getLeft();
                            LocalDateTime dateTime = tuple.getRight();
                            uO.ifPresent(value -> System.out.println(value.getLastName() + " " + value.getFirstName() + " | " + dateTime.getDayOfMonth() + " " + dateTime.getMonth() + " " + dateTime.getYear()));
                        });
                        break;
                    }
                    System.out.println("Utilizatorul ales nu si-a facut prieteni in luna respectiva. Fii mai sociabil!");
                    break;

                case 10:
                    System.out.println("Introduceti un sir de caractere: ");
                    String string = scanner.next();
                    List<User> usersByString=userService.findUserByString(string);
                    if(usersByString.isEmpty()){
                        System.out.println("Nu am gasit utilizatori care sa contina acest sir de caractere.");
                        break;
                    }
                    usersByString.forEach(u->{
                        System.out.println(u.getId()+". "+u.getLastName()+ " "+u.getFirstName());
                    });
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Opțiune invalidă.");
                    break;
            }
        }
    }
}
*/