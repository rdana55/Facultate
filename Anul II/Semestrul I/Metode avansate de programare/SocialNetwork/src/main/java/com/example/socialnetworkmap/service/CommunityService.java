package com.example.socialnetworkmap.service;

import com.example.socialnetworkmap.repository.InMemoryRepo;

public class CommunityService {
    private InMemoryRepo repo;
    private UserService serviceU;
    private FriendshipService serviceF;

    public CommunityService(InMemoryRepo repo, UserService serviceU, FriendshipService serviceF) {
        this.repo = repo;
        this.serviceU = serviceU;
        this.serviceF = serviceF;
    }

    /**
     *
     * @param user
     * @return friends of the user user
     */
    /*
    public Set<User> getFriendsOfUser(User user) {
        Set<Friendship> friendships = repo.findAllF();
        return friendships.stream()
                .filter(friendship -> friendship.containsUser(user.getId()))
                .map(friendship -> {
                    if (user.getId().equals(friendship.getId().getLeft())) {
                        return serviceU.findUserById(friendship.getId().getRight());
                    } else {
                        return serviceU.findUserById(friendship.getId().getLeft());
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        /*
        Set<User> friends = new HashSet<>();
        Iterable<Friendship> friendships = repo.findAllF();
        for (Friendship f : friendships) {
            if (f.containsUser(user.getId())) {
                Optional<User> otherUser;
                if (user.getId().equals(f.getId().getLeft())) {
                    otherUser = serviceU.findUserById(f.getId().getRight());
                } else {
                    otherUser = serviceU.findUserById(f.getId().getLeft());
                }
                otherUser.ifPresent(friends::add);
            }
        }
        return friends;

    }


     */

    /**
     *Searches in depth to find the friendships between users
     * @param start
     * @param component
     * @param visited
     */

    /*
    private void dfs(User start, Set<User> component, Set<User> visited) {
        visited.add(start);
        component.add(start);
        getFriendsOfUser(start).stream()
                .filter(friend -> !visited.contains(friend))
                .forEach(friend -> dfs(friend, component, visited));
    }

     */

    /**
     *
     * @return the communities (groups of friends)
     */
    /*
    private List<Set<User>> findConnectedComponents() {
        Set<User> visited = new HashSet<>();
        Iterable<User> users = repo.findAll();
        List<Set<User>> connectedComp = StreamSupport.stream(users.spliterator(), false)
                .filter(user -> !visited.contains(user))
                .map(user -> {
                    Set<User> component = new HashSet<>();
                    dfs(user, component, visited);
                    return component;
                })
                .collect(Collectors.toList());
        return connectedComp;
    }

     */

    /**
     *
     * @return the number of communities
     */
    /*
    public int getNumberOfCommunities() {
        List<Set<User>> connectedComp = findConnectedComponents();
        return connectedComp.size();
    }

     */

    /**
     *Searches for the community which has the maximum number of users
     * @return the most sociable community
     */
    /*
    public List<Set<User>> findMostSociable() {
        List<Set<User>> connectedComponents = findConnectedComponents();
        if (connectedComponents.isEmpty()) {
            return Collections.emptyList();
        }

        int maxSize = connectedComponents.stream()
                .mapToInt(Set::size)
                .max()
                .orElse(0);

        return connectedComponents.stream()
                .filter(component -> component.size() == maxSize)
                .collect(Collectors.toList());
    }


     */
}
