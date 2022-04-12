package account.service;


import account.entity.User;
import account.request.LockUnlockUserRequest;
import account.request.UserRoleChangeRequest;
import account.response.PasswordChangeSuccessResponse;
import account.userDAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDAO userDAO;

    @Autowired
    LogService service;

    private final List<String> allRoles = List.of("ROLE_USER","ROLE_ADMINISTRATOR","ROLE_ACCOUNTANT","ROLE_AUDITOR");

    @Override
    public User signUp(User user) {
        String email = user.getEmail();
        if (checkUserExist(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String defaultRole = "ROLE_USER";
        user.setRole(defaultRole);
        user.setRoles(new ArrayList<>() {{
            add(defaultRole);
        }});
        userDAO.save(user);
        int id = userDAO.findByEmailIgnoreCase(email).getId();
        user.setId(id);
        return user;
    }

    @Override
    public PasswordChangeSuccessResponse changePassword(String newPassword, UserDetails userDetails) {
        String username = userDetails.getUsername();
        String oldCryptPassword = userDetails.getPassword();
        checkSamePassword(newPassword, oldCryptPassword);

        User user = userDAO.findByEmailIgnoreCase(username);

        String newCryptPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newCryptPassword);

        userDAO.update(user);
        return new PasswordChangeSuccessResponse(username, "The password has been updated successfully");
    }

    private void checkSamePassword(String newPassword, String oldCryptPassword) {
        if (passwordEncoder.matches(newPassword, oldCryptPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }
    }

    @Override
    public List<User> getAllUsersOrderById() {
        List<User> users = userDAO.getAllUsersOrderById();
        return users.stream().peek(user -> user.setRoles(List.of(user.getRole().split(" ")))).collect(Collectors.toList());
    }

    @Override
    public void deleteUserByEmail(String email) {
        if (!checkUserExist(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }

        User user = userDAO.findByEmailIgnoreCase(email);
        if (user.getRoles().contains("ROLE_ADMINISTRATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove a user with a ADMINISTRATOR role!");
        }

        userDAO.deleteUser(email);
    }

    @Override
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    public User changeUserRole(UserRoleChangeRequest request) {
        User user = userDAO.findByEmailIgnoreCase(request.getUser());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        String newRole = "ROLE_" + request.getRole();
        if (!allRoles.contains(newRole)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }

        List<String> userRoles = user.getRoles();
        if (request.getOperation().equals("REMOVE")) {
            removeUserRole(newRole, user);
        } else {
            addUserRole(newRole, user);
        }

        String userRolesString = userRoles.stream().reduce((s, s2) -> s + " " + s2).orElseThrow();
        user.setRole(userRolesString);
        user.setRoles(userRoles);

        userDAO.update(user);
        return user;
    }

    private void addUserRole(String addRole, User user) {
        List<String> userRoles = user.getRoles();
        if (userRoles.contains(addRole)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user already have this role!");
        }
        checkRoleGroupIntersection(addRole, userRoles);
        userRoles.add(addRole);
    }

    private void removeUserRole(String deleteRole, User user) {
        List<String> userRoles = user.getRoles();
        if (!userRoles.contains(deleteRole)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
        }
        if (deleteRole.equals("ROLE_ADMINISTRATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        if (userRoles.size() == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have one role at least!");
        }

        userRoles.remove(deleteRole);
    }

    private void checkRoleGroupIntersection(String addedRole, List<String> userRoles) {
        List<String> businessGroup = List.of("ROLE_USER","ROLE_ACCOUNTANT","ROLE_AUDITOR");
        if (businessGroup.containsAll(userRoles) && addedRole.equals("ROLE_ADMINISTRATOR") ||
        userRoles.contains("ROLE_ADMINISTRATOR") && businessGroup.contains(addedRole)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }
    }

    @Override
    public boolean checkUserExist(String employee) {
        return userDAO.findByEmailIgnoreCase(employee) != null;
    }

    @Override
    public void lockUnlockUser(LockUnlockUserRequest request) {
        User updatedUser = userDAO.findByEmailIgnoreCase(request.getUser());
        if (updatedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        if (updatedUser.getRole().equals("ROLE_ADMINISTRATOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
        }
        userDAO.lockUnlockUser(request.getUser(), request.getOperation());
    }



    @Override
    public User findUserByUsername(String username) {
        return userDAO.findByEmailIgnoreCase(username);
    }

}
