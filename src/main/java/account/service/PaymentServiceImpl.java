package account.service;

import account.entity.Payment;
import account.entity.User;
import account.request.UpdatePaymentRequest;
import account.response.PaymentUserInfo;
import account.userDAO.PaymentDAO;
import account.userDAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentDAO paymentDAO;

    @Autowired
    UserService userService;

    @Override
    public void addPayment(List<Payment> request) {
        paymentDAO.addPayment(request);
    }

    @Override
    public boolean checkUniquePayment(Payment payment) {
        return paymentDAO.findEmailPeriodUnique(payment);
    }

    @Override
    public void updatePaymentByEmployeePeriod(UpdatePaymentRequest payment) {
        paymentDAO.updatePaymentByEmployeePeriod(payment);
    }

    @Override
    public List<PaymentUserInfo> getInfoUserByPeriod(String period, UserDetails userDetails) {
        List<PaymentUserInfo> paymentUserInfoList = paymentDAO.getInfoUserByPeriod(period, userDetails.getUsername());
        User user = findByEmailIgnoreCase(userDetails.getUsername());
        paymentUserInfoList.sort(Comparator.comparing(PaymentUserInfo::getPeriod).reversed());
//        for (PaymentUserInfo info : paymentUserInfoList) {
//            info.setPeriod(info.getPeriod().replaceFirst("0*(?=[^0])", ""));
//            Pattern pattern = Pattern.compile("\\d*");
//            Matcher matcher = pattern.matcher(info.getPeriod());
//            int numberOfMonth = 1;
//            if (matcher.find()) {
//                numberOfMonth = Integer.parseInt(matcher.group());
//            }
//            info.setPeriod(info.getPeriod().replaceFirst("\\d*", Month.of(numberOfMonth).getDisplayName(TextStyle.FULL, Locale.US)));
//            String salary = info.getSalary();
//            String dollarSalary = salary.substring(0, salary.length() - 2);
//            if (dollarSalary.equals("")) {
//                dollarSalary = "0";
//            }
//            String centSalary = salary.substring(salary.length() - 2);
//            info.setSalary(String.format("%s dollar(s) %s cent(s)", dollarSalary, centSalary));
//            info.setName(user.getName());
//            info.setLastname(user.getLastname());
//        }
        return paymentUserInfoList;
    }

    private User findByEmailIgnoreCase(String email) {
        return userService.findUserByUsername(email);
    }
}
