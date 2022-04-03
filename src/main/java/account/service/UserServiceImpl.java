package account.service;


import account.entity.Payment;
import account.entity.User;
import account.request.LockUnlockUserRequest;
import account.request.PasswordChangeRequest;
import account.request.UpdatePaymentRequest;
import account.request.UserRoleChangeRequest;
import account.response.PasswordChangeSuccessResponse;
import account.response.PaymentAddSuccessResponse;
import account.response.PaymentUserInfo;
import account.userDAO.UserDAO;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        if (checkEmailUserExist(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String defaultRole = "ROLE_USER";
        user.setRole(defaultRole);
        user.setRoles(List.of(defaultRole));
        userDAO.save(user);
        int id = userDAO.findByEmailIgnoreCase(email).getId();
        user.setId(id);
        return user;
    }

    @Override
    public ResponseEntity<PasswordChangeSuccessResponse> changePassword(PasswordChangeRequest changeRequest, UserDetails userDetails, BindingResult bindingResult) {
        checkSamePassword(changeRequest.getNew_password(), userDetails);
        User userNewPassword = userDAO.findByEmailIgnoreCase(userDetails.getUsername());
        userNewPassword.setPassword(passwordEncoder.encode(changeRequest.getNew_password()));
        userDAO.changePassword(userNewPassword.getId(), userNewPassword.getPassword());
        return new ResponseEntity<>(new PasswordChangeSuccessResponse(userNewPassword.getEmail().toLowerCase(), "The password has been updated successfully"), HttpStatus.OK);
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
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
    public User signIn(UserDetails userDetails) {
        return userDAO.findByEmailIgnoreCase(userDetails.getUsername());
    }

    @Override
    public ResponseEntity<PaymentAddSuccessResponse> addPayment(List<Payment> request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Request body is missing");
        }
        userDAO.addPayment(request);
        return new ResponseEntity<>(new PaymentAddSuccessResponse("Added successfully!"), HttpStatus.OK);
    }


    @Override
    public boolean checkUserExist(String employee) {
        return userDAO.findByEmailIgnoreCase(employee) != null;
    }

    @Override
    public boolean checkUniquePayment(Payment payment) {
        return userDAO.findEmailPeriodUnique(payment);
    }

    @Override
    public void updatePaymentByEmployeePeriod(UpdatePaymentRequest payment) {
        userDAO.updatePaymentByEmployeePeriod(payment);
    }

    @Override
    public ResponseEntity getInfoUserByPeriod(String period, UserDetails userDetails) {
        List<PaymentUserInfo> paymentUserInfoList = userDAO.getInfoUserByPeriod(period, userDetails.getUsername());
        User user = userDAO.findByEmailIgnoreCase(userDetails.getUsername());
        paymentUserInfoList.sort((p1, p2) -> {
            Long monthP1 = Long.parseLong(p1.getPeriod().substring(0, 2));
            Long yearP1 = Long.parseLong(p1.getPeriod().substring(3));
            Long monthP2 = Long.parseLong(p2.getPeriod().substring(0, 2));
            Long yearP2 = Long.parseLong(p2.getPeriod().substring(3));
            if (yearP1.equals(yearP2)) {
                return Long.compare(monthP2, monthP1);
            } else {
                return Long.compare(yearP2, yearP1);
            }
        });
        for (PaymentUserInfo info : paymentUserInfoList) {
            info.setPeriod(info.getPeriod().replaceFirst("0*(?=[^0])", ""));
            Pattern pattern = Pattern.compile("\\d*");
            Matcher matcher = pattern.matcher(info.getPeriod());
            int numberOfMonth = 1;
            if (matcher.find()) {
                numberOfMonth = Integer.parseInt(matcher.group());
            }
            info.setPeriod(info.getPeriod().replaceFirst("\\d*", Month.of(numberOfMonth).getDisplayName(TextStyle.FULL, Locale.US)));
            String salary = info.getSalary();
            String dollarSalary = salary.substring(0, salary.length() - 2);
            if (dollarSalary.equals("")) {
                dollarSalary = "0";
            }
            String centSalary = salary.substring(salary.length() - 2);
            info.setSalary(String.format("%s dollar(s) %s cent(s)", dollarSalary, centSalary));
            info.setName(user.getName());
            info.setLastname(user.getLastname());
        }
        if (paymentUserInfoList.isEmpty()) {
            return new ResponseEntity(List.of(), HttpStatus.OK);
        } else if (paymentUserInfoList.size() == 1) {
            return new ResponseEntity(paymentUserInfoList.get(0), HttpStatus.OK);
        } else {
            return new ResponseEntity(paymentUserInfoList, HttpStatus.OK);
        }
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


    private boolean checkEmailUserExist(String email) {
        return userDAO.findByEmailIgnoreCase(email) != null;
    }


    private void checkSamePassword(String newPassword, UserDetails userDetails) {
        if (passwordEncoder.matches(newPassword, userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }
    }

    @Override
    public User findUserByUsername(String username) {
        return userDAO.findByEmailIgnoreCase(username);
    }
}
