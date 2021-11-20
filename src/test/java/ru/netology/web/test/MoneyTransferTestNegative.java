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

        val dashboardPage = new DashboardPage();
        val startBalanceOfFirstCard = dashboardPage.getBalanceOfFirstCard();
        val startBalanceOfSecondCard = dashboardPage.getBalanceOfSecondCard();

        val transferPage = dashboardPage.replenishBalanceSecondCard();
        val transferFrom1To2Card = DataHelper.getFirstCardInfo();
        val transfer = 10022;
        transferPage.transferFrom(transferFrom1To2Card, transfer);
        val balanceFirstCardAfterTrans = DataHelper.getBalanceCardMinus(startBalanceOfFirstCard, transfer);
        val balanceSecondCardAfterTrans = DataHelper.getBalanceCardPlus(startBalanceOfSecondCard, transfer);

        assertEquals(balanceFirstCardAfterTrans, dashboardPage.getBalanceOfFirstCard());

        assertEquals(dashboardPage.getBalanceOfSecondCard(), balanceSecondCardAfterTrans);
    }
}