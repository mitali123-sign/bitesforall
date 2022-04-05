package openminder.emeal.service.account;

import lombok.RequiredArgsConstructor;
import openminder.emeal.domain.account.Account;
import openminder.emeal.domain.account.AccountPrincipal;
import openminder.emeal.domain.account.Attendance;
import openminder.emeal.domain.account.Authority;
import openminder.emeal.domain.file.UploadFile;
import openminder.emeal.mapper.account.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUserName(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else {
            return AccountPrincipal.create(account);
        }
    }

    public UserDetails loadUserById(Long id) {
        Account account = accountRepository.findById(id);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        } else {
            return AccountPrincipal.create(account);
        }
    }

    public Account save(Account account, Authority authority) {
        accountRepository.insertUser(account);
        accountRepository.insertUserAuthority(authority);
        return account;
    }

    public void insertAttendance(Attendance attendance) {
        accountRepository.insertAttendance(attendance);
    }

    public List<Attendance> selectAttendance(Attendance attendance) {
        return accountRepository.selectAttendance(attendance);
    }

    public void updateAvatar(UploadFile uploadFile) {
        accountRepository.updateAvatar(uploadFile);
    }

    public int updateAccount(Account account) {
        return accountRepository.updateAccount(account);
    }

    public int updateWaterPlus(String username) {
        return accountRepository.updateWaterPlus(username);
    }

    public int updateWaterMinus(String username) {
        return accountRepository.updateWaterMinus(username);
    }

    public int selectWater(String username) {
        return accountRepository.selectWater(username);
    }

    public int selectWeight(String username) {
        return accountRepository.selectWeight(username);
    }
}
