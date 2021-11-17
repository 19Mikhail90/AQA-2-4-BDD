package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTestNegative {

    @Test
    void shouldInvalidVerification() {
        val loginPage = open("http://localhost:9999", LoginPageV2.class);
        val authInfo = DataHelper.getAuthInfoInvalid();
        loginPage.invalidLogin(authInfo);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondMoreThanExist() {
        val loginPage = open("http://localhost:9999", LoginPageV2.class);
        val authInfo = DataHelper.getAuthInfoValid();
        val verificationPage = loginPage.validLogin((DataHelper.AuthInfo) authInfo);
        val verificationCode = DataHelper.getVerificationCode();
        verificationPage.validVerify((DataHelper.VerificationCode) verificationCode);

        val dahsboardPage = new DashboardPage();
        int startBalanceOfFirstCard = dahsboardPage.getBalanceOfFirstCard();
        int startBalanceOfSecondCard = dahsboardPage.getBalanceOfSecondCard();

        val transferPage = dahsboardPage.replenishBalanceSecondCard();
        val transferFrom1To2Card = DataHelper.getFirstCardInfo();
        int transfer = 20001;
        transferPage.transferFromFirstToSecond(transferFrom1To2Card, transfer);
        val balanceFirstCardAfterTrans = DataHelper.getBalanceCardMinus(startBalanceOfFirstCard, transfer);
        val balanceSecondCardAfterTrans = DataHelper.getBalanceCardPlus(startBalanceOfSecondCard, transfer);

        assertEquals(balanceFirstCardAfterTrans, dahsboardPage.getBalanceOfFirstCard());
        assertEquals(balanceSecondCardAfterTrans, dahsboardPage.getBalanceOfSecondCard());
    }
}