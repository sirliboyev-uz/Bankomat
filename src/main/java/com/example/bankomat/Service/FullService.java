package com.example.bankomat.Service;

import com.example.bankomat.DTO.*;
import com.example.bankomat.Model.Bankomat;
import com.example.bankomat.Model.Card;
import com.example.bankomat.Model.Datas;
import com.example.bankomat.Model.Enums.RoleName;
import com.example.bankomat.Model.Users;
import com.example.bankomat.Repository.BankomatRepository;
import com.example.bankomat.Repository.CardRepository;
import com.example.bankomat.Repository.DatasRepository;
import com.example.bankomat.Repository.UserRepository;
import com.example.bankomat.WebService.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FullService implements UserDetailsService {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    UserRepository userRepository;

    @Autowired
    Token token;
    @Autowired
    CardRepository cardRepository;

    @Autowired
    BankomatRepository bankomatRepository;
    @Autowired
    DatasRepository datasRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public ApiResponse addCard(CardDTO cardDTO){
        boolean existsByCardNumber = cardRepository.existsByCardNumber(cardDTO.getCardNumber());
        if (!existsByCardNumber){
            if (cardDTO.getCardNumber().toString().length()==16){
                Users users=new Users();
                users.setUsername(cardDTO.getCardNumber());
                users.setFullName(cardDTO.getFullName());
                users.setPassword(passwordEncoder.encode(cardDTO.getCardPassword()));
                users.setBankomat(null);
                userRepository.save(users);
                Card card=new Card();
                card.setCardNumber(cardDTO.getCardNumber());
                card.setBankName(cardDTO.getBankName());
                card.setFullName(cardDTO.getFullName());
                card.setExpDate(cardDTO.getExpDate());
                card.setBalance(cardDTO.getBalance());
                String token1 = token.getToken(cardDTO.getCardNumber(), RoleName.USER);
                System.out.println(token1);
                card.setPassword(passwordEncoder.encode(cardDTO.getCardPassword()));
                String[] c=cardDTO.getCardNumber().toString().split("");
                String a="";
                for (int i = 0; i < 4; i++) {
                    a+=c[i];
                }
                switch (a) {
                    case "8600":
                        card.setCardType("UZCARD");
                        break;
                    case "9860":
                        card.setCardType("HUMO");
                        break;
                    case "4000":
                    case "4046":
                        card.setCardType("VISA");
                        break;
                    default:
                        return new ApiResponse("Not found card type!", true);
                }
                cardRepository.save(card);
                return new ApiResponse("Successfully registered!", true);
            }
            return new ApiResponse("Error card number!",true);
        }
        return new ApiResponse("Card already registered", false);
    }

    public ApiResponse addBankomat(BankomatDTO bankomatDTO){
        boolean existsByPrivateNumber = bankomatRepository.existsByPrivateNumber(bankomatDTO.getPrivateNumber());
        if (!existsByPrivateNumber){
            if (bankomatDTO.getBankomatType().equals("VISA") || bankomatDTO.getBankomatType().equals("UZCARD") || bankomatDTO.getBankomatType().equals("HUMO")){
                Bankomat bankomat=new Bankomat();
                bankomat.setPrivateNumber(bankomatDTO.getPrivateNumber());
                bankomat.setMaxSum(bankomatDTO.getMaxSum());
                bankomat.setBankomatType(bankomatDTO.getBankomatType());
                bankomat.setAlertSum(bankomatDTO.getAlertSum());
                bankomat.setRespCard(bankomatDTO.getRespCard());
                bankomat.setOtherCard(bankomatDTO.getOtherCard());
                bankomat.setAddress(bankomatDTO.getAddress());
                bankomatRepository.save(bankomat);
                return new ApiResponse("Succesfully registered!", true);
            }
            return new ApiResponse("Wrong type bankomat",false);
        }
        return new ApiResponse("Bankomat already registered", false);
    }

    public ApiResponse addManager(ManagerDTO managerDTO){

        if (!userRepository.existsByUsername(managerDTO.getUsername())){
            Optional<Bankomat> optionalBankomat = bankomatRepository.findByPrivateNumber(managerDTO.getBankomat());
            if (optionalBankomat.isPresent()){
                Users users=new Users();
                users.setFullName(managerDTO.getFullName());
                users.setUsername(managerDTO.getUsername());
                users.setPassword(passwordEncoder.encode(managerDTO.getPassword()));
                users.setBankomat(optionalBankomat.get());
                userRepository.save(users);
                return new ApiResponse("Successfully registered manager", true);
            }
            return new ApiResponse("Not found bankomat",false);
        }
        return new ApiResponse("Username already registered", false);
    }

    public ApiResponse takeSumCard(TakeDTO takeDTO){
        Optional<Card> optionalCard = cardRepository.findByCardNumber(takeDTO.getCardNumber());
        if (optionalCard.isPresent()){
            Optional<Bankomat> optionalBankomat = bankomatRepository.findByPrivateNumber(takeDTO.getBankomatNum());
            if (optionalBankomat.isPresent()){
                Bankomat bankomat = optionalBankomat.get();
                if (bankomat.getMaxSum()>=takeDTO.getSum()){
                    Card card = optionalCard.get();
                    if (takeDTO.getCardType().equals(bankomat.getBankomatType())){
                        card.setBalance(card.getBalance() - takeDTO.getSum() - (takeDTO.getSum()*bankomat.getRespCard()/100));
                    }
                    if (!takeDTO.getCardType().equals(bankomat.getBankomatType())){
                        card.setBalance(card.getBalance() - takeDTO.getSum() - (takeDTO.getSum()*bankomat.getOtherCard()/100));
                    }
                    bankomat.setMaxSum(bankomat.getMaxSum()- takeDTO.getSum());
                    Datas data=new Datas();
                    data.setCard(takeDTO.getCardNumber());
                    data.setSum(takeDTO.getSum());
                    data.setIncome(takeDTO.getSum());
                    datasRepository.save(data);
                    bankomatRepository.save(bankomat);
                    cardRepository.save(card);
                    Optional<Users> optionalUsers=userRepository.findByBankomat(bankomat);
                    if (bankomat.getMaxSum() - takeDTO.getSum()<=bankomat.getAlertSum()){
                        emailSender(optionalUsers.get().getUsername(),"Alert"," less than "+bankomat.getAlertSum()+" funds left");
                    }
                    return new ApiResponse("Successfully taken money", true);
                }
                return new ApiResponse("No balance",false);
            }
            return new ApiResponse("Not found bankomat", false);
        }
        return new ApiResponse("Not found card", false);
    }
    public ApiResponse fillSumCard(TakeDTO takeDTO){
        Optional<Card> optionalCard = cardRepository.findByCardNumber(takeDTO.getCardNumber());
        if (optionalCard.isPresent()){
            Optional<Bankomat> optionalBankomat = bankomatRepository.findByPrivateNumber(takeDTO.getBankomatNum());
            if (optionalBankomat.isPresent()){
                Bankomat bankomat = optionalBankomat.get();
                if (bankomat.getMaxSum()>=takeDTO.getSum()){
                    Card card = optionalCard.get();
                    if (takeDTO.getCardType().equals(bankomat.getBankomatType())){
                        card.setBalance(card.getBalance() + takeDTO.getSum() - (takeDTO.getSum()*bankomat.getRespCard()/100));
                    }
                    if (!takeDTO.getCardType().equals(bankomat.getBankomatType())){
                        card.setBalance(card.getBalance() + takeDTO.getSum() - (takeDTO.getSum()*bankomat.getOtherCard()/100));
                    }
                    bankomat.setMaxSum(bankomat.getMaxSum() - takeDTO.getSum());
                    Datas data=new Datas();
                    data.setCard(takeDTO.getCardNumber());
                    data.setSum(takeDTO.getSum());
                    data.setOutcome(takeDTO.getSum());
                    datasRepository.save(data);
                    bankomatRepository.save(bankomat);
                    cardRepository.save(card);
                    Optional<Users> optionalUsers=userRepository.findByBankomat(bankomat);
                    if (bankomat.getMaxSum() - takeDTO.getSum()<=bankomat.getAlertSum()){
                        emailSender(optionalUsers.get().getUsername(),"Alert"," less than "+bankomat.getAlertSum()+" funds left");
                    }
                    return new ApiResponse("Successfully taken money", true);
                }
                return new ApiResponse("No balance",false);
            }
            return new ApiResponse("Not found bankomat", false);
        }
        return new ApiResponse("Not found card", false);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username+" not found username"));
    }
    public void emailSender(String userEmail, String title, String info){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("test");
            mailMessage.setTo(userEmail);
            mailMessage.setSubject(title);
            mailMessage.setText(info);
            javaMailSender.send(mailMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ApiResponse fillBalanceBankomat(FillBankomatDTO fillBankomatDTO){
        Optional<Bankomat> optionalBankomat = bankomatRepository.findByPrivateNumber(fillBankomatDTO.getName());
        if (optionalBankomat.isPresent()){
            Bankomat bankomat = optionalBankomat.get();
            bankomat.setMaxSum(fillBankomatDTO.getSum());
            bankomatRepository.save(bankomat);
            return new ApiResponse("Successfully filled bankomat", true);
        }
        return new ApiResponse("Not found bankomat", false);
    }
}
